package com.king.caesar.gamma.core.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.Resource;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;

public final class ConfigName
{
    static Properties properties = new Properties();
    
    public static int REGISTRY_CONNECT_TIMEOUT;
    
    public static int REGISTRY_SESSION_TIMEOUT;
    
    public static String REGISTRY_ADDRESS;
    
    public static String APPLICATION_NAME;
    
    public static String IP;
    
    public static String PORT;
    
    public static Integer CONNECT_TIMEOUT_MILLIS;
    
    public static Integer SO_SNDBUF;
    
    public static Integer SO_RCVBUF;
    
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
