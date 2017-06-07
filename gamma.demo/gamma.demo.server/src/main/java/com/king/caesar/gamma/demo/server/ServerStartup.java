package com.king.caesar.gamma.demo.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.king.caesar.gamma.Bootstrap;
import com.king.caesar.gamma.core.util.ContextHolder;

public class ServerStartup
{
    public static void main(String[] args)
    {
        System.err.println("Begin to start server...");
        /*String[] classpath =
            new String[] {"classpath*:/META-INF/spring/*-exporter.xml", "classpath*:/META-INF/spring/gamma*.xml"};
        ApplicationContext context = new ClassPathXmlApplicationContext(classpath);
        ContextHolder.setContext(context);*/
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.init();
        bootstrap.start();
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
