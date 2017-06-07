package com.king.caesar.gamma.rpc.service.wrapper;

import com.king.caesar.gamma.rpc.service.RemoteService;

public interface RemoteServiceWrapper
{
    RemoteService getRemoteService(String ip,String port);
}
