package com.king.caesar.gamma.demo.server;

import com.king.caesar.gamma.demo.api.HelloService;

public class HelloServiceImpl implements HelloService
{
    
    @Override
    public String sayHello(String request)
    {
        System.out.println("request hello...");
        return request;
    }
    
}
