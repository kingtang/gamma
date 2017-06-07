package com.king.caesar.gamma.pipeline;

import com.king.caesar.gamma.handler.Handler;
import com.king.caesar.gamma.rpc.api.context.Context;

/**
 * Handler节点，提供触发下一个节点执行的逻辑
 * 
 * @author: Caesar
 * @date:   2017年4月23日 下午1:17:28
 */
public interface HandlerChainNode {
	
	Handler handler();
	
	HandlerChainNode fireInProcess(Context context);
	
	HandlerChainNode fireOutProcess(Context context);
}
