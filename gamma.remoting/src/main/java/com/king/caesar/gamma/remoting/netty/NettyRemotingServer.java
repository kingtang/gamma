package com.king.caesar.gamma.remoting.netty;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;
import com.king.caesar.gamma.core.util.ConfigName;
import com.king.caesar.gamma.remoting.api.Connection;
import com.king.caesar.gamma.remoting.api.RemotingServer;
import com.king.caesar.gamma.remoting.netty.connection.ConnectionImpl;
import com.king.caesar.gamma.remoting.netty.handler.ServerInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * RPC的服务端的Netty实现
 * 
 * @author: Caesar
 * @date: 2017年5月17日 下午9:49:00
 */
public class NettyRemotingServer implements RemotingServer
{
    private static final Logger log = LoggerFactory.getLogger(NettyRemotingServer.class);
    
    private ServerBootstrap bootstrap;
    
    private EventLoopGroup bossGroup;
    
    private EventLoopGroup workerGroup;
    
    // <clientID,Connection> 连接到服务端的所有连接
    private ConcurrentHashMap<String, Connection> authedConnections = new ConcurrentHashMap<String, Connection>();
    
    // 客户端ID，客户端连接唯一对应一个ID
    private AtomicInteger clientId = new AtomicInteger(0);
    
    @Override
    public void init()
    {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(2);
        bootstrap = new ServerBootstrap();
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024)
            .option(ChannelOption.SO_REUSEADDR, true)
            .option(ChannelOption.SO_KEEPALIVE, false)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.SO_SNDBUF, ConfigName.SO_SNDBUF)
            .option(ChannelOption.SO_RCVBUF, ConfigName.SO_RCVBUF)
            .group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ServerInitializer(this));
    }
    
    @Override
    public boolean open()
    {
        ChannelFuture future = bootstrap.bind(ConfigName.IP, Integer.parseInt(ConfigName.PORT));
        future.awaitUninterruptibly();
        boolean binded = false;
        if (future.isDone())
        {
            if (future.isCancelled())
            {
                close(future);
            }
            else if (!future.isSuccess())
            {
                close(future);
                throw new GammaException(Result.Code.RPC_SERVER_OPENERROR,
                    String.format("Server bind failed.[ip=%s,port=%s]", ConfigName.IP, ConfigName.PORT),
                    future.cause());
            }
            else
            {
                binded = true;
            }
        }
        return binded;
    }
    
    private void close(ChannelFuture channelFuture)
    {
        Channel channel = channelFuture.channel();
        if (null != channel)
        {
            SocketAddress remote = channel.remoteAddress();
            final String remoteAddress = null == remote ? "" : remote.toString();
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
    
    public String addAuthedConnection(ConnectionImpl clientConnection)
    {
        clientId.incrementAndGet();
        String id = clientId.toString();
        authedConnections.putIfAbsent(id, clientConnection);
        return id;
    }
    
    public Connection getAuthedConnections(String clientId)
    {
        return authedConnections.get(clientId);
    }
    
    @Override
    public void close()
    {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
    
}
