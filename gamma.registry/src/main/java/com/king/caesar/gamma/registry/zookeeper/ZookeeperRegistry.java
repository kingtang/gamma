package com.king.caesar.gamma.registry.zookeeper;

import java.io.Closeable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.king.caesar.gamma.core.util.ConfigName;
import com.king.caesar.gamma.registry.Registry;
import com.king.caesar.gamma.registry.zookeeper.event.AbstractEventService;
import com.king.caesar.gamma.registry.zookeeper.event.Event;
import com.king.caesar.gamma.registry.zookeeper.event.Event.StatusType;
import com.king.caesar.gamma.registry.zookeeper.listener.AbstractConnectionStatusListener;
import com.king.caesar.gamma.registry.zookeeper.listener.ConnectionStatusListener;
import com.king.caesar.gamma.registry.zookeeper.listener.ServiceListener;
import com.king.caesar.gamma.rpc.api.service.Service;

/**
 * 服务注册中心基于zookeeper实现
 * 
 * @author: Caesar
 * @date: 2017年5月24日 下午9:57:35
 */
public class ZookeeperRegistry extends AbstractEventService implements Registry
{
    private static final Logger log = LoggerFactory.getLogger(ZookeeperRegistry.class);
    
    private volatile boolean inited = false;
    
    private CuratorFramework zkClient;
    
    private List<Closeable> closeableComps = new ArrayList<Closeable>();
    
    // 连接zookeeper
    public synchronized void connect(String connectString)
    {
        if (inited)
        {
            return;
        }
        zkClient = CuratorFrameworkFactory.builder()
            .connectString(connectString)
            .connectionTimeoutMs(ConfigName.REGISTRY_CONNECT_TIMEOUT)
            .sessionTimeoutMs(ConfigName.REGISTRY_SESSION_TIMEOUT)
            .retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 3000))
            .build();
        // 注册服务恢复监听
        this.addStatusListener(new RecoverService());
        // 注册状态监听器
        zkClient.getConnectionStateListenable().addListener(new ConnectionStateListener()
        {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState)
            {
                if (newState == ConnectionState.LOST)
                {
                    ZookeeperRegistry.this.stateChanged(Event.StatusType.LOST);
                }
                else if (newState == ConnectionState.CONNECTED)
                {
                    ZookeeperRegistry.this.stateChanged(Event.StatusType.CONNECTED);
                }
                else if (newState == ConnectionState.RECONNECTED)
                {
                    ZookeeperRegistry.this.stateChanged(Event.StatusType.RECONNECTED);
                }
            }
        });
        // 启动zk连接
        zkClient.start();
        inited = true;
        log.info("ZookeeperClient connect with  {}  successfully.", connectString);
    }
    
    /**
     * 状态变更通知，由于上层已经是curator的线程了，这里就不用单独的线程去通知了。
     * 
     * @param statusType
     */
    public void stateChanged(Event.StatusType statusType)
    {
        for (ConnectionStatusListener connectionStatusListener : statusListeners)
        {
            connectionStatusListener.statusChanged(this, statusType);
        }
    }
    
    /**
     * 订阅服务
     */
    @Override
    public List<String> subscribe(String path, final ServiceListener listener)
    {
        if (!checkExists(path))
        {
            try
            {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
            }
            catch (Exception e)
            {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        
        // 创建缓存
        PathChildrenCache pathCache = new PathChildrenCache(zkClient, path, true);
        closeableComps.add(pathCache);
        PathChildrenCacheListener pathCacheListener = new PathChildrenCacheListener()
        {
            
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                throws Exception
            {
                ChildData data = event.getData();
                Service obj = JSON.parseObject(data.getData(), Service.class);
                String path = data.getPath();
                Type type = event.getType();
                switch (type)
                {
                    case CHILD_ADDED:
                        log.info("child[{}] added", path);
                        listener.serviceAdded(obj, path);
                        break;
                    case CHILD_REMOVED:
                        log.info("child[{}] deleted", path);
                        listener.serviceDeleted(obj, path);
                        break;
                    case CHILD_UPDATED:
                        System.out.println("child update");
                        break;
                    default:
                        break;
                }
                
            }
        };
        pathCache.getListenable().addListener(pathCacheListener);
        try
        {
            pathCache.start();
            List<String> service = zkClient.getChildren().forPath(path);
            return service;
        }
        catch (Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
    
    public boolean checkExists(String path)
    {
        try
        {
            Stat state = zkClient.checkExists().forPath(path);
            if (null != state)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return false;
    }
    
    @Override
    public boolean unSubscribe(String path)
    {
        return false;
    }
    
    class RecoverService extends AbstractConnectionStatusListener
    {
        public RecoverService()
        {
            this.registerEvent(StatusType.RECONNECTED);
        }
        
        @Override
        protected void doStatusChanged()
        {
            ZookeeperRegistry.this.recovery();
        }
        
    }
    
    /**
     * 用于zk断链后的服务恢复
     */
    public void recovery()
    {
        
    }
    
    /**
     * 向Zookeeper服务端注册节点，常用的场景可能包括:<b>
     * <p>
     * 服务端注册提供的服务
     * <p>
     * 客户端注册消费者信息
     */
    @Override
    public void register(String path, String zkData)
    {
        try
        {
            zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,
                zkData.getBytes("UTF-8"));
        }
        catch (NodeExistsException t)
        {
            // ignore
        }
        catch (Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
        
    }
    
    /*
     * 订阅服务并设置监听
     * 
     * @see
     * com.king.caesar.gamma.registry.Registry#subscribeService(java.
     * lang.String, com.king.caesar.gamma.registry.zookeeper.listener.
     * ServiceListener)
     */
    @Override
    public List<Service> subscribeService(String path, ServiceListener listener)
    {
        List<String> services = subscribe(path, listener);
        if (null == services)
        {
            return null;
        }
        List<Service> providers = new ArrayList<Service>();
        for (String service : services)
        {
            String servicePath = path + "/" + service;
            String data = getData(servicePath);
            Service provider = JSON.parseObject(data, Service.class);
            providers.add(provider);
            listener.serviceAdded(provider, servicePath);
        }
        return providers;
    }
    
    @Override
    public String getData(String path)
    {
        try
        {
            byte[] data = zkClient.getData().forPath(path);
            return new String(data, Charset.forName("UTF-8"));
        }
        catch (Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
    
    @Override
    public void close()
    {
        for (Closeable closeable : closeableComps)
        {
            try
            {
                closeable.close();
            }
            catch (Exception e)
            {
                log.error("Close resource failed.", e);
            }
        }
        // 关闭zk
        zkClient.close();
    }
    
    @Override
    public boolean unRegister(String path)
    {
        if (checkExists(path))
        {
            try
            {
                zkClient.delete().forPath(path);
            }
            catch (Exception e)
            {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return true;
    }
}
