package com.king.caesar.gamma.spring.api.servicebean;

public interface HandlerBeanDefinition
{
    boolean isHead();
    
    void setHead(boolean head);
    
    boolean isTail();
    
    void setTail(boolean tail);
    
    String getPreName();
    
    void setPreName(String preName);
    
    String getNextName();
    
    void setNextName(String nextName);
    
    void setHandler(String handler);
    
    String getHandler();
    
    void setName(String name);
    
    String getName();
}
