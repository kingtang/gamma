package com.king.caesar.gamma.pipeline;

import com.king.caesar.gamma.handler.Handler;
import com.king.caesar.gamma.rpc.api.context.Context;

/**
 * <p>
 * Pipeline接口提供如下功能：
 * </p>
 * <li>1、向pipeline中增加头节点</li>
 * <li></li>
 * <li></li>
 * <li></li>
 * 
 * @author: Caesar
 * @date: 2017年4月23日 下午1:09:51
 */
public interface Pipeline
{
    /**
     * 向pipeline的头结点增加Handler
     * @param name
     * @param handler
     * @return
     */
    Pipeline addFirst(String name, Handler handler);
    
    /**
     * 触发pipeline中的下一个节点执行
     * @param context
     * @return
     */
    Pipeline fireProcess(Context context);
    
    Pipeline addLast(String name,Handler handler);
}
