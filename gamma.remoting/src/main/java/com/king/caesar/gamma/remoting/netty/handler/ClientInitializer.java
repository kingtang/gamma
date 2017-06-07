package com.king.caesar.gamma.remoting.netty.handler;

import com.king.caesar.gamma.core.attribute.ConfigAttribute;
import com.king.caesar.gamma.remoting.api.Connection;
import com.king.caesar.gamma.remoting.api.RemotingClient;
import com.king.caesar.gamma.rpc.api.pool.Executor;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 客户端初始化，当TCP连接建立起来后，初始化pipeline上的处理handler。
 * 
 * @author: Caesar
 * @date: 2017年5月14日 下午2:08:24
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel>
{
    
    private AutoReconnectHandler reconnectHandler;
    
    private Connection connection;
    
    public ClientInitializer(RemotingClient client)
    {
        reconnectHandler = new AutoReconnectHandler(client);
        connection = client.getRegister();
    }
    
    @Override
    protected void initChannel(SocketChannel ch)
        throws Exception
    {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(reconnectHandler);
        pipeline.addLast("idleHandler", new IdleStateHandler(5, 4, 0));
        pipeline.addLast("heartbeatHandler", new HeartbeatHandler());
        pipeline.addLast("serviceHandler",
            new ClientServiceHandler((Executor)ConfigAttribute.CLIENT_DECODE_POOL.value, connection));
    }
    
    public AutoReconnectHandler getReconnectHandler()
    {
        return reconnectHandler;
    }
    
    public void setReconnectHandler(AutoReconnectHandler reconnectHandler)
    {
        this.reconnectHandler = reconnectHandler;
    }
    
}
