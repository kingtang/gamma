package com.king.caesar.gamma.core.util;

import org.springframework.context.ApplicationContext;

public enum ContextHolder
{
    ;
    private static ApplicationContext context;
    
    public static void setContext(ApplicationContext acontext)
    {
        context = acontext;
    }
    
    public static ApplicationContext getContext()
    {
        return context;
    }
}
