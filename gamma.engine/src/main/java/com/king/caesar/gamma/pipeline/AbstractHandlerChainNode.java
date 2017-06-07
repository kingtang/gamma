package com.king.caesar.gamma.pipeline;

import com.king.caesar.gamma.handler.AbstractInboundHandler;
import com.king.caesar.gamma.handler.AbstractOutboundHandler;
import com.king.caesar.gamma.rpc.api.context.Context;
import com.king.caesar.gamma.rpc.api.context.ContextAware;

/**
 * 抽象HandlerChainNode，实现为双向链表 ________________________ | | | ------> | | NodeA NodeB | | <------ |
 * |______________________|
 * 
 * 
 * @author: Caesar
 * @date: 2017年4月23日 下午1:19:48
 */
public abstract class AbstractHandlerChainNode implements HandlerChainNode, ContextAware
{
    
    // prev节点
    AbstractHandlerChainNode prev;
    
    // next节点
    AbstractHandlerChainNode next;
    
    // handlerContext名字，全局唯一用于标识HandlerContext
    private String name;
    
    private boolean head;
    
    private boolean tail;
    
    private String preName;
    
    private String nextName;
    
    // 承载框架数据的上下文
    private Context context;
    
    AbstractHandlerChainNode(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * fireInProcess的方向是从尾开始，故取prev
     */
    public HandlerChainNode fireInProcess(Context context)
    {
        AbstractHandlerChainNode prevNode = this.prev;
        while(prevNode.handler() instanceof AbstractOutboundHandler)
        {
            prevNode = prevNode.prev;
        }
        prevNode.invokeProcess(context);
        return this;
    }
    
    /**
     * fireOutProcess的方向是从头开始，故取next
     */
    @Override
    public HandlerChainNode fireOutProcess(Context context)
    {
        AbstractHandlerChainNode nextNode = null;
        do
        {
            nextNode = this.next;
        } while (next.handler() instanceof AbstractInboundHandler);
        nextNode.invokeProcess(context);
        return this;
    }
    
    private void invokeProcess(Context context)
    {
        handler().process(this, context);
    }
    
    public Context getContext()
    {
        return context;
    }
    
    public void setContext(Context context)
    {
        this.context = context;
    }
    
    public boolean isHead()
    {
        return head;
    }
    
    public void setHead(boolean head)
    {
        this.head = head;
    }
    
    public boolean isTail()
    {
        return tail;
    }
    
    public void setTail(boolean tail)
    {
        this.tail = tail;
    }
    
    public String getPreName()
    {
        return preName;
    }
    
    public void setPreName(String preName)
    {
        this.preName = preName;
    }
    
    public String getNextName()
    {
        return nextName;
    }
    
    public void setNextName(String nextName)
    {
        this.nextName = nextName;
    }
    
}
