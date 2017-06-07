package com.king.caesar.gamma.remoting;

import java.util.HashMap;
import java.util.Map;

import com.king.caesar.gamma.core.util.ContextHolder;
import com.king.caesar.gamma.remoting.api.Connection;
import com.king.caesar.gamma.remoting.api.handler.MessageHandler;
import com.king.caesar.gamma.rpc.api.context.Connector;
import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.api.message.MessageType;
import com.king.caesar.gamma.rpc.handler.BusinessRequestMsgHandler;
import com.king.caesar.gamma.rpc.handler.BusinessResponseMsgHandler;
import com.king.caesar.gamma.rpc.handler.HeartbeatMessageHandler;

/**
 * 消息分发器，外部消息进入业务框架的统一入口，消息的分类由分发器负责。
 * 
 * @author: Caesar
 * @date: 2017年5月17日 下午9:02:29
 */
public class Dispatcher
{
    private static final Dispatcher INSTANCE = new Dispatcher();
    
    private Map<MessageType, MessageHandler> msgHandlers = new HashMap<MessageType, MessageHandler>();
    
    private Dispatcher()
    {
        BusinessRequestMsgHandler msgHandler = new BusinessRequestMsgHandler();
        msgHandler.setConnector((Connector)ContextHolder.getContext().getBean("gammaConnector"));
        msgHandlers.put(MessageType.REQUEST, msgHandler);
        
        BusinessResponseMsgHandler responseMsgHandler = new BusinessResponseMsgHandler();
        msgHandlers.put(MessageType.RESPONSE, responseMsgHandler);
        
        msgHandlers.put(MessageType.HEARTBEAT_REQ, new HeartbeatMessageHandler());
        msgHandlers.put(MessageType.HEARTBEAT_RSP, new HeartbeatMessageHandler());
    }
    
    public static Dispatcher getInstance()
    {
        return INSTANCE;
    }
    
    public void dispatch(Message message, Connection connection)
    {
        MessageHandler messageHandler = msgHandlers.get(message.getMessageType());
        messageHandler.handleMessage(message, connection);
    }
}
