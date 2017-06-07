package com.king.caesar.gamma.connector.gamma;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.fastjson.JSON;
import com.king.caesar.gamma.connector.AbstractConnector;
import com.king.caesar.gamma.core.constants.GammaConstants.Attribute;
import com.king.caesar.gamma.core.util.ConfigName;
import com.king.caesar.gamma.core.util.ContextHolder;
import com.king.caesar.gamma.engine.Engine;
import com.king.caesar.gamma.engine.EngineAware;
import com.king.caesar.gamma.registry.Registry;
import com.king.caesar.gamma.registry.RegistryFactory;
import com.king.caesar.gamma.remoting.api.ClientOption;
import com.king.caesar.gamma.remoting.api.RemotingClient;
import com.king.caesar.gamma.remoting.netty.NettyRemotingClient;
import com.king.caesar.gamma.rpc.api.context.Context;
import com.king.caesar.gamma.rpc.api.exporter.Exporter;
import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.api.service.Service;
import com.king.caesar.gamma.rpc.service.LocalService;
import com.king.caesar.gamma.rpc.service.RemoteService;
import com.king.caesar.gamma.rpc.service.LocalService.ServiceKey;
import com.king.caesar.gamma.rpc.service.wrapper.RemoteServiceWrapper;

/**
 * GammaConnector负责处理Gamma协议的消息
 * 
 * @author: Caesar
 * @date: 2017年5月21日 下午12:55:08
 */
public class GammaConnector extends AbstractConnector
    implements ApplicationContextAware, EngineAware, Exporter, RemoteServiceWrapper
{
    // 服务引擎
    private Engine engine;
    
    private volatile ConcurrentMap<String/* ip:port */, RemoteService> remoteServices =
        new ConcurrentHashMap<String, RemoteService>();
    
    private Object serviceLock = new Object();
    
    @Override
    public Message createRequest()
    {
        return null;
    }
    
    @Override
    public String getProtocol()
    {
        return "gamma";
    }
    
    /**
     * 导出服务
     */
    @Override
    public void export(Service service)
    {
        String host = ConfigName.IP;
        String port = ConfigName.PORT;
        
        //
        
        String registryType = service.getExtInfos().get("registryType");
        Registry registry = RegistryFactory.getInstance().getRegistry(registryType);
        // 避免spring生成实例的时候产生不用的加载，使用时需调用connect方法，到此时才会真正产生到registry的连接。
        registry.connect(ConfigName.REGISTRY_ADDRESS);
        registry.register(service.getPath(), JSON.toJSONString(service));
    }
    
    @Override
    public void unexport()
    {
        
    }
    
    @Override
    public void setEngine(Engine engine)
    {
        this.engine = engine;
    }
    
    /**
     * connector接入的消息，由ServiceEngine处理
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <T> T doOnReceive(Context context)
    {
        // 设置流程方向
        context.setAttribute(Attribute.PROCESSTYPEKEY, Attribute.INPROCESS);
        // 前后两步可以提取到父类中 TODO
        context.setSrcConnector(this);
        engine.service(context);
        return (T)context.getResponse();
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException
    {
        ContextHolder.setContext(applicationContext);
    }
    
    @Override
    public Message createResponse(Message request)
    {
        return null;
    }
    
    /**
     * 连接的建立推迟到这里
     */
    @Override
    public RemoteService getRemoteService(String ip, String port)
    {
        String key = ip.concat(":").concat(port);
        RemoteService remoteService = remoteServices.get(key);
        if (null == remoteService)
        {
            synchronized (serviceLock)
            {
                remoteService = remoteServices.get(key);
                if (null == remoteService)
                {
                    remoteService = new RemoteService();
                    ClientOption option = new ClientOption();
                    option.setRemoteIp(ip);
                    option.setPort(port);
                    RemotingClient client = new NettyRemotingClient(option);
                    client.init();
                    // 建立连接
                    client.connect();
                    remoteService.setClient(client);
                    remoteServices.putIfAbsent(key, remoteService);
                }
            }
        }
        return remoteService;
    }
    
}
