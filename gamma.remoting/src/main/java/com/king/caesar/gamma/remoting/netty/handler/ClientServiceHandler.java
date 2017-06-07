package com.king.caesar.gamma.remoting.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.remoting.api.Connection;
import com.king.caesar.gamma.remoting.task.DecodeTask;
import com.king.caesar.gamma.rpc.api.pool.Executor;
import com.king.caesar.gamma.rpc.codec.buffer.NettyBufferWrapper;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 客户端业务Handler，负责读取服务端的响应并投递到线程池。
 * 
 * @author: Caesar
 * @date: 2017年5月30日 上午11:56:38
 */
public class ClientServiceHandler extends ChannelInboundHandlerAdapter
{
    private static final Logger log = LoggerFactory.getLogger(ClientServiceHandler.class);
    
    // 客户端处理响应的线程池
    private Executor executor;
    
    private Connection clientConnection;
    
    public ClientServiceHandler(Executor executor, Connection clientConnection)
    {
        this.executor = executor;
        this.clientConnection = clientConnection;
    }
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx)
        throws Exception
    {
        // TODO Auto-generated method stub
        super.channelRegistered(ctx);
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx)
        throws Exception
    {
        System.err.println("channel channelUnregistered.......");
        super.channelUnregistered(ctx);
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx)
        throws Exception
    {
        // TODO Auto-generated method stub
        super.channelActive(ctx);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx)
        throws Exception
    {
        System.err.println("channel channelInactive.......");
        super.channelInactive(ctx);
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
        throws Exception
    {
        log.trace("Received a response from server.");
        ByteBuf buffer = (ByteBuf)msg;
        NettyBufferWrapper bufferWrapper = new NettyBufferWrapper(buffer);
        DecodeTask task = new DecodeTask(bufferWrapper, clientConnection);
        if (null != executor)
        {
            // 业务线程处理
            executor.execute(task);
        }
        else
        {
            // Object request = CodecUtil.decode(bufferWrapper);
            // 由netty的worker线程处理
            task.run();
        }
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
        throws Exception
    {
        // TODO Auto-generated method stub
        super.channelReadComplete(ctx);
    }
    
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx)
        throws Exception
    {
        System.err.println("channel channelWritabilityChanged.......");
        super.channelWritabilityChanged(ctx);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception
    {
        System.err.println("channel exceptionCaught.......");
        super.exceptionCaught(ctx, cause);
    }
    
}
