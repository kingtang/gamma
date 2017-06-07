package com.king.caesar.gamma.registry.zookeeper.listener;


import com.king.caesar.gamma.registry.Registry;
import com.king.caesar.gamma.registry.zookeeper.event.Event;

public interface ConnectionStatusListener
{
    void statusChanged(Registry registry,Event.StatusType status);
}
