package com.king.caesar.gamma.rpc.service.wrapper;

import com.king.caesar.gamma.rpc.service.LocalService;

public interface LocalServiceWrapper
{
    void addService(LocalService service);
    
    LocalService findService(LocalService.ServiceKey serviceKey);
}
