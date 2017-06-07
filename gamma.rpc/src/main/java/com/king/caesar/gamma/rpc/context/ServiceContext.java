package com.king.caesar.gamma.rpc.context;

import java.util.HashMap;
import java.util.Map;

import com.king.caesar.gamma.rpc.api.context.Connector;
import com.king.caesar.gamma.rpc.api.context.Context;
import com.king.caesar.gamma.rpc.api.message.Message;

public class ServiceContext implements Context
{
    private Message request;
    
    private Message response;
    
    private Connector srcConnector;
    
    private Connector destConnector;
    
    private int timeOutMs;
    
    private Map<String, Object> attributes = new HashMap<String, Object>();
    
    @Override
    public Message getRequest()
    {
        return request;
    }
    
    @Override
    public void setRequest(Message request)
    {
        this.request = request;
    }
    
    @Override
    public Message getResponse()
    {
        return response;
    }
    
    @Override
    public void setResponse(Message response)
    {
        this.response = response;
    }
    
    @Override
    public Connector getSrcConnector()
    {
        return srcConnector;
    }
    
    @Override
    public Connector getDestConnector()
    {
        return destConnector;
    }
    
    @Override
    public void setSrcConnector(Connector srcConnector)
    {
        this.srcConnector = srcConnector;
    }
    
    @Override
    public void setDestConnector(Connector destConnector)
    {
        this.destConnector = destConnector;
    }
    
    public int getTimeOutMs()
    {
        return timeOutMs;
    }

    public void setTimeOutMs(int timeOutMs)
    {
        this.timeOutMs = timeOutMs;
    }
    
    @Override
    public Object getAttribute(String key)
    {
        return attributes.get(key);
    }
    
    @Override
    public void setAttribute(String key, Object value)
    {
        attributes.put(key, value);
    }
}
