package com.king.caesar.gamma.remoting.util;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public abstract class ResourceUtil
{
    public static final Logger log = LoggerFactory.getLogger(ResourceUtil.class);
    
    public static void close(ChannelFuture channelFuture)
    {
        Channel channel = channelFuture.channel();
        if (null != channel)
        {
            SocketAddress remote = channel.remoteAddress();
            final String remoteAddress = getAddressInfo(remote);
            channel.close().addListener(new ChannelFutureListener()
            {
                @Override
                public void operationComplete(ChannelFuture future)
                    throws Exception
                {
                    log.info("Channel with [{}] has been closed.", remoteAddress);
                }
            });
        }
    }
    
    public static String getAddressInfo(SocketAddress remote)
    {
        String remoteAddress = null == remote ? "" : remote.toString();
        if (remoteAddress.startsWith("/"))
        {
            remoteAddress = remoteAddress.substring(1);
        }
        return remoteAddress;
    }
}
