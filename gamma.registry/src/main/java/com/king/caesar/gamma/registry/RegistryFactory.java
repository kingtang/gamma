package com.king.caesar.gamma.registry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.ApplicationContext;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;
import com.king.caesar.gamma.core.util.ContextHolder;

/**
 * 单例工厂类spring实现扩展
 * 
 * @author: Caesar
 * @date: 2017年5月3日 下午10:21:21
 */
public class RegistryFactory
{
    private Map<String, Registry> cache = new HashMap<String, Registry>();
    
    private static class RegistryFactoryHolder
    {
        private static final RegistryFactory INSTANCE = new RegistryFactory();
    }
    
    private RegistryFactory()
    {
    }
    
    public static final RegistryFactory getInstance()
    {
        return RegistryFactoryHolder.INSTANCE;
    }
    
    public Registry getRegistry(String type)
    {
        Registry registry = cache.get(type);
        if (null == registry)
        {
            synchronized (cache)
            {
                if (null == cache.get(type))
                {
                    ApplicationContext context = ContextHolder.getContext();
                    Map<String, Registry> registrys = context.getBeansOfType(Registry.class);
                    if (null == registrys || registrys.size() < 1)
                    {
                        throw new GammaException(Result.Code.NO_REGISTRY, "Can not find registry from spring context.");
                    }
                    Iterator<Entry<String, Registry>> iterator = registrys.entrySet().iterator();
                    while (iterator.hasNext())
                    {
                        Entry<String, Registry> iterRegistry = iterator.next();
                        cache.put(iterRegistry.getKey(), iterRegistry.getValue());
                    }
                    registry = cache.get(type);
                }
            }
        }
        return registry;
    }
}
