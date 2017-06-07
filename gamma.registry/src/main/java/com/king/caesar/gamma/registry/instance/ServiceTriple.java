package com.king.caesar.gamma.registry.instance;

import com.king.caesar.gamma.core.util.StringUtils;

/**
 * 服务三元组 G group 服务分组 S service name 服务名 V version 服务版本号
 * 
 * @author: Caesar
 * @date: 2017年5月6日 下午12:42:34
 * @param <T>
 * @param <U>
 * @param <V>
 */
public class ServiceTriple<G, S, V>
{
    private volatile G group;
    
    private volatile S service;
    
    private volatile V version;
    
    public ServiceTriple(G group, S service, V version)
    {
        this.group = group;
        this.service = service;
        this.version = version;
    }
    
    
    public G getGroup()
    {
        return group;
    }
    
    public void setGroup(G group)
    {
        this.group = group;
    }
    
    public S getService()
    {
        return service;
    }
    
    public void setService(S service)
    {
        this.service = service;
    }
    
    public V getVersion()
    {
        return version;
    }
    
    public void setVersion(V version)
    {
        this.version = version;
    }
    
    public int hashCode()
    {
        return StringUtils.hashCode(new Object[] {getGroup(), getService(), getVersion()});
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (null == obj)
        {
            return false;
        }
        if (!(obj instanceof ServiceTriple))
        {
            return false;
        }
        boolean result = false;
        ServiceTriple<?, ?, ?> ohters = (ServiceTriple<?, ?, ?>)obj;
        if (((ohters.getGroup() == null) && (getGroup() == null))
            || ((getGroup() != null) && getGroup().equals(ohters.getGroup())))
        {
            if (((ohters.getService() == null) && (getService() == null))
                || ((getService() != null) && getService().equals(ohters.getService())))
            {
                result = (((ohters.getVersion() == null) && (getVersion() == null))
                    || ((getVersion() != null) && getVersion().equals(ohters.getVersion())));
            }
        }
        return result;
    }
}
