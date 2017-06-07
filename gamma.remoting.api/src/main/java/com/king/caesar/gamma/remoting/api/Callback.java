package com.king.caesar.gamma.remoting.api;

import com.king.caesar.gamma.rpc.api.context.Context;

public interface Callback
{
    void registerCallback(Long reqId, Context context);
    
    Context unregisterCallback(Long reqId);
}
