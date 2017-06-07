package com.king.caesar.gamma.ha;

import java.util.List;

import com.king.caesar.gamma.balance.Balancer;
import com.king.caesar.gamma.registry.instance.ProviderService;
import com.king.caesar.gamma.rpc.api.context.Context;

public interface HaStrategy
{
    Object availableCall(Context context, List<ProviderService> services,Balancer balancer);
    
    String getType();
}
