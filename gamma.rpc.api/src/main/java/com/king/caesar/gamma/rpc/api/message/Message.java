package com.king.caesar.gamma.rpc.api.message;

public interface Message
{
    MessageType getMessageType();
    
    void setMessageType(MessageType type);
    
    MessageHeader getMessageHeader();
    
    void setMessageHeader(MessageHeader msgHeader);
    
    Object getPayload();
    
    void setPayload(Object payload);
    
    Long getSessionId();
    
    void setSessionId(Long sessionId);
}
