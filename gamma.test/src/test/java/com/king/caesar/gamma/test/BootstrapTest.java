package com.king.caesar.gamma.test;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.king.caesar.gamma.Bootstrap;
import com.king.caesar.gamma.core.attribute.Attribute;
import com.king.caesar.gamma.core.attribute.ConfigAttribute;
import com.king.caesar.gamma.remoting.netty.NettyRemotingServer;
import com.king.caesar.gamma.rpc.pool.ServiceExecutor;

public class BootstrapTest
{
    @Test
    public void testBootstrap()
    {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.init();
        bootstrap.start();
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
