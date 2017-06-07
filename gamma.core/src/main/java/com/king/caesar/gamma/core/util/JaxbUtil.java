package com.king.caesar.gamma.core.util;

import java.io.InputStream;
import java.io.StringReader;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * xml解析工具
 * 
 * @author: Caesar
 * @date: 2017年5月21日 下午1:43:00
 */
public class JaxbUtil
{
    private static final Logger log = LoggerFactory.getLogger(JaxbUtil.class);
    
    private static ConcurrentHashMap<Class<?>, JAXBContext> contexts = new ConcurrentHashMap<>();
    
    @SuppressWarnings("unchecked")
    public static <T> T toBean(Class<T> clazz, String data)
    {
        JAXBContext jaxbContext = getJaxbContext(clazz);
        if (null != jaxbContext)
        {
            try
            {
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                return (T)unmarshaller.unmarshal(new StringReader(data));
            }
            catch (JAXBException e)
            {
                log.error("Create Unmarshaller error.", e);
                return null;
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T toBean(Class<T> clazz, InputStream is)
    {
        JAXBContext jaxbContext = getJaxbContext(clazz);
        if (null != jaxbContext)
        {
            try
            {
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                return (T)unmarshaller.unmarshal(is);
            }
            catch (JAXBException e)
            {
                log.error("Create Unmarshaller error.", e);
                return null;
            }
        }
        return null;
    }
    
    private static <T> JAXBContext getJaxbContext(Class<T> clazz)
    {
        JAXBContext jaxbContext = contexts.get(clazz);
        if (null == jaxbContext)
        {
            synchronized (clazz)
            {
                if (null == contexts.get(clazz))
                {
                    try
                    {
                        JAXBContext instance = JAXBContext.newInstance(clazz);
                        contexts.put(clazz, instance);
                    }
                    catch (JAXBException e)
                    {
                        log.error("Create JAXBContext error.", e);
                        contexts.put(clazz, null);
                    }
                }
                jaxbContext = contexts.get(clazz);
            }
        }
        return jaxbContext;
    }
}
