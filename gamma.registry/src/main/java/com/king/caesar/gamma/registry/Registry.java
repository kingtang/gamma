package com.king.caesar.gamma.registry;

import java.util.List;

import com.king.caesar.gamma.registry.zookeeper.listener.ServiceListener;
import com.king.caesar.gamma.rpc.api.service.Service;

/**
 * 注册中心
 * 
 * @author: Caesar
 * @date: 2017年5月3日 下午8:41:56
 */
public interface Registry
{
    List<String> subscribe(String path,ServiceListener listener);
    
    List<Service> subscribeService(String path,ServiceListener listener);
    
    String getData(String path);
    
    boolean unSubscribe(String path);
    
    void register(String path,String zkData);
    
    void connect(String connectString);
}
