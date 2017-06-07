package com.king.caesar.gamma.rpc.api.codec;

import java.io.IOException;

import com.king.caesar.gamma.rpc.api.buffer.BufferWrapper;

public interface MsgDecoder<T>
{
    T doDecode(BufferWrapper buffer,T msg)
        throws IOException;
}
