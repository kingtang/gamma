package com.king.caesar.gamma.remoting.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.core.constants.GammaConstants;
import com.king.caesar.gamma.remoting.api.Connection;
import com.king.caesar.gamma.remoting.netty.NettyRemotingServer;
import com.king.caesar.gamma.remoting.netty.connection.ConnectionImpl;
import com.king.caesar.gamma.remoting.task.DecodeTask;
import com.king.caesar.gamma.rpc.api.pool.Executor;
import com.king.caesar.gamma.rpc.codec.buffer.NettyBufferWrapper;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class ServerServiceHandler extends ChannelInboundHandlerAdapter
{
    private static final Logger log = LoggerFactory.getLogger("service_log");
    
    private NettyRemotingServer remotingServer;
    
    private Executor executor;
    
    public ServerServiceHandler(Executor executor, NettyRemotingServer remotingServer)
    {
        this.executor = executor;
        this.remotingServer = remotingServer;
    }
    
    /**
     * 连接一旦建立，先执行注册逻辑，后执行active
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx)
        throws Exception
    {
        System.err.println("channel channelRegistered.......");
        super.channelRegistered(ctx);
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx)
        throws Exception
    {
        System.err.println("channel active.......");
        // 连接激活后创建客户端连接
        ConnectionImpl clientConnection = new ConnectionImpl(ctx.channel());
        String clientId = remotingServer.addAuthedConnection(clientConnection);
        ctx.attr(AttributeKey.valueOf(GammaConstants.Remoting.CLIENTID)).setIfAbsent(clientId);
        ctx.fireChannelActive();
    }
    
    /**
     * 客户端发送的请求由该方法负责读取
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception
    {
        log.debug("Receive a msg from client.");
        ByteBuf buffer = (ByteBuf)msg;
        String clientId = (String)ctx.attr(AttributeKey.valueOf(GammaConstants.Remoting.CLIENTID)).get();
        Connection authedConnection = remotingServer.getAuthedConnections(clientId);
        DecodeTask task = new DecodeTask(new NettyBufferWrapper(buffer), authedConnection);
        if(null != executor)
        {
            executor.execute(buffer, task);
        }
        else
        {
            task.run();
        }
    }
    
    /**
     * 读取完成
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
        throws Exception
    {
        System.err.println("channel read complete.......");
        super.channelReadComplete(ctx);
    }
    
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx)
        throws Exception
    {
        System.err.println("channel WritabilityChanged.......");
        super.channelWritabilityChanged(ctx);
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx)
        throws Exception
    {
        System.err.println("channel channelUnregistered.......");
        super.channelUnregistered(ctx);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx)
        throws Exception
    {
        System.err.println("channel channelInactive.......");
        log.error("Channel inactive.");
        // super.channelInactive(ctx);
        ctx.close();
        ctx.fireChannelInactive();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception
    {
        System.err.println("channel exceptionCaught.......");
        super.exceptionCaught(ctx, cause);
    }
    
}
