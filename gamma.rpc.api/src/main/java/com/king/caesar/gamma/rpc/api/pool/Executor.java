package com.king.caesar.gamma.rpc.api.pool;

import io.netty.buffer.ByteBuf;

public interface Executor
{
    void execute(ByteBuf buffer,Runnable task);
    
    void execute(Runnable task);
}
