package com.king.caesar.gamma.test;

public class Main
{
    public static void main(String[] args)
    {
        Exception e = new NullPointerException();
        System.out.println(e instanceof Exception);
        System.out.println(null instanceof Main);
        String[] s = new String[2];
        NullPointerException[] ne = new NullPointerException[2];
        RuntimeException[] re = new RuntimeException[2];
        System.out.println(s instanceof Object);
        
        System.out.println(ne instanceof RuntimeException[]);
        System.out.println(re instanceof NullPointerException[]);
    }
}
