package com.king.caesar.gamma.remoting.api.handler;

import com.king.caesar.gamma.remoting.api.Connection;
import com.king.caesar.gamma.rpc.api.message.Message;

public interface MessageHandler
{
    void handleMessage(Message message, Connection connection);
}
