package com.king.caesar.gamma.rpc.codec;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.king.caesar.gamma.rpc.codec.buffer.NettyBufferWrapper;
import com.king.caesar.gamma.rpc.codec.serializer.ProtostuffSerializer;

public class NettyBufferWrapperTest
{
    ProtostuffSerializer serializer = new ProtostuffSerializer();
    
    NettyBufferWrapper buffer = NettyBufferWrapper.allocate();
    
    @Test
    public void testStringObject()
    {
        String data = "a";
        buffer.writeObjectDirect(data, serializer);
        Object request = buffer.readObjectDirect(serializer);
        assertEquals("a",request);
    }
    
    @Test
    public void testWriteInt()
    {
        buffer.writeInt(3);
        int readInt = buffer.readInt();
        assertEquals(3, readInt);
    }
    
    @Test
    public void testWriteString()
    {
        buffer.writeString("a");
        String data = buffer.readString();
        assertEquals("a", data);
    }
    
    @Test
    public void testObject()
    {
        Student student = new Student();
        student.setName("huang");
        buffer.writeObjectDirect(student, serializer);
        Student request = (Student)buffer.readObjectDirect(serializer);
        assertEquals("huang", request.getName());
    }
    
    @Test
    public void testMap()
    {
        Map<String,Integer> map = new HashMap<String,Integer>();
        map.put("a", 1);
        map.put("b", 2);
        buffer.writeMap(map, Integer.class, serializer);

        Map<String, Integer> request = buffer.readMap(Integer.class, serializer);
        assertEquals(2, request.size());
        assertEquals(new Integer(1),request.get("a"));
        assertEquals(new Integer(2), request.get("b"));
    }
}
