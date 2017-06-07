package com.king.caesar.gamma.engine.pipeline;

import static org.junit.Assert.*;

import org.junit.Test;

import com.king.caesar.gamma.registry.instance.ServiceTriple;

public class ServiceTripleTest
{
    
    @Test
    public void testEquals()
    {
        ServiceTriple s1 = new ServiceTriple<>("default", "hell", "1.0");
        ServiceTriple s2 = new ServiceTriple<>("default", "hell", "1.0");
        assertEquals(true, s1.equals(s2));
    }
    
    @Test
    public void testNotEquals()
    {
        ServiceTriple s1 = new ServiceTriple<>("default", "hell", "1.0");
        ServiceTriple s2 = new ServiceTriple<>("default", "hell", "1.0.0");
        assertEquals(false, s1.equals(s2));
    }
    
}
