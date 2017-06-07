package com.king.caesar.gamma.rpc.service;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.caesar.gamma.core.util.ConfigName;
import com.king.caesar.gamma.registry.center.ServiceCenter;
import com.king.caesar.gamma.registry.util.RegistryUtil;
import com.king.caesar.gamma.rpc.api.service.Service;

/**
 * 服务实体在服务端的抽象，该类描述了服务端服务的所有属性，注册服务时 会注册到服务中心。
 * 
 * @author: Caesar
 * @date: 2017年6月3日 下午4:11:48
 */
public class ServiceInfo implements Service
{
    private transient boolean isExporter;
    
    private transient String name;
    
    private transient String group;
    
    private transient String version;
    
    private String interfaceName;
    
    private String protocol;
    
    // 服务端的服务权重，客户端可以根据该权重做出服务的选择
    private Integer weight;
    
    // 服务扩展属性
    private Map<String, String> extInfos = new HashMap<String, String>();
    
    public boolean isExporter()
    {
        return isExporter;
    }
    
    public void setExporter(boolean isExporter)
    {
        this.isExporter = isExporter;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    @JSONField(serialize = false)
    public String getPath()
    {
        if (!isExporter)
        {
            String serviceName = group + SERVICE_SEPERATOR + name + SERVICE_SEPERATOR + version;
            String leafNode = ConfigName.IP + SERVICE_SEPERATOR + System.currentTimeMillis();
            return RegistryUtil.getConsumerPath(serviceName) + leafNode;
        }
        else
        {
            String serviceName = group + SERVICE_SEPERATOR + name + SERVICE_SEPERATOR + version;
            String leafNode = ConfigName.IP + ServiceCenter.COLON + ConfigName.PORT;
            return RegistryUtil.getProviderPath(serviceName, true) + leafNode;
        }
    }
    
    @JSONField(serialize = false)
    public String getProvidersPath()
    {
        String serviceName = group + SERVICE_SEPERATOR + name + SERVICE_SEPERATOR + version;
        return RegistryUtil.getProviderPath(serviceName, false);
    }
    
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
    
    public String getInterfaceName()
    {
        return interfaceName;
    }
    
    public void setInterfaceName(String interfaceName)
    {
        this.interfaceName = interfaceName;
    }
    
    public Map<String, String> getExtInfos()
    {
        return extInfos;
    }
    
    public void setExtInfos(Map<String, String> extInfos)
    {
        this.extInfos = extInfos;
    }
    
    @Override
    public String getProtocol()
    {
        return protocol;
    }
    
    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }
    
    public Integer getWeight()
    {
        return weight;
    }
    
    public void setWeight(Integer weight)
    {
        this.weight = weight;
    }
}
