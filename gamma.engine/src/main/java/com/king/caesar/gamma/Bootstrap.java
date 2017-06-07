package com.king.caesar.gamma;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import com.king.caesar.gamma.core.config.ContextParam;
import com.king.caesar.gamma.core.config.StartupConfig;
import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;
import com.king.caesar.gamma.core.util.ContextHolder;
import com.king.caesar.gamma.core.util.JaxbUtil;
import com.king.caesar.gamma.core.util.ResourceLoader;
import com.king.caesar.gamma.engine.ServiceEngine;

/**
 * Gamma启动器
 * 
 * @author: Caesar
 * @date: 2017年5月21日 下午1:23:45
 */
public class Bootstrap
{
    private static final String CONFIG_LOCATIONS = "springConfigLocations";
    
    private Map<String, String> contextParams = new HashMap<String, String>();
    
    private static Bootstrap bootstrap = null;
    
    public static void main(String[] args)
    {
        if (null == bootstrap)
        {
            bootstrap = new Bootstrap();
            bootstrap.init();
            bootstrap.start();
        }
        
    }
    
    public void init()
    {
        Resource startupXml = ResourceLoader.loadResource("startup.xml");
        StartupConfig startupConfig = null;
        try
        {
            startupConfig = JaxbUtil.toBean(StartupConfig.class, startupXml.getInputStream());
        }
        catch (IOException e)
        {
            throw new GammaException(Result.Code.FRAMEWORK_INITERROR, Result.CodeDesc.INIT_ERROR, e);
        }
        List<ContextParam> params = startupConfig.getParams();
        if (null != params)
        {
            for (ContextParam contextParam : params)
            {
                contextParams.put(contextParam.getName(), contextParam.getValue());
            }
        }
        if (!contextParams.containsKey(CONFIG_LOCATIONS))
        {
            throw new GammaException(Result.Code.FRAMEWORK_INITERROR, "Can not find spring config locations.");
        }
        String location = contextParams.get(CONFIG_LOCATIONS);
        String[] configLocations = location.split(",");
        ApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
        ContextHolder.setContext(context);
    }
    
    public void start()
    {
        ServiceEngine serviceEngine = new ServiceEngine();
        serviceEngine.init();
        serviceEngine.start();
    }
}
