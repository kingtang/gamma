package com.king.caesar.gamma.registry.zookeeper.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.king.caesar.gamma.registry.zookeeper.listener.ConnectionStatusListener;

public abstract class AbstractEventService implements EventService
{
    protected List<ConnectionStatusListener> statusListeners = new CopyOnWriteArrayList<ConnectionStatusListener>();
    
    @Override
    public void addStatusListener(ConnectionStatusListener listener)
    {
        statusListeners.add(listener);
    }
    
    @Override
    public void removeStatusListener(ConnectionStatusListener listener)
    {
        statusListeners.remove(listener);
    }
    
}
