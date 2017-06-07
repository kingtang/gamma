package com.king.caesar.gamma.rpc.api.codec;

import com.king.caesar.gamma.rpc.api.buffer.BufferWrapper;

public interface MsgEncoder<E>
{
    BufferWrapper doEncode(E msg);
}
