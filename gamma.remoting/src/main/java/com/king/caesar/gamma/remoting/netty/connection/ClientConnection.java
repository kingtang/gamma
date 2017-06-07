package com.king.caesar.gamma.remoting.netty.connection;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.king.caesar.gamma.remoting.api.AbstractConnection;
import com.king.caesar.gamma.remoting.api.RemoteInfo;
import com.king.caesar.gamma.remoting.api.RemotingClient;
import com.king.caesar.gamma.rpc.api.buffer.BufferWrapper;
import com.king.caesar.gamma.rpc.api.context.Context;
import com.king.caesar.gamma.rpc.util.CodecUtil;

import io.netty.channel.Channel;

/**
 * 客户端连接
 * 
 * @author: Caesar
 * @date: 2017年6月4日 下午8:51:06
 */
public class ClientConnection extends AbstractConnection
{
    private RemotingClient client;
    
    private ConcurrentMap<Long, Context> callbacks = new ConcurrentHashMap<Long, Context>();
    
    public ClientConnection(RemotingClient client)
    {
        this.client = client;
    }
    
    @Override
    public void registerCallback(Long reqId, Context context)
    {
        callbacks.putIfAbsent(reqId, context);
    }
    
    @Override
    public Context unregisterCallback(Long reqId)
    {
        return callbacks.remove(reqId);
    }
    
    @Override
    public void close()
        throws IOException
    {
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
        BufferWrapper encodedInfo = CodecUtil.encode(msg);
        // 发送异步请求
        client.sendOneway(encodedInfo);
        return true;
    }
    
    @Override
    public RemoteInfo getRemoteInfo()
    {
        return null;
    }
    
    public RemotingClient getClient()
    {
        return client;
    }
    
    public void setClient(RemotingClient client)
    {
        this.client = client;
    }
}
