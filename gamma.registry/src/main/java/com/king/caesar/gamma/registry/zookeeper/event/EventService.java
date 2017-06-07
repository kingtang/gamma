package com.king.caesar.gamma.registry.zookeeper.event;

import com.king.caesar.gamma.registry.zookeeper.listener.ConnectionStatusListener;

/**
 * 事件服务
 * 
 * @author: Caesar
 * @date: 2017年5月3日 下午8:09:32
 */
public interface EventService
{
    void addStatusListener(ConnectionStatusListener listener);
    
    void removeStatusListener(ConnectionStatusListener listener);
}
