package com.king.caesar.gamma.remoting.api;

import java.io.IOException;

import com.king.caesar.gamma.rpc.api.context.Context;

import io.netty.channel.Channel;

public abstract class AbstractConnection implements Connection,Callback
{

    @Override
    public void close()
        throws IOException
    {
        
    }

    @Override
    public void registerCallback(Long reqId, Context context)
    {
        
    }

    @Override
    public Context unregisterCallback(Long reqId)
    {
        return null;
    }

    @Override
    public Channel getChannel()
    {
        return null;
    }

    @Override
    public boolean isConnected()
    {
        return false;
    }

    @Override
    public boolean sendResponse(Object msg)
    {
        return false;
    }

    @Override
    public RemoteInfo getRemoteInfo()
    {
        return null;
    }
}
