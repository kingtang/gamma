package com.king.caesar.gamma.rpc.api.context;

public interface ContextAware
{
    Context getContext();
    
    void setContext(Context context);
}
