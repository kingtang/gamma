package com.king.caesar.gamma.spring.bean.handler;

import com.king.caesar.gamma.spring.api.servicebean.HandlerBeanDefinition;

/**
 * Handler元数据
 * 
 * @author: Caesar
 * @date: 2017年5月21日 下午6:22:44
 */
public class DefaultHandlerBeanDefinition implements HandlerBeanDefinition
{
    // 是否头节点
    private boolean head;
    
    // 是否尾节点
    private boolean tail;
    
    // pre handler的名称
    private String preName;
    
    // next handler的名称
    private String nextName;
    
    // 所引用handler的bean名称
    private String handler;
    
    // handler名称
    private String name;
    
    @Override
    public boolean isHead()
    {
        return head;
    }
    
    @Override
    public void setHead(boolean head)
    {
        this.head = head;
    }
    
    @Override
    public boolean isTail()
    {
        return tail;
    }
    
    @Override
    public void setTail(boolean tail)
    {
        this.tail = tail;
    }
    
    @Override
    public String getPreName()
    {
        return preName;
    }
    
    @Override
    public void setPreName(String preName)
    {
        this.preName = preName;
    }
    
    @Override
    public String getNextName()
    {
        return nextName;
    }
    
    @Override
    public void setNextName(String nextName)
    {
        this.nextName = nextName;
    }
    
    @Override
    public void setHandler(String handler)
    {
        this.handler = handler;
    }
    
    @Override
    public String getHandler()
    {
        return handler;
    }
    
    @Override
    public void setName(String name)
    {
        this.name = name;
    }
    
    @Override
    public String getName()
    {
        return name;
    }
    
}
