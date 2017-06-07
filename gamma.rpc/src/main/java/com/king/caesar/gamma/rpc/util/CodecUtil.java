package com.king.caesar.gamma.rpc.util;

import java.io.IOException;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;
import com.king.caesar.gamma.rpc.api.buffer.BufferWrapper;
import com.king.caesar.gamma.rpc.api.codec.Codec;
import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.api.message.MessageType;
import com.king.caesar.gamma.rpc.codec.MessageCodecFactory;
import com.king.caesar.gamma.rpc.message.MessageImpl;

public abstract class CodecUtil
{
    public static Object decode(BufferWrapper buffer)
        throws IOException
    {
        // 读取magic数
        short magic = buffer.readShort();
        if (magic != Codec.MAGIC)
        {
            throw new GammaException(Result.Code.MESSAGE_ILLEGAL,
                String.format(Result.CodeDesc.MESSAGE_ILLEGAL, "magic"));
        }
        MessageImpl msg = new MessageImpl();
        // 读取消息类型
        byte type = buffer.readByte();
        msg.setMessageType(MessageType.valueOf(type));
        
        // 读取版本号
        byte version = buffer.readByte();
        if (version != Codec.VERSION)
        {
            throw new GammaException(Result.Code.VERSION_ILLEGAL,
                String.format(Result.CodeDesc.MESSAGE_ILLEGAL, "version"));
        }
        
        // 根据消息类型获取对应的codec
        Codec<Message> codec = MessageCodecFactory.DEFAULT.getCodec(MessageType.valueOf(type));
        return codec.doDecode(buffer, msg);
        
    }
    
    public static BufferWrapper encode(Object msg)
    {
        if (msg instanceof MessageImpl)
        {
            MessageImpl msgImpl = (MessageImpl)msg;
            Codec<Message> codec = MessageCodecFactory.DEFAULT.getCodec(msgImpl.getMessageType());
            return codec.doEncode(msgImpl);
        }
        return null;
    }
}
