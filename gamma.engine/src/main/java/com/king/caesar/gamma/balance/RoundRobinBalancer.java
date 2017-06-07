package com.king.caesar.gamma.balance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;
import com.king.caesar.gamma.registry.instance.ProviderService;

/**
 * 轮询负载
 * 
 * @author: Caesar
 * @date: 2017年6月3日 下午1:50:07
 */
public class RoundRobinBalancer implements Balancer
{
    private Map<String, AtomicInteger> counters = new HashMap<String, AtomicInteger>();
    
    private Object counterLock = new Object();
    
    @Override
    public ProviderService selectOne(List<ProviderService> services, String service)
    {
        int size = services.size();
        if (null == services || size == 0)
        {
            throw new GammaException(Result.Code.NO_ROUTE_ERROR,
                String.format("Can not find route of service %s", service));
        }
        AtomicInteger counter = counters.get(service);
        if (null == counter)
        {
            // 锁防止多次new
            synchronized (counterLock)
            {
                if (null == counters.get(service))
                {
                    counter = new AtomicInteger(0);
                    counters.put(service, counter);
                }
            }
        }
        int countValue = Math.abs(counter.getAndIncrement());
        int index = countValue % services.size();
        return services.get(index);
    }
    
    @Override
    public String getType()
    {
        return "roundrobin";
    }
    
}
