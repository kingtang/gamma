package com.king.caesar.gamma.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netty.util.internal.chmv8.LongAdderV8;

public class LongAdderTest
{
    public static void main(String[] args)
    {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        LongAdderV8 adder = new LongAdderV8();
        for(int i = 0;i<50;i++)
        {
            executor.execute(new MyTask(adder));
        }
    }
    
    public static class MyTask implements Runnable
    {
        private LongAdderV8 adder;
        
        public MyTask(LongAdderV8 adder)
        {
            this.adder = adder;
        }
        
        @Override
        public void run()
        {
            for (int i = 0; i < 100000; i++)
            {
                adder.increment();
                try
                {
                    Thread.sleep(10);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                long value = adder.longValue();
                System.out.println(value);
            }
        }
        
    }
}
