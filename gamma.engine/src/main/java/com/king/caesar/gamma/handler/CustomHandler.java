package com.king.caesar.gamma.handler;

import com.king.caesar.gamma.rpc.api.context.Context;

public abstract class CustomHandler extends AbstractHandler
{
    @Override
    public abstract void handle(Context context);
    
}
