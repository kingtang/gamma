package com.king.caesar.gamma.remoting.api;

import java.io.Closeable;

import io.netty.channel.Channel;

/**
 * 客户端和服务端的连接抽象
 * 
 * @author: Caesar
 * @date: 2017年5月14日 下午4:13:49
 */
public interface Connection extends Closeable
{
    Channel getChannel();
    
    boolean isConnected();
    
    boolean sendResponse(Object msg);
    
    RemoteInfo getRemoteInfo();
}
