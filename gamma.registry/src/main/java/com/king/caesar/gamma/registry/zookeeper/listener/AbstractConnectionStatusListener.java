package com.king.caesar.gamma.registry.zookeeper.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.registry.Registry;
import com.king.caesar.gamma.registry.zookeeper.event.Event;

public abstract class AbstractConnectionStatusListener implements ConnectionStatusListener
{
    protected Logger log = LoggerFactory.getLogger(getClass());
    
    private List<Event.StatusType> acceptedEvents;
    
    @Override
    public void statusChanged(Registry registry, Event.StatusType status)
    {
        log.debug("Registry connected satus changed to {}.", status);
        if (acceptedEvents.contains(status))
        {
            doStatusChanged();
        }
    }
    
    // 模板方法
    protected abstract void doStatusChanged();
    
    /**
     * 注册关注的事件
     * 
     * @param statusTypes
     */
    public void registerEvent(Event.StatusType... statusTypes)
    {
        if (null == statusTypes)
        {
            acceptedEvents.add(Event.StatusType.CONNECTED);
            acceptedEvents.add(Event.StatusType.LOST);
            acceptedEvents.add(Event.StatusType.RECONNECTED);
        }
        else
        {
            for (Event.StatusType statusType : statusTypes)
            {
                acceptedEvents.add(statusType);
            }
        }
    }
}
