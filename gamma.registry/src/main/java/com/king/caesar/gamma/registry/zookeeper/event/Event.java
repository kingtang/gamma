package com.king.caesar.gamma.registry.zookeeper.event;

public interface Event
{
    enum StatusType
    {
        CONNECTED(0), LOST(1), RECONNECTED(2);
        private final int intValue;
        
        StatusType(int intValue)
        {
            this.intValue = intValue;
        }
        
        public int getIntValue()
        {
            return this.intValue;
        }
    }
    
    enum ServiceType
    {
        PROVIDER_SERVICE_ADD, PROVIDER_SERVICE_DELETE, PROVIDER_SERVICE_UPDATE;
    }
}
