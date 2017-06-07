package com.king.caesar.gamma.registry.zookeeper.listener;

import com.king.caesar.gamma.rpc.api.service.Service;

public interface ServiceListener
{
    void serviceAdded(Service addedService,String servicePath);
}
