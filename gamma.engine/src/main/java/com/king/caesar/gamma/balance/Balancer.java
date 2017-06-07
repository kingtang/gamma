package com.king.caesar.gamma.balance;

import java.util.List;

import com.king.caesar.gamma.registry.instance.ProviderService;

public interface Balancer
{
    ProviderService selectOne(List<ProviderService> services,String service);
    
    String getType();
}
