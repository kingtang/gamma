package com.king.caesar.gamma.rpc.message;

import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.api.message.MessageHeader;
import com.king.caesar.gamma.rpc.api.message.MessageType;

public class MessageImpl implements Message
{
    private MessageType type;
    
    private MessageHeader header = new MessageHeader();
    
    private Object payload;
    
    private Long sessionId;
    
    @Override
    public MessageHeader getMessageHeader()
    {
        return header;
    }
    
    @Override
    public void setMessageHeader(MessageHeader msgHeader)
    {
        this.header = msgHeader;
    }
    
    @Override
    public Object getPayload()
    {
        return payload;
    }
    
    @Override
    public void setPayload(Object payload)
    {
        this.payload = payload;
    }
    
    @Override
    public MessageType getMessageType()
    {
        return type;
    }
    
    @Override
    public void setMessageType(MessageType type)
    {
        this.type = type;
    }
    
    @Override
    public Long getSessionId()
    {
        return sessionId;
    }
    
    @Override
    public void setSessionId(Long sessionId)
    {
        this.sessionId = sessionId;
    }
    
    @Override
    public String toString()
    {
        return "MessageImpl [type=" + type + ", header=" + (null == header ? "" : header) + ", payload="
            + (null == payload ? "" : payload) + "]";
    }
}
