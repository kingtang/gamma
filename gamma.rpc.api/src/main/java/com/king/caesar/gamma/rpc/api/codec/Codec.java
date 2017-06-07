package com.king.caesar.gamma.rpc.api.codec;

public interface Codec<T> extends MsgDecoder<T>, MsgEncoder<T>
{
    short MAGIC = 0x0101;
    
    byte VERSION = (byte)0xaa;
}
