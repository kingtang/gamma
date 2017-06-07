package com.king.caesar.gamma.rpc.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.king.caesar.gamma.rpc.api.codec.serializer.Serializer;
/**
 * 序列化工厂
 * 
 * @author: Caesar
 * @date:   2017年5月29日 下午5:20:48
 */
public class SerializerFactory implements ApplicationContextAware
{
    private ApplicationContext context;
    
    private Map<Byte, Serializer> serializer = new HashMap<Byte, Serializer>();
    
    public Serializer getSerializer(byte type)
    {
        return serializer.get(type);
    }
    
    public void init()
    {
        Map<String, Serializer> serializers = context.getBeansOfType(Serializer.class);
        Iterator<Entry<String, Serializer>> iterator = serializers.entrySet().iterator();
        while (iterator.hasNext())
        {
            Entry<String, Serializer> next = iterator.next();
            serializer.put(next.getValue().getType(), next.getValue());
        }
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException
    {
        this.context = applicationContext;
    }
}
