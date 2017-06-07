package com.king.caesar.gamma.rpc.codec;

import java.io.IOException;

import com.king.caesar.gamma.rpc.api.buffer.BufferWrapper;
import com.king.caesar.gamma.rpc.api.codec.Codec;
import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.codec.buffer.NettyBufferWrapper;
import com.king.caesar.gamma.rpc.message.MessageImpl;

public class HeartbeatCodec implements Codec<Message>
{
    private static final Codec<Message> INSTANCE = new HeartbeatCodec();
    
    private HeartbeatCodec()
    {
    }
    
    @Override
    public Message doDecode(BufferWrapper buffer, Message msg)
        throws IOException
    {
        return msg;
    }
    
    @Override
    public BufferWrapper doEncode(Message msg)
    {
        MessageImpl msgImpl = (MessageImpl)msg;
        // 开辟256B内存空间，引用数自动+1不需要显示retain
        BufferWrapper buffer = NettyBufferWrapper.allocate(6);
        // 2字节魔数
        buffer.writeShort(MAGIC);
        buffer.writeByte(msgImpl.getMessageType().originalValue());
        buffer.writeByte(VERSION);
        //心跳消息body为0
        buffer.writeInt(0);
        return buffer;
    }
    
    public static Codec<Message> getInstance()
    {
        return INSTANCE;
    }
    
}
