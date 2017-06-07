package com.king.caesar.gamma.demo.client;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.king.caesar.gamma.Bootstrap;
import com.king.caesar.gamma.core.util.ContextHolder;
import com.king.caesar.gamma.rpc.invoker.ServiceInvoker;

public class Startup
{
    public static void main(String[] args)
    {
        System.err.println("Begin to start...");
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.init();
        bootstrap.start();
        ApplicationContext context = ContextHolder.getContext();
        Map<String, ServiceInvoker> beansOfType = context.getBeansOfType(ServiceInvoker.class);
        Object bean = context.getBean("hello");
        
        Remoting client = new Remoting();
        client.sendMsg();
        
        try
        {
            Thread.sleep(500000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
