package com.king.caesar.gamma.registry.util;

import com.king.caesar.gamma.core.constants.GammaConstants;
import com.king.caesar.gamma.core.util.ConfigName;

public final class RegistryUtil
{
    public static final String PATH_SEPARATOR = "/";
    
    public static String getProviderPath(String serviceName, boolean endWithSlash)
    {
        StringBuilder pathBuilder = new StringBuilder(PATH_SEPARATOR);
        pathBuilder.append(GammaConstants.RegistryPath.ROOT)
            .append(PATH_SEPARATOR)
            .append(ConfigName.APPLICATION_NAME)
            .append(PATH_SEPARATOR)
            .append(serviceName)
            .append(PATH_SEPARATOR)
            .append(GammaConstants.RegistryPath.PROVIDERS);
        if (endWithSlash)
        {
            pathBuilder.append(PATH_SEPARATOR);
        }
        return pathBuilder.toString();
    }
    
    public static String getConsumerPath(String serviceName)
    {
        StringBuilder pathBuilder = new StringBuilder(PATH_SEPARATOR);
        pathBuilder.append(GammaConstants.RegistryPath.ROOT)
            .append(PATH_SEPARATOR)
            .append(ConfigName.APPLICATION_NAME)
            .append(PATH_SEPARATOR)
            .append(serviceName)
            .append(PATH_SEPARATOR)
            .append(GammaConstants.RegistryPath.CONSUMERS)
            .append(PATH_SEPARATOR);
        return pathBuilder.toString();
    }
}
