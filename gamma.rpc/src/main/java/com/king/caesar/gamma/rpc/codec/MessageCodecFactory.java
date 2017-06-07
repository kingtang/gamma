package com.king.caesar.gamma.rpc.codec;

import com.king.caesar.gamma.rpc.api.codec.Codec;
import com.king.caesar.gamma.rpc.api.codec.CodecFactory;
import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.api.message.MessageType;

public final class MessageCodecFactory implements CodecFactory<Message>
{
    public static CodecFactory<Message> DEFAULT = new MessageCodecFactory();
    
    @Override
    public Codec<Message> getCodec(MessageType msgType)
    {
        switch (msgType)
        {
            case REQUEST:
                return GammaCodec.getInstance();
            case RESPONSE:
                return GammaCodec.getInstance();
            case HEARTBEAT_REQ:
                return HeartbeatCodec.getInstance();
            case HEARTBEAT_RSP:
                return HeartbeatCodec.getInstance();
            default:
                return HeartbeatCodec.getInstance();
        }
    }
}
