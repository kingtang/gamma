package com.king.caesar.gamma.pipeline;

import java.util.HashMap;
import java.util.Map;

import static com.king.caesar.gamma.core.constants.GammaConstants.Attribute.*;
import com.king.caesar.gamma.handler.Handler;
import com.king.caesar.gamma.rpc.api.context.Context;

/**
 * pipeline默认实现
 * 
 * @author: Caesar
 * @date: 2017年4月23日 下午1:18:45
 */
public final class DefaultPipeline implements Pipeline
{
    
    final AbstractHandlerChainNode head;
    
    final AbstractHandlerChainNode tail;
    
    private Map<String, Handler> ctxHandlers = new HashMap<String, Handler>(4);
    
    public DefaultPipeline()
    {
        
        head = new HeadHandlerContext();
        tail = new TailHandlerContext();
        
        head.next = tail;
        tail.prev = head;
    }
    
    static final class HeadHandlerContext extends AbstractHandlerChainNode implements Handler
    {
        
        private static final String HEADNAME = "HEAD";
        
        HeadHandlerContext()
        {
            super(HEADNAME);
        }
        
        public Handler handler()
        {
            return this;
        }
        
        @Override
        public void process(HandlerChainNode chainNode, Context context)
        {
            System.out.println(this.getName());
        }
        
        @Override
        public void handle(Context context)
        {
            
        }
    }
    
    static final class TailHandlerContext extends AbstractHandlerChainNode implements Handler
    {
        
        private static final String TAILNAME = "TAIL";
        
        TailHandlerContext()
        {
            super(TAILNAME);
        }
        
        public Handler handler()
        {
            return this;
        }
        
        public void process(HandlerChainNode context)
        {
            System.out.println(this.getName());
        }
        
        @Override
        public void process(HandlerChainNode chainNode, Context context)
        {
            System.out.println(this.getName());
        }
        
        @Override
        public void handle(Context context)
        {
            
        }
    }
    
    public Pipeline addFirst(String name, Handler handler)
    {
        
        AbstractHandlerChainNode newCtx = new DefaultHandlerChainNode(name, handler);
        AbstractHandlerChainNode nextCtx = head.next;
        newCtx.prev = head;
        newCtx.next = nextCtx;
        
        head.next = newCtx;
        nextCtx.prev = newCtx;
        
        ctxHandlers.put(name, handler);
        return this;
    }
    
    public Pipeline addLast(String name, Handler handler)
    {
        AbstractHandlerChainNode newCtx = new DefaultHandlerChainNode(name, handler);
        AbstractHandlerChainNode preCtx = tail.prev;
        newCtx.next = tail;
        newCtx.prev = preCtx;
        
        tail.prev = newCtx;
        preCtx.next = newCtx;
        
        ctxHandlers.put(name, handler);
        return this;
    }
    
    public Map<String, Handler> getCtxHandlers()
    {
        return ctxHandlers;
    }
    
    public void setCtxHandlers(Map<String, Handler> ctxHandlers)
    {
        this.ctxHandlers = ctxHandlers;
    }
    
    @Override
    public Pipeline fireProcess(Context context)
    {
        String processType = (String)context.getAttribute(PROCESSTYPEKEY);
        if (INPROCESS.equals(processType))
        {
            tail.fireInProcess(context);
        }
        else
        {
            head.fireOutProcess(context);
        }
        return this;
    }
    
}
