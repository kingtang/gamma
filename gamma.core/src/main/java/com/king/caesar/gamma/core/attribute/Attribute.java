package com.king.caesar.gamma.core.attribute;

public interface Attribute
{
    String OPTION_MAXIDLE = "OPTION_MAXIDLE";
    
    String OPTION_MINIDLE = "OPTION_MINIDLE";
    
    String OPTION_MAXACTIVE = "OPTION_MAXACTIVE";
    
    String OPTION_MAXWAIT = "OPTION_MAXWAIT";
    
    // 客户端服务调用超时时间
    String OPTION_TIMEOUT = "OPTION_TIMEOUT";
    
    String OPTION_HA = "OPTION_HA";
    
    String OPTION_BALANCER = "OPTION_BALANCER";
    
    String OPTION_ATTRIBUTES = "OPTION_ATTRIBUTES";
    
    String OPTION_SERVER_DECODE_POOL = "OPTION_SERVER_DECODE_POOL";
    
    String OPTION_SERVER_ENCODE_POOL = "OPTION_SERVER_ENCODE_POOL";
    
    String OPTION_CLIENT_DECODE_POOL = "OPTION_CLIENT_DECODE_POOL";
    
    String OPTION_CLIENT_ENCODE_POOL = "OPTION_CLIENT_ENCODE_POOL";
    
    void addLocalAttribute(String key, Object value);
    
    Object getLocalAttribute(String key);
    
    Object removeLocalAttribute(String key);
    
    void clearLocalAttributes();
}
