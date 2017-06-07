package com.king.caesar.gamma.rpc.api.invoker;

import com.king.caesar.gamma.rpc.api.context.Context;

/**
 * 调用远程服务接口
 * 
 * @author: Caesar
 * @date: 2017年5月30日 下午4:45:01
 * @param <T>
 */
public interface Invoker<T>
{
    Object invoke(Context context);
}
