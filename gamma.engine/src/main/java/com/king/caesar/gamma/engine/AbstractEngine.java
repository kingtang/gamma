package com.king.caesar.gamma.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.core.Component;
import com.king.caesar.gamma.core.exception.GammaException;

public abstract class AbstractEngine implements Engine, Component
{
    protected static final Logger log = LoggerFactory.getLogger(AbstractEngine.class);
    
    @Override
    public void init()
        throws GammaException
    {
        log.info("Begin init engine.");
    }
    
    @Override
    public void start()
        throws GammaException
    {
        log.info("Begin start engine.");
    }
    
    @Override
    public void stop()
        throws GammaException
    {
        log.info("Begin stop engine.");
    }
    
}
