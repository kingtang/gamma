package com.king.caesar.gamma.remoting.netty.handler;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.remoting.util.ResourceUtil;
import com.king.caesar.gamma.rpc.api.message.MessageType;
import com.king.caesar.gamma.rpc.message.MessageImpl;
import com.king.caesar.gamma.rpc.util.CodecUtil;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳handler，当一个链路某个时间内未发生写或读的时候便发送一个心跳
 * 当连续n次未读到数据则关闭链路。
 * @author: Caesar
 * @date: 2017年5月11日 下午11:10:18
 */
public class HeartbeatHandler extends ChannelDuplexHandler
{
    private static final Logger log = LoggerFactory.getLogger(HeartbeatHandler.class);
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
        throws Exception
    {
        if (evt instanceof IdleStateEvent)
        {
            SocketAddress remoteAddress = ctx.channel().remoteAddress();
            final String addressInfo = ResourceUtil.getAddressInfo(remoteAddress);
            IdleState state = ((IdleStateEvent)evt).state();
            // 写事件超时
            if (state == IdleState.WRITER_IDLE)
            {
                log.debug("Send heartbeat request to {}.", addressInfo);
                MessageImpl heartbeat = new MessageImpl();
                heartbeat.setMessageType(MessageType.HEARTBEAT_REQ);
                // outbound数据流由框架负责释放directbuffer
                ctx.writeAndFlush(CodecUtil.encode(heartbeat));
            }
            else if (state == IdleState.READER_IDLE)// 读事件超时
            {
                log.error("Read idle and close the channel with {}.", addressInfo);
                ChannelFuture closedResult = ctx.close();
                closedResult.addListener(new ChannelFutureListener()
                {
                    
                    @Override
                    public void operationComplete(ChannelFuture future)
                        throws Exception
                    {
                        log.error("The channel with {} has closed.", addressInfo);
                    }
                });
            }
        }
    }
}
