package com.king.caesar.gamma.core.constants;

/**
 * 
 * 框架用到的常量
 * 
 * @author: Caesar
 * @date: 2017年4月23日 下午5:08:21
 */
public interface GammaConstants
{
    interface DefaultValue
    {
        String GROUP = "default";
        
        String VERSION = "1.0";
        
        String PROTOCOL = "gamma";
        
        String REGISTRY = "zookeeper";
        
        String HASTRATEGY = "failfast";
    }
    
    interface RegistryPath
    {
        // 根目录
        String ROOT = "gamma";
        
        // 服务提供者
        String PROVIDERS = "providers";
        
        // 服务消费者
        String CONSUMERS = "consumers";
    }
    
    interface Remoting
    {
        String CLIENTID = "clientId";
    }
    
    interface Attachment
    {
        // 请求ID
        String REQUESTID = "requestId";
        
        // 序列化方式
        String SERIALIZETYPE = "serializeType";
    }
    
    interface Attribute
    {
        // 处理类型
        String PROCESSTYPEKEY = "processTypeKey";
        
        //以应用为参考，外部请求进入为in
        String INPROCESS = "inProcess";
        
        //内部请求外部为out
        String OUTPROCESS = "outProcess";
    }
    
    //协议
    interface Protocal
    {
        String GAMMA = "gamma";
        
        String POJO = "pojo";
    }
}
