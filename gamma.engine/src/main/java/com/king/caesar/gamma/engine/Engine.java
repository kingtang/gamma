package com.king.caesar.gamma.engine;

import com.king.caesar.gamma.pipeline.Pipeline;
import com.king.caesar.gamma.rpc.api.context.Context;

/**
 * rpc服务引擎
 * 
 * @author: Caesar
 * @date:   2017年4月23日 下午12:57:25
 */
public interface Engine
{
    Pipeline getPipeline();
    
    void setPipeline(Pipeline pipeline);
    
    void service(Context context);
}
