package com.king.caesar.gamma.connector;

import com.king.caesar.gamma.rpc.api.context.Connector;
import com.king.caesar.gamma.rpc.api.context.Context;

import io.netty.util.internal.chmv8.LongAdderV8;

/**
 * 该类提供connector基础的功能，比如当前正在处理的消息数跟踪
 * 
 * @author: Caesar
 * @date: 2017年4月25日 下午11:07:01
 */
public abstract class AbstractConnector implements Connector
{
    /*
     * LongAdderV8同样为大神DougLea的作品，高并发下的表现优于AtomicLong。
     * 而AtomicLong由于cpu-cache-line伪共享的问题表现不尽如人意。
     * 这个问题最初是由Disruptor的作者MartinThompson提出来的。
     * 由于AtomicLong并没有cache的填充，导致在高并发场景下cpu的缓存行由于和其他变量共享（其他变量，或者对象头）
     * 频繁失效。而LongAdderV8独占缓存行，缓存失效的概率大大降低。
     * 诚如DougLea所讲在并发量不高的情况下LongAdderV8和AtomicLong表现差不多，
     * 但是在高并发下LongAdderV8的性能更高。 ps:LongAdder并未提供类似incrementAndGet的方法。
     */
    private LongAdderV8 msgNumInprocess = new LongAdderV8();
    
    @Override
    public <T> T onReceive(Context context)
    {
        T response = null;
        try
        {
            msgNumInprocess.increment();
            response = doOnReceive(context);
        }
        catch (Throwable t)
        {
            
        }
        finally
        {
            msgNumInprocess.decrement();
        }
        return response;
    }
    
    protected abstract <T> T doOnReceive(Context context);
    
    public LongAdderV8 getMsgNumInprocess()
    {
        return msgNumInprocess;
    }
    
}
