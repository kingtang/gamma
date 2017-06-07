package com.king.caesar.gamma.spring.api.methodbean;

import java.util.List;

/**
 * 方法标签定义，某些服务中的一些方法可能比较重要或者耗时
 * 可以通过该标签针对此类方法单独进行个性化定义。
 * 
 * @author: Caesar
 * @date:   2017年4月19日 下午7:58:35
 */
public class MethodBeanDefinition
{
    //方法名
    private String name;
    
    //方法参数，用于识别重载的方法
    private List<Class<?>> argumentTypes;
    
    //该方法绑定的线程池
    private List<String> executePool;
    
    //请求超时时间（优先级：方法级别>服务级别>全局）
    private int timeout;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<String> getExecutePool()
    {
        return executePool;
    }

    public void setExecutePool(List<String> executePool)
    {
        this.executePool = executePool;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }

    public List<Class<?>> getArgumentTypes()
    {
        return argumentTypes;
    }

    public void setArgumentTypes(List<Class<?>> argumentTypes)
    {
        this.argumentTypes = argumentTypes;
    }

    @Override
    public String toString()
    {
        return "MethodBeanDefinition [name=" + name + ", argumentTypes=" + argumentTypes + ", executePool="
            + executePool + ", timeout=" + timeout + "]";
    }
}
