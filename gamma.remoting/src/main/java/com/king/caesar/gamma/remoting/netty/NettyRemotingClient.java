package com.king.caesar.gamma.remoting.netty;

import static com.king.caesar.gamma.core.attribute.ConfigAttribute.MAXACTIVE;
import static com.king.caesar.gamma.core.attribute.ConfigAttribute.MAXIDLE;
import static com.king.caesar.gamma.core.attribute.ConfigAttribute.MAXWAIT;
import static com.king.caesar.gamma.core.attribute.ConfigAttribute.MINIDLE;

import java.net.InetSocketAddress;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;
import com.king.caesar.gamma.core.util.ConfigName;
import com.king.caesar.gamma.remoting.api.ClientOption;
import com.king.caesar.gamma.remoting.api.Connection;
import com.king.caesar.gamma.remoting.api.RemotingClient;
import com.king.caesar.gamma.remoting.clientpool.ConnectionFactory;
import com.king.caesar.gamma.remoting.netty.connection.ClientConnection;
import com.king.caesar.gamma.remoting.netty.connection.ConnectionImpl;
import com.king.caesar.gamma.remoting.netty.handler.ClientInitializer;
import com.king.caesar.gamma.remoting.util.ResourceUtil;
import com.king.caesar.gamma.rpc.api.buffer.BufferWrapper;
import com.king.caesar.gamma.rpc.util.CodecUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 客户端连接，一个服务端一个实例，但是内部可以指定和对端服务器建立多少条链路。<b>
 * <p>
 * 客户端多链路借助了commons
 * pool实现，每次请求时从客户端获取连接，异步发送完毕后回收客户端连接。这里的实现稍微有点问题，channel是线程安全的，这种做法限制了并发度。
 * 但是考虑到异步发送效率很高，这里的channel并不是瓶颈，所以当前这种做法也无可厚非。
 * 
 * @author: Caesar
 * @date: 2017年5月30日 上午11:19:52
 */
public class NettyRemotingClient implements RemotingClient
{
    private static final Logger log = LoggerFactory.getLogger(NettyRemotingClient.class);
    
    private Bootstrap bootstrap;
    
    private boolean inited;
    
    private ConnectionFactory connectionFactory;
    
    // 客户端连接由pool缓存
    private GenericObjectPool connectionPool;
    
    private ClientConnection connection;
    
    private ClientOption option;
    
    public NettyRemotingClient(ClientOption option)
    {
        this.option = option;
        connection = new ClientConnection(this);
    }
    
    @Override
    public void init()
    {
        EventLoopGroup workerGroup = new NioEventLoopGroup(3);
        bootstrap = new Bootstrap();
        options();
        bootstrap.group(workerGroup).channel(NioSocketChannel.class).handler(new ClientInitializer(this));
        connectionFactory = new ConnectionFactory(this);
        Config config = new Config();
        config.maxActive = MAXACTIVE.value;
        config.maxIdle = MAXIDLE.value;
        config.minIdle = MINIDLE.value;
        config.maxWait = MAXWAIT.value;
        connectionPool = new GenericObjectPool(connectionFactory, config);
        
        inited = true;
    }
    
    @Override
    public Connection connect()
    {
        String remoteIp = option.getRemoteIp();
        String remotePort = option.getPort();
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(remoteIp, Integer.parseInt(remotePort)));
        channelFuture.awaitUninterruptibly();
        if (channelFuture.isDone())
        {
            if (channelFuture.isCancelled())
            {
                ResourceUtil.close(channelFuture);
            }
            else if (!channelFuture.isSuccess())
            {
                ResourceUtil.close(channelFuture);
                throw new GammaException(Result.Code.RPC_CLIENT_CONNECTERROR,
                    String.format("Connect to %s:%s failed", remoteIp, remotePort), channelFuture.cause());
            }
        }
        Channel channel = channelFuture.channel();
        Connection connection = new ConnectionImpl(channel);
        return connection;
    }
    
    private void options()
    {
        if (null != ConfigName.CONNECT_TIMEOUT_MILLIS)
        {
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ConfigName.CONNECT_TIMEOUT_MILLIS);
        }
    }
    
    public Bootstrap getBootstrap()
    {
        return bootstrap;
    }
    
    public void setBootstrap(Bootstrap bootstrap)
    {
        this.bootstrap = bootstrap;
    }
    
    @Override
    public ChannelFuture sendOneway(final Object message)
    {
        BufferWrapper encodedInfo = CodecUtil.encode(message);
        Connection connection = null;
        try
        {
            connection = (Connection)connectionPool.borrowObject();
            ChannelFuture writeAndFlush = connection.getChannel().writeAndFlush(encodedInfo);
            connectionPool.returnObject(connection);
            boolean result = writeAndFlush.awaitUninterruptibly(3000);
            if (!(result || writeAndFlush.isSuccess()))
            {
                throw new GammaException(Result.Code.MSG_SEND_ERROR, "Send msg to IO-Thread failed.");
            }
            writeAndFlush.addListener(new GenericFutureListener<Future<? super Void>>()
            {
                @Override
                public void operationComplete(Future<? super Void> future)
                    throws Exception
                {
                    if (null != future.cause())
                    {
                        log.error("Send msg error!", future.cause());
                    }
                }
            });
            return writeAndFlush;
        }
        catch (Exception e)
        {
            try
            {
                connectionPool.invalidateObject(connection);
            }
            catch (Exception e1)
            {
                log.error("Invalidate object error.", e1);
            }
            throw new GammaException(Result.Code.MSG_SEND_ERROR, "Send one way msg failed.", e);
        }
        
    }
    
    @Override
    public boolean isInited()
    {
        return inited;
    }
    
    public ConnectionFactory getConnectionFactory()
    {
        return connectionFactory;
    }
    
    public void setConnectionFactory(ConnectionFactory connectionFactory)
    {
        this.connectionFactory = connectionFactory;
    }
    
    @Override
    public ClientOption getClientOption()
    {
        return option;
    }
    
    public ClientConnection getRegister()
    {
        return connection;
    }
}
