package com.king.caesar.gamma.handler;

import com.king.caesar.gamma.pipeline.HandlerChainNode;
import com.king.caesar.gamma.rpc.api.context.Context;

/**
 * Handler用于处理接收到的请求
 * 
 * @author: Caesar
 * @date: 2017年4月23日 下午1:17:54
 */
public interface Handler
{
    void process(HandlerChainNode chainNode,Context context);
    
    void handle(Context context);
}
