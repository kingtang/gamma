package com.king.caesar.gamma.rpc.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.remoting.api.Connection;
import com.king.caesar.gamma.remoting.api.handler.MessageHandler;
import com.king.caesar.gamma.rpc.api.message.Message;

public class HeartbeatMessageHandler implements MessageHandler
{
    private static final Logger log = LoggerFactory.getLogger(HeartbeatMessageHandler.class);
    
    @Override
    public void handleMessage(Message message, Connection connection)
    {
        log.debug("Received a heartbeat.", message);
    }
}
