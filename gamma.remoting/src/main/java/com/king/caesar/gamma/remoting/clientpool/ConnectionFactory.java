package com.king.caesar.gamma.remoting.clientpool;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.remoting.api.Connection;
import com.king.caesar.gamma.remoting.api.RemotingClient;

/**
 * 用于创建netty的客户端连接
 * 
 * @author: Caesar
 * @date: 2017年5月14日 下午4:01:26
 */
public class ConnectionFactory extends BasePoolableObjectFactory
{
    private static final Logger log = LoggerFactory.getLogger(ConnectionFactory.class);
    
    private RemotingClient client;
    
    public ConnectionFactory(RemotingClient client)
    {
        this.client = client;
    }
    
    @Override
    public Object makeObject()
        throws Exception
    {
        log.debug("Create client connection.");
        if (null == client)
        {
            throw new NullPointerException("Client is null");
        }
        if (!client.isInited())
        {
            throw new IllegalStateException("Client is not initialized.");
        }
        return client.connect();
    }
    
    @Override
    public void destroyObject(Object obj)
        throws Exception
    {
        if (obj instanceof Connection)
        {
            Connection con = (Connection)obj;
            con.close();
        }
    }
    
    @Override
    public boolean validateObject(Object obj)
    {
        if (obj instanceof Connection)
        {
            Connection con = (Connection)obj;
            try
            {
                return con.isConnected();
            }
            catch (Exception e)
            {
                return false;
            }
        }
        return false;
    }
    
    @Override
    public void activateObject(Object obj)
        throws Exception
    {
        if (obj instanceof Connection)
        {
            Connection con = (Connection)obj;
            if (!con.isConnected())
            {
                con = (Connection)makeObject();
            }
        }
    }
    
    public RemotingClient getClient()
    {
        return client;
    }
    
    public void setClient(RemotingClient client)
    {
        this.client = client;
    }
}
