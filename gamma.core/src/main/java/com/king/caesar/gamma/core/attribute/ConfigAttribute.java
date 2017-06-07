package com.king.caesar.gamma.core.attribute;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.core.io.Resource;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;
import com.king.caesar.gamma.core.util.ResourceLoader;
import com.king.caesar.gamma.rpc.api.pool.Executor;

/**
 * 配置中心
 * 
 * @author: Caesar
 * @date: 2017年5月14日 下午5:32:38
 * @param <T>
 */
public class ConfigAttribute<T> implements Attribute
{
    @SuppressWarnings("rawtypes")
    private static final ConcurrentMap<String, ConfigAttribute> names = new ConcurrentHashMap<>();
    
    public static final ConfigAttribute<Integer> MAXIDLE = valueOf(OPTION_MAXIDLE, 4);
    
    public static final ConfigAttribute<Integer> MINIDLE = valueOf(OPTION_MINIDLE, 4);
    
    public static final ConfigAttribute<Integer> MAXACTIVE = valueOf(OPTION_MAXACTIVE, 4);
    
    public static final ConfigAttribute<Long> MAXWAIT = valueOf(OPTION_MAXWAIT, 3000L);
    
    public static final ConfigAttribute<String> TIMEOUT = valueOf(OPTION_TIMEOUT, "5000");
    
    public static final ConfigAttribute<String> HA = valueOf(OPTION_HA, "failfast");
    
    public static final ConfigAttribute<String> BALANCER = valueOf(OPTION_BALANCER, "roundrobin");
    
    public static final ConfigAttribute<Executor> SERVER_DECODE_POOL = valueOf(OPTION_SERVER_DECODE_POOL, null);
    
    public static final ConfigAttribute<Executor> SERVER_ENCODE_POOL = valueOf(OPTION_SERVER_ENCODE_POOL, null);
    
    public static final ConfigAttribute<Executor> CLIENT_DECODE_POOL = valueOf(OPTION_CLIENT_DECODE_POOL, null);
    
    public static final ConfigAttribute<Executor> CLIENT_ENCODE_POOL = valueOf(OPTION_CLIENT_ENCODE_POOL, null);
    
    public static final ConfigAttribute<AttributeThreadLocal> ATTRIBUTES =
        valueOf(OPTION_ATTRIBUTES, new AttributeThreadLocal());
    
    private final String name;
    
    public T value;
    
    static class AttributeThreadLocal extends ThreadLocal<Map<String, Object>>
    {
        
        @Override
        protected Map<String, Object> initialValue()
        {
            return new HashMap<String, Object>();
        }
        
    }
    
    static
    {
        Properties properties = new Properties();
        Resource[] resources = ResourceLoader.loadResources("classpath:gamma.properties");
        if (null == resources || resources.length != 1)
        {
            throw new GammaException(Result.Code.INITCONFIG_ERROR, "Load config file error.");
        }
        Resource resource = resources[0];
        try
        {
            properties.load(resource.getInputStream());
        }
        catch (IOException e)
        {
            throw new GammaException(Result.Code.INITCONFIG_ERROR, "Load config properties error.");
        }
        if (null != properties.getProperty("channel.maxIdle"))
        {
            MAXIDLE.value = Integer.parseInt(properties.getProperty("channel.maxIdel"));
        }
        if (null != properties.getProperty("channel.minIdle"))
        {
            MINIDLE.value = Integer.parseInt(properties.getProperty("channel.minIdle"));
        }
        if (null != properties.getProperty("channel.maxActive"))
        {
            MAXACTIVE.value = Integer.parseInt(properties.getProperty("channel.maxActive"));
        }
        if (null != properties.getProperty("channel.maxWait"))
        {
            MAXWAIT.value = Long.parseLong(properties.getProperty("channel.maxWait"));
        }
        if (null != properties.get("timeout"))
        {
            TIMEOUT.value = properties.getProperty("timeout");
        }
        if (null != properties.get("haStrategy"))
        {
            HA.value = properties.getProperty("haStrategy");
        }
        if (null != properties.get("balancer"))
        {
            BALANCER.value = properties.getProperty("balancer");
        }
    }
    
    public ConfigAttribute(String name)
    {
        this.name = name;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> ConfigAttribute<T> valueOf(String name, T defaultValue)
    {
        ConfigAttribute<T> configOption = names.get(name);
        if (null == configOption)
        {
            configOption = new ConfigAttribute<T>(name);
            configOption.value = defaultValue;
            names.putIfAbsent(name, configOption);
        }
        return names.get(name);
    }
    
    @Override
    public void addLocalAttribute(String key, Object value)
    {
        ATTRIBUTES.value.get().put(key, value);
    }
    
    @Override
    public Object getLocalAttribute(String key)
    {
        return ATTRIBUTES.value.get().get(key);
    }
    
    @Override
    public Object removeLocalAttribute(String key)
    {
        return ATTRIBUTES.value.get().remove(key);
    }
    
    @Override
    public void clearLocalAttributes()
    {
        ATTRIBUTES.value.get().clear();
    }
    
    public String getName()
    {
        return name;
    }
}
