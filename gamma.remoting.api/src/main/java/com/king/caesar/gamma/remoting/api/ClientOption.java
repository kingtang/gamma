package com.king.caesar.gamma.remoting.api;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelOption;

public class ClientOption
{
    private final Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
    
    private String remoteIp;
    
    private String port;
    
    public String getRemoteIp()
    {
        return remoteIp;
    }
    
    public void setRemoteIp(String remoteIp)
    {
        this.remoteIp = remoteIp;
    }
    
    public String getPort()
    {
        return port;
    }
    
    public void setPort(String port)
    {
        this.port = port;
    }
    
    public Map<ChannelOption<?>, Object> getOptions()
    {
        return options;
    }
}
