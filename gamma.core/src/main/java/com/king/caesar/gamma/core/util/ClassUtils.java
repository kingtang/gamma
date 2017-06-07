package com.king.caesar.gamma.core.util;

import java.util.HashMap;
import java.util.Map;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;

/**
 * 工具类，用于常用的class操作
 * 
 * @author: Caesar
 * @date: 2017年4月20日 下午10:45:11
 */
public abstract class ClassUtils
{
    private static volatile Map<String, Class<?>> classCache = new HashMap<String, Class<?>>();
    
    private static final Object LOCK = new Object();
    /**
     * 加载类
     * 
     * @param name 类名
     * @param classLoader 可为空
     * @return
     */
    public static Class<?> forName(String name, ClassLoader classLoader)
    {
        Assert.notNull(name, "class name");
        Class<?> clazz = classCache.get(name);
        
        if (null == classCache.get(name))
        {
            synchronized (LOCK)
            {
                if (null == classCache.get(name))
                {
                    ClassLoader loader = null;
                    if (null == classLoader)
                    {
                        loader = getDefaultClassLoader();
                    }
                    try
                    {
                        clazz = (null == loader ? Class.forName(name) : loader.loadClass(name));
                        classCache.put(name, clazz);
                    }
                    catch (ClassNotFoundException e)
                    {
                        throw new GammaException(Result.Code.FRAMEWORK_INITERROR,
                            "Class not found and class name is " + name, e);
                    }
                }
            }
        }
        return clazz;
    }
    
    public static ClassLoader getDefaultClassLoader()
    {
        ClassLoader cl = null;
        try
        {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex)
        {
        }
        if (cl == null)
        {
            cl = ClassUtils.class.getClassLoader();
            if (cl == null)
            {
                try
                {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex)
                {
                }
            }
        }
        return cl;
    }
}
