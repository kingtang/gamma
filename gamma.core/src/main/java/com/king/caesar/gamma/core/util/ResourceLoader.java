package com.king.caesar.gamma.core.util;

import java.io.IOException;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;

/**
 * 资源加载器
 * 
 * @author: Caesar
 * @date: 2017年4月28日 下午8:13:23
 */
public abstract class ResourceLoader
{
    private static org.springframework.core.io.ResourceLoader resourceLoader =
        new DefaultResourceLoader(ClassUtils.getDefaultClassLoader());
    
    /**
     * 支持ant风格的路径表示
     * 
     * @param resourcePath
     * @return
     */
    public static Resource[] loadResources(String resourcePath)
    {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader);
        try
        {
            Resource[] resources = resolver.getResources(resourcePath);
            return resources;
        }
        catch (IOException e)
        {
            throw new GammaException(Result.Code.RESOURCE_LOAD_ERROR, "Load resource error.", e);
        }
    }
    
    /**
     * 支持ant风格的路径表示
     * 
     * @param resourcePath
     * @return
     */
    public static Resource loadResource(String resourcePath)
    {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader);
        return resolver.getResource(resourcePath);
    }
}
