package com.king.caesar.gamma.rpc.handler;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;
import com.king.caesar.gamma.remoting.api.Connection;
import com.king.caesar.gamma.remoting.api.handler.MessageHandler;
import com.king.caesar.gamma.rpc.api.context.Connector;
import com.king.caesar.gamma.rpc.api.context.Context;
import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.context.ServiceContext;

/**
 * 业务请求处理
 * 
 * @author: Caesar
 * @date: 2017年5月17日 下午8:57:43
 */
public class BusinessRequestMsgHandler implements MessageHandler
{
    private Connector connector;
    
    @Override
    public void handleMessage(Message message, Connection connection)
    {
        Context context = new ServiceContext();
        context.setRequest(message);
        Object response = connector.onReceive(context);
        // 回写响应
        if (connection.isConnected())
        {
            connection.sendResponse(response);
        }
        else
        {
            throw new GammaException(Result.Code.CONNECTION_STATUS_ILLEGAL,
                String.format("Connection is disconnected and remote address is %s", connection.getRemoteInfo()));
        }
    }
    
    public Connector getConnector()
    {
        return connector;
    }
    
    public void setConnector(Connector connector)
    {
        this.connector = connector;
    }
    
}
