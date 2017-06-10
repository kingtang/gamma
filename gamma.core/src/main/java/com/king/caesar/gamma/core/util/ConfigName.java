package com.king.caesar.gamma.core.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.Resource;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;

/**
 * TODO 配置项集中在该类管理
 * 
 * @author: Caesar
 * @date: 2017年6月10日 下午8:27:04
 */
public final class ConfigName
{
    static Properties properties = new Properties();
    
    // 注册中心连接超时时间
    public static int REGISTRY_CONNECT_TIMEOUT;
    
    // 注册中心session失效时间
    public static int REGISTRY_SESSION_TIMEOUT;
    
    // 注册中心地址
    public static String REGISTRY_ADDRESS;
    
    // 应用名
    public static String APPLICATION_NAME;
    
    // 应用IP
    public static String IP;
    
    // 应用端口
    public static String PORT;
    
    public static Integer CONNECT_TIMEOUT_MILLIS;
    
    // socket发送缓存
    public static Integer SO_SNDBUF;
    
    // socket接收缓存
    public static Integer SO_RCVBUF;
    
    /**
     * 首次使用触发属性配置加载
     */
    static
    {
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
        
        REGISTRY_CONNECT_TIMEOUT = Integer.parseInt(properties.getProperty("registry.connect.timeout", "2000"));
        
        REGISTRY_SESSION_TIMEOUT = Integer.parseInt(properties.getProperty("registry.session.timeout", "2000"));
        
        REGISTRY_ADDRESS = properties.getProperty("registry.address");
        
        APPLICATION_NAME = properties.getProperty("application", "default");
        
        String address = properties.getProperty("gamma.address");
        String[] ipAndPort = address.split(":");
        if (ipAndPort.length != 2)
        {
            throw new GammaException(Result.Code.PARAM_FORMAT_ILLEGAL,
                String.format(Result.CodeDesc.PARAM_FORMAT_ILLEGAL, address));
        }
        
        IP = ipAndPort[0];
        PORT = ipAndPort[1];
        
        CONNECT_TIMEOUT_MILLIS = Integer.parseInt(properties.getProperty("channel.connect.timeout", "3000"));
        
        SO_SNDBUF = Integer.parseInt(properties.getProperty("io.sendbuffer", "65535"));
        SO_RCVBUF = Integer.parseInt(properties.getProperty("io.receivebuffer", "6535"));
    }
}
