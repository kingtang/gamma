package com.king.caesar.gamma.remoting.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.core.attribute.Attribute;
import com.king.caesar.gamma.core.attribute.ConfigAttribute;
import com.king.caesar.gamma.remoting.netty.NettyRemotingServer;
import com.king.caesar.gamma.rpc.api.pool.Executor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ServerInitializer extends ChannelInitializer<SocketChannel>
{
    private static final Logger log = LoggerFactory.getLogger(ServerInitializer.class);
    
    private NettyRemotingServer remotingServer;
    
    public ServerInitializer(NettyRemotingServer remotingServer)
    {
        this.remotingServer = remotingServer;
    }
    
    /**
     * TCP一旦Established便会回调该方法
     */
    @Override
    protected void initChannel(SocketChannel ch)
        throws Exception
    {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("freamHaderDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 4, 4));
        pipeline.addLast("idleHandler", new IdleStateHandler(5, 4, 0));
        pipeline.addLast("heartbeatHandler", new HeartbeatHandler());
        pipeline.addLast("msgHandler",
            new ServerServiceHandler((Executor)ConfigAttribute.SERVER_DECODE_POOL.value,remotingServer));
        log.info("init channel.");
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx)
        throws Exception
    {
        log.info("channelActive.");
       
        ctx.fireChannelActive();
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx)
        throws Exception
    {
        System.err.println("channel inactive");
        super.channelInactive(ctx);
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception
    {
        System.err.println("channel read");
        super.channelRead(ctx, msg);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
        throws Exception
    {
        System.err.println("channel read complete");
        super.channelReadComplete(ctx);
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx)
        throws Exception
    {
        System.err.println("channel Unregistered");
        super.channelUnregistered(ctx);
    }
    
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx)
        throws Exception
    {
        System.err.println("channel WritabilityChanged");
        super.channelWritabilityChanged(ctx);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception
    {
        System.err.println("channel exceptionCaught");
        super.exceptionCaught(ctx, cause);
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
        throws Exception
    {
        System.err.println("channel userEventTriggered");
        super.userEventTriggered(ctx, evt);
    }
    
}
