package com.king.caesar.gamma.rpc.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 生成请求ID
 * 
 * @author: Caesar
 * @date: 2017年5月30日 下午1:29:12
 */
public final class RequestId
{
    // private static LongAdderV8 id = new LongAdderV8();
    private static AtomicLong id = new AtomicLong(0);
    
    public static long get()
    {
        // ++，不考虑long值达到最大值的场景，首先这个最大值非常大，单进程不太可能跑到底
        // 其次即时达到最大值，继续累加会从最小负数开始，对于session类场景不影响
        return id.incrementAndGet();
    }
}
