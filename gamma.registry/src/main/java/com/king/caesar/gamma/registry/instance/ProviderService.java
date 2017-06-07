package com.king.caesar.gamma.registry.instance;

/**
 * 服务实体在客户端的描述，客户端路由的时候需要获取。<br>
 * <p>
 * 比如基于延时的动态路由，可以基于此类做服务质量的统计。
 * 
 * @author: Caesar
 * @date: 2017年4月26日 下午9:38:21
 */
public class ProviderService
{
    private String name;
    
    private String remoteIp;
    
    private String remotePort;
    
    private String protocol;
    
    private Integer weight;
    
    public Integer getWeight()
    {
        return weight;
    }
    
    public void setWeight(Integer weight)
    {
        this.weight = weight;
    }
    
    public String getProtocol()
    {
        return protocol;
    }
    
    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }
    
    public String getRemoteIp()
    {
        return remoteIp;
    }
    
    public void setRemoteIp(String remoteIp)
    {
        this.remoteIp = remoteIp;
    }
    
    public String getRemotePort()
    {
        return remotePort;
    }
    
    public void setRemotePort(String remotePort)
    {
        this.remotePort = remotePort;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    @Override
    public String toString()
    {
        return "ProviderService [remoteIp=" + remoteIp + ", remotePort=" + remotePort + ", protocol=" + protocol
            + ", weight=" + weight + "]";
    }
    
    @Override
    public int hashCode()
    {
        int ipHashCode = null == remoteIp ? 0 : remoteIp.hashCode();
        int portHashCode = null == remotePort ? 0 : remotePort.hashCode();
        return ipHashCode + portHashCode;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (null == obj)
        {
            return false;
        }
        else if (!(obj instanceof ProviderService))
        {
            return false;
        }
        ProviderService other = (ProviderService)obj;
        if (remoteIp.equals(other.getRemoteIp()) && remotePort.equals(other.getRemotePort()))
        {
            return true;
        }
        return super.equals(obj);
    }
    
}
