package com.king.caesar.gamma.test;

import java.util.HashMap;
import java.util.Map;               

import com.king.caesar.gamma.rpc.api.message.AttachmentObject;
import com.king.caesar.gamma.rpc.api.message.AttachmentType;

import io.protostuff.GraphIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.StringMapSchema;
import io.protostuff.runtime.RuntimeSchema;

public class MapSerializerTest
{
    public static void main(String[] args)
    {
        RuntimeSchema.getSchema(Address.class);
        HashMap<String,AttachmentObject> map = new HashMap<String,AttachmentObject>();
        AttachmentObject a1 = new AttachmentObject(AttachmentType.REMOTE_IMPLICIT, "a");
        Address aaaa = new Address();
        Map<String,AttachmentObject> inner = new HashMap<String,AttachmentObject>();
        inner.put("inner", new AttachmentObject(AttachmentType.LOCAL_IMPLICIT, "I am inner"));
        aaaa.setMap(inner);
        a1.setValue(aaaa);
        map.put("name", a1);
        byte[] byteArray = GraphIOUtil.toByteArray(map, new StringMapSchema<>(RuntimeSchema.getSchema(AttachmentObject.class)), LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        System.out.println(byteArray.length);
        /*List<AttachmentObject> list = new ArrayList<AttachmentObject>();
        GraphIOUtil.toByteArray(list, RuntimeSchema.getSchema(List.class), LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));*/
        
        HashMap<String,AttachmentObject> message = new HashMap<String,AttachmentObject>();
        GraphIOUtil.mergeFrom(byteArray, message, new StringMapSchema<>(RuntimeSchema.getSchema(AttachmentObject.class)));
        System.out.println(message.get("name").getValue());
        
        Address address = new Address();
        HashMap<String,AttachmentObject> data = new HashMap<String,AttachmentObject>();
        data.put("a", a1);
        address.setMap(data);
        byte[] byteArray2 = GraphIOUtil.toByteArray(address, RuntimeSchema.getSchema(Address.class), LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        System.out.println(byteArray2.length);
        
        Address a2 = new Address();
        GraphIOUtil.mergeFrom(byteArray2, a2, RuntimeSchema.getSchema(Address.class));
        System.out.println(a2.getMap().size());
        System.out.println(a2.getMap().get("a"));
        
        Map<String,String> stringMap = new HashMap<String,String>();
        stringMap.put("a", "1");
        byte[] byteArray3 = GraphIOUtil.toByteArray(stringMap, new StringMapSchema<>(RuntimeSchema.getSchema(String.class)), LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        System.out.println(byteArray3.length);
        
        Map<String,String> newStringMap = new HashMap<String,String>();
        GraphIOUtil.mergeFrom(byteArray3, newStringMap, new StringMapSchema<>(RuntimeSchema.getSchema(String.class)));
        System.out.println(newStringMap.get("a"));
        
        System.out.println(map.getClass());
        //==================
        /*Map<String,Object> objectMap = new HashMap<String,Object>();
        objectMap.put("a", "1");
        objectMap.put("b",1);
        byte[] byteArray4 = GraphIOUtil.toByteArray(objectMap, new StringMapSchema<>(RuntimeSchema.getSchema(Object.class)), LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        
        Map<String,Object> objectMessage = new HashMap<String,Object>();
        GraphIOUtil.mergeFrom(byteArray4, objectMessage, new StringMapSchema<>(RuntimeSchema.getSchema(Object.class)));
        System.out.println(objectMessage);*/
        Map<String,String> objectMap = new HashMap<String,String>();
        objectMap.put("a", "1");
        byte[] byteArray4 = GraphIOUtil.toByteArray(objectMap, new StringMapSchema<>(RuntimeSchema.getSchema(String.class)), LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        
        Map<String,String> objectMessage = new HashMap<String,String>();
        GraphIOUtil.mergeFrom(byteArray4, objectMessage, new StringMapSchema<>(RuntimeSchema.getSchema(String.class)));
        System.out.println(objectMessage);
        
    }
    
    public static class Address
    {
        private Map<String,AttachmentObject> map;

        public Map<String, AttachmentObject> getMap()
        {
            return map;
        }

        public void setMap(Map<String, AttachmentObject> map)
        {
            this.map = map;
        }

        @Override
        public String toString()
        {
            return "Address [map=" + map + "]";
        }
        
        
    }
}
