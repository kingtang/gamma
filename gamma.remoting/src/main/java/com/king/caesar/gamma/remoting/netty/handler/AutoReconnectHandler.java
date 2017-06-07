package com.king.caesar.gamma.remoting.netty.handler;

import java.net.SocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.remoting.api.RemotingClient;
import com.king.caesar.gamma.remoting.netty.NettyRemotingClient;
import com.king.caesar.gamma.remoting.util.ResourceUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * 客户端重连handler 当前只提供简单的时间递增重试，后面计划提供不同的策略供用户选择。
 * 
 * @author: Caesar
 * @date: 2017年5月14日 上午11:38:16
 */
@Sharable
public class AutoReconnectHandler extends ChannelInboundHandlerAdapter
{
    public static final AttributeKey<SocketAddress> key = AttributeKey.valueOf("remoteAddress");
    
    private static final Logger log = LoggerFactory.getLogger(AutoReconnectHandler.class);
    
    // 最大重连次数
    private static final int MAX_RECONNECT_TIMES = 10;
    
    private RemotingClient client;
    
    // 重连次数
    private int reconnectTimes;
    
    public AutoReconnectHandler(RemotingClient client)
    {
        this.client = client;
        reconnectTimes = 0;
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx)
        throws Exception
    {
        if (reconnectTimes <= MAX_RECONNECT_TIMES)
        {
            int internalTime = reconnectTimes << 2;
            reconnectTimes++;
            
            final ChannelHandlerContext channelCtx = ctx;
            TimerTask timerTask = new TimerTask()
            {
                
                @Override
                public void run()
                {
                    SocketAddress remoteAddress = channelCtx.channel().remoteAddress();
                    Bootstrap bootstrap = ((NettyRemotingClient)client).getBootstrap();
                    ChannelFuture connected = null;
                    if (null == remoteAddress)
                    {
                        remoteAddress = (SocketAddress)channelCtx.channel().attr(key).get();
                    }
                    final String addressInfo = ResourceUtil.getAddressInfo(remoteAddress);
                    connected = bootstrap.connect(remoteAddress);
                    connected.channel().attr(key).set(remoteAddress);
                    connected.addListener(new ChannelFutureListener()
                    {
                        @Override
                        public void operationComplete(ChannelFuture future)
                            throws Exception
                        {
                            if (future.isSuccess())
                            {
                                log.info("Reconnect with {} successed.", addressInfo);
                            }
                            else
                            {
                                log.error("Reconnect with {} failed.", addressInfo);
                                future.channel().pipeline().fireChannelInactive();
                            }
                        }
                    });
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask, internalTime * 1000);
        }
        else
        {
            log.info("Reconnect times has reached max times and reconnect failed.");
        }
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx)
        throws Exception
    {
        reconnectTimes = 0;
        super.channelActive(ctx);
    }
    
}
