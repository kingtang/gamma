package com.king.caesar.gamma.demo.server;

import com.king.caesar.gamma.Bootstrap;

public class ServerStartup
{
    public static void main(String[] args)
    {
        System.err.println("Begin to start server...");
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.init();
        bootstrap.start();
    }
}
