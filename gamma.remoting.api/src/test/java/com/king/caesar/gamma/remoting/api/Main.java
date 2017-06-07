package com.king.caesar.gamma.remoting.api;

public class Main
{
    public static void main(String[] args)
    {
        new Thread(new Runnable()
        {
            ThreadLocal<StringBuilder> t = new ThreadLocal<>();
            @Override
            public void run()
            {
                System.out.println("start");
                t.set(new StringBuilder());
                StringBuilder stringBuilder = t.get();
                for(int i=0;i<1000000;i++)
                {
                    if(i%100000 == 0)
                    {
                        stringBuilder.setLength(0);
                    }
                    stringBuilder.append("aaaaaaaaaa");
                }
                
                while(true)
                {
                    
                }
            }
        }).start();
    }
}
