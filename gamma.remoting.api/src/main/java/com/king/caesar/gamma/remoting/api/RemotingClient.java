package com.king.caesar.gamma.remoting.api;


import io.netty.channel.ChannelFuture;

public interface RemotingClient
{
    void init();
    
    Connection connect();
    
    ChannelFuture sendOneway(Object message);
    
    boolean isInited();
    
    ClientOption getClientOption();
    
    AbstractConnection getRegister();
}
