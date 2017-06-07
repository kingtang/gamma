package com.king.caesar.gamma.rpc.codec;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.king.caesar.gamma.rpc.codec.serializer.ProtostuffSerializer;

public class ProtostuffSerializerTest
{
    /**
     * 序列化Map
     */
    @Test
    public void testMap()
    {
        ProtostuffSerializer serializer = new ProtostuffSerializer();
        Map<String, String> data = new HashMap<String, String>();
        data.put("a", "1");
        byte[] bytes = serializer.serializeMap(data, String.class);
        
        Map<String, String> deserializeMap = serializer.deserializeMap(bytes, String.class);
        Assert.assertEquals(1, deserializeMap.size());
        Assert.assertEquals("a", deserializeMap.keySet().iterator().next());
        Assert.assertEquals("1", deserializeMap.get("a"));
    }
    
}
