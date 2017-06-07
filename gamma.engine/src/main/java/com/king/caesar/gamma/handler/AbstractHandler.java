package com.king.caesar.gamma.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.pipeline.HandlerChainNode;
import com.king.caesar.gamma.rpc.api.context.Context;

/**
 * 提供流程驱动的抽象，使用者不用关注Pipeline的执行。
 * 
 * @author: Caesar
 * @date: 2017年4月25日 下午10:59:33
 */
public abstract class AbstractHandler implements Handler
{
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    @Override
    public void process(HandlerChainNode chainNode, Context context)
    {
        try
        {
            handle(context);
            chainNode.fireInProcess(context);
        }
        catch (Exception e)
        {
            log.error("Handler process message error.", e);
            throw e;
        }
    }
    
}
