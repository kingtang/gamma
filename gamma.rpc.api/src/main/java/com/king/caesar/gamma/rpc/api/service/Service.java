package com.king.caesar.gamma.rpc.api.service;

import java.util.Map;

/**
 * 服务抽象
 * 
 * @author: Caesar
 * @date: 2017年5月6日 上午11:59:47
 */
public interface Service
{
    final String SERVICE_SEPERATOR = "_";
    
    public boolean isExporter();
    
    public String getName();
    
    public String getPath();
    
    public String getProvidersPath();
    
    public String getGroup();
    
    public String getVersion();
    
    public String getInterfaceName();
    
    public String getProtocol();
    
    // 服务权重，服务端属性，该属性用于客户端路由
    public Integer getWeight();
    
    public Map<String, String> getExtInfos();
    
}
