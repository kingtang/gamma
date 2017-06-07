package com.king.caesar.gamma.spring.api.servicebean;

import java.util.List;

import com.king.caesar.gamma.spring.api.methodbean.MethodBeanDefinition;

/**
 * 抽象服务定义，定义某些公共的服务属性
 * 
 * @author: Caesar
 * @date:   2017年4月19日 下午8:06:18
 */
public abstract class AbstractServiceBeanDefinition
{
    //服务名
    protected String name;
    
    //服务分组
    protected String group;
    
    //服务版本
    protected String version;
    
    //服务超时时间
    protected String timeout;
    
    //服务协议类型
    protected String protocol;
    
    //注册中心类型
    protected String registryType;
    
    //针对该服务方法的个性化配置
    protected List<MethodBeanDefinition> methods;

    public String getGroup()
    {
        return group;
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getTimeout()
    {
        return timeout;
    }

    public void setTimeout(String timeout)
    {
        this.timeout = timeout;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public String getRegistryType()
    {
        return registryType;
    }

    public void setRegistryType(String registryType)
    {
        this.registryType = registryType;
    }

    public List<MethodBeanDefinition> getMethods()
    {
        return methods;
    }

    public void setMethods(List<MethodBeanDefinition> methods)
    {
        this.methods = methods;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
