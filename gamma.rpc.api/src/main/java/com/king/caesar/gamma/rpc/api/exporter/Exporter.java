package com.king.caesar.gamma.rpc.api.exporter;

import com.king.caesar.gamma.rpc.api.service.Service;

public interface Exporter
{
    void export(Service service);
    
    void unexport();
}
