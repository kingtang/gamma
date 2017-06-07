package com.king.caesar.gamma.rpc.api.context;

import com.king.caesar.gamma.rpc.api.message.Message;

public interface MessageFactory
{
    Message createRequest();
    
    Message createResponse(Message request);
}
