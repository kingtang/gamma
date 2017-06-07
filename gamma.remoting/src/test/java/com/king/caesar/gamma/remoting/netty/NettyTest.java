package com.king.caesar.gamma.remoting.netty;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.king.caesar.gamma.Bootstrap;
import com.king.caesar.gamma.core.attribute.Attribute;
import com.king.caesar.gamma.core.attribute.ConfigAttribute;
import com.king.caesar.gamma.remoting.api.ClientOption;
import com.king.caesar.gamma.rpc.api.message.MessageType;
import com.king.caesar.gamma.rpc.message.MessageImpl;
import com.king.caesar.gamma.rpc.pool.ServiceExecutor;

public class NettyTest
{
    
    @Test
    public void testClient()
    {
        ClientOption option = new ClientOption();
        NettyRemotingClient client = new NettyRemotingClient(option);
        client.init();
        try
        {
            /*boolean successed = client.connect();
            if(successed)
            {*/
                System.out.println("connect successed.");
                MessageImpl msg = new MessageImpl();
                msg.setMessageType(MessageType.REQUEST);
                msg.setPayload("a");
                client.sendOneway(msg);
                
            //}
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        try
        {
            Thread.sleep(600000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    
    @Test
    public void testServer()
    {
        NettyRemotingServer server = new NettyRemotingServer();
        server.init();
        server.open();
        try
        {
            Thread.sleep(600000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testServer2()
    {
        //初始化线程池
        /*ExecutorService executor = Executors.newFixedThreadPool(10);
        ServiceExecutor service = new ServiceExecutor();
        service.setDefaultExecutor(executor);
        ConfigAttribute.attribute.put(Attribute.SERVER_DECODE_POOL, service);*/
        
        //启动服务端
        NettyRemotingServer server = new NettyRemotingServer();
        server.init();
        server.open();
        try
        {
            System.out.println("NettyServer started.");
            Thread.sleep(600000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    
}
