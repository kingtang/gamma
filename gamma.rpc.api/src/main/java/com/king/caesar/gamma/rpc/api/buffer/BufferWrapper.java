package com.king.caesar.gamma.rpc.api.buffer;

import java.nio.charset.Charset;
import java.util.Map;

import com.king.caesar.gamma.rpc.api.codec.serializer.Serializer;

import io.netty.buffer.ByteBuf;

/**
 * 扩展Netty的ByteBuf
 * 
 * @author: Caesar
 * @date: 2017年5月29日 下午6:31:41
 */
public abstract class BufferWrapper extends ByteBuf
{
    public abstract ByteBuf writeString(String data);
    
    public abstract ByteBuf writeString(String data, Charset charset);
    
    public abstract ByteBuf writeObject(Object data, Serializer serializer);
    
    public abstract ByteBuf writeObjects(Object[] data, Serializer serializer);
    
    public abstract ByteBuf writeObjectDirect(Object data, Serializer serializer);
    
    public abstract ByteBuf writeObjectsDirect(Object[] data, Serializer serializer);
    
    // 单纯的Map参数无法获取到泛型类型，因此需要传入
    public abstract <V extends Object> ByteBuf writeMap(Map<String, V> map, Class<V> valueClazz, Serializer serializer);
    
    public abstract String readString();
    
    public abstract String readString(Charset charset);
    
    public abstract Object readObject(Serializer serializer);
    
    public abstract Object[] readObjects(Serializer serializer);
    
    public abstract Object readObjectDirect(Serializer serializer);
    
    public abstract Object[] readObjectsDirect(Serializer serializer);
    
    public abstract <V extends Object> Map<String, V> readMap(Class<V> valueClazz, Serializer serializer);
    
}
