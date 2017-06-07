package com.king.caesar.gamma.core.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.king.caesar.gamma.core.exception.GammaException;

public class AssertTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void test()
    {
        thrown.expect(GammaException.class);
        thrown.expectMessage("name must not be null.");
        com.king.caesar.gamma.core.util.Assert.notNull(null, "name");
    }
    
}
