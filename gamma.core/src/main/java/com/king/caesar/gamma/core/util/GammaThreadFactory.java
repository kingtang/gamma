package com.king.caesar.gamma.core.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadFactory用于创建Thread
 * 
 * @author: Caesar
 * @date:   2017年5月17日 下午7:34:10
 */
public class GammaThreadFactory implements ThreadFactory
{
    private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);
    
    private final AtomicInteger mThreadNum = new AtomicInteger(1);
    
    private final String mPrefix;
    
    private final boolean mDaemo;
    
    private final ThreadGroup mGroup;
    
    public GammaThreadFactory()
    {
        this("pool-" + POOL_SEQ.getAndIncrement(), false);
    }
    
    public GammaThreadFactory(String prefix)
    {
        this(prefix, false);
    }
    
    public GammaThreadFactory(String prefix, boolean daemo)
    {
        mPrefix = prefix + "-thread-";
        mDaemo = daemo;
        SecurityManager s = System.getSecurityManager();
        mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }
    
    public Thread newThread(Runnable runnable)
    {
        String name = mPrefix + mThreadNum.getAndIncrement();
        Thread ret = new Thread(mGroup, runnable, name, 0);
        ret.setDaemon(mDaemo);
        return ret;
    }
    
    public ThreadGroup getThreadGroup()
    {
        return mGroup;
    }
}
