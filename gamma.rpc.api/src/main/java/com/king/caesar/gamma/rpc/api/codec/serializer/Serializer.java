package com.king.caesar.gamma.rpc.api.codec.serializer;

import java.util.Map;

/**
 * Request body or Response body的序列化器
 * 
 * @author: Caesar
 * @date: 2017年5月28日 下午4:21:32
 */
public interface Serializer
{
    byte getType();
    
    byte[] serialize(Object data);
    
    <T> T deserialize(byte[] bytes, Class<T> clazz);
    
    // 只支持String作为key的场景
    <V extends Object> byte[] serializeMap(Map<String, V> map, Class<V> valueClazz);
    
    <V extends Object> Map<String, V> deserializeMap(byte[] bytes, Class<V> valueClazz);
}
