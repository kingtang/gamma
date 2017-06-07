package com.king.caesar.gamma.registry;

import java.io.Closeable;
import java.util.List;

import com.king.caesar.gamma.registry.zookeeper.listener.ServiceListener;
import com.king.caesar.gamma.rpc.api.service.Service;

/**
 * 注册中心
 * 
 * @author: Caesar
 * @date: 2017年5月3日 下午8:41:56
 */
public interface Registry extends Closeable
{
    List<String> subscribe(String path,ServiceListener listener);
    
    List<Service> subscribeService(String path,ServiceListener listener);
    
    //获取数据
    String getData(String path);
    
    //去注册
    boolean unSubscribe(String path);
    
    //注册服务
    void register(String path,String zkData);
    
    //连接注册中心
    void connect(String connectString);
    
    //资源关闭接口
    void close();
    
    //检查节点是否存在
    boolean checkExists(String path);
}
