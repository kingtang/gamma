package com.king.caesar.gamma.rpc.api.codec;

import com.king.caesar.gamma.rpc.api.message.MessageType;

public interface CodecFactory<T>
{
    Codec<T> getCodec(MessageType msgType);
}
