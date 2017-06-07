package com.king.caesar.gamma.pipeline;

import com.king.caesar.gamma.handler.Handler;

/**
 * 默认HandlerChainNode实现
 * 
 * @author: Caesar
 * @date: 2017年4月23日 下午1:19:19
 */
public class DefaultHandlerChainNode extends AbstractHandlerChainNode
{
    
    private Handler handler;
    
    DefaultHandlerChainNode(String name, Handler handler)
    {
        super(name);
        this.handler = handler;
    }
    
    public Handler handler()
    {
        return handler;
    }
    
}
