package com.king.caesar.gamma.core;

import com.king.caesar.gamma.core.exception.GammaException;

/**
 * 
 * @Description:组件接口，描述一个组件的生命周期
 * @author: Caesar
 * @date:   2017年4月16日 下午4:10:08
 */
public interface Component
{
    void init() throws GammaException;
    
    void start() throws GammaException;
    
    void stop() throws GammaException;
}
