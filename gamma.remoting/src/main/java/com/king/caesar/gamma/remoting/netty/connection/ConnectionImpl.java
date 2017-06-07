package com.king.caesar.gamma.remoting.netty.connection;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.core.attribute.ConfigAttribute;
import com.king.caesar.gamma.remoting.api.AbstractConnection;
import com.king.caesar.gamma.remoting.api.Connection;
import com.king.caesar.gamma.remoting.api.RemoteInfo;
import com.king.caesar.gamma.remoting.task.EncodeTask;
import com.king.caesar.gamma.remoting.util.ResourceUtil;
import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.api.pool.Executor;
import com.king.caesar.gamma.rpc.message.MessageImpl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * 客户端连接
 * 
 * @author: Caesar
 * @date: 2017年5月14日 下午4:11:09
 */
public class ConnectionImpl extends AbstractConnection
{
    private static final Logger log = LoggerFactory.getLogger(ConnectionImpl.class);
    
    // 和服务端的channel
    private Channel channel;
    
    private RemoteInfo remoteInfo;
    
    public ConnectionImpl(Channel channel)
    {
        this.channel = channel;
        remoteInfo = new RemoteInfo();
        if (isConnected())
        {
            SocketAddress remoteAddress = channel.remoteAddress();
            String addressInfo = ResourceUtil.getAddressInfo(remoteAddress);
            remoteInfo.setAddress(addressInfo);
        }
        remoteInfo.setAddress("");
    }
    
    @Override
    public Channel getChannel()
    {
        return channel;
    }
    
    @Override
    public boolean isConnected()
    {
        return channel.isActive();
    }
    
    public RemoteInfo getRemoteInfo()
    {
        return remoteInfo;
    }
    
    @Override
    public void close()
    {
        ChannelFuture closedResult = channel.close();
        closedResult.addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(ChannelFuture future)
                throws Exception
            {
                if (future.isSuccess())
                {
                    log.info("Channel has been closed.");
                }
                else
                {
                    if (null != future.cause())
                    {
                        log.error("Channel close failed.", future.cause());
                    }
                }
            }
        });
    }
    
    @Override
    public boolean sendResponse(Object msg)
    {
        if (msg instanceof MessageImpl)
        {
            EncodeTask encodeTask = new EncodeTask((Message)msg, this);
            // TODO 获取编码线程池
            if (null != ConfigAttribute.SERVER_ENCODE_POOL.value)
            {
                Executor executor = ConfigAttribute.SERVER_ENCODE_POOL.value;
                executor.execute(encodeTask);
            }
            else
            {
                encodeTask.run();
                return true;
            }
        }
        return false;
    }
    
}
