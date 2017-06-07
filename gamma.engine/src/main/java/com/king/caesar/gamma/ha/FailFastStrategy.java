package com.king.caesar.gamma.ha;

import java.util.List;

import com.king.caesar.gamma.balance.Balancer;
import com.king.caesar.gamma.registry.instance.ProviderService;
import com.king.caesar.gamma.rpc.api.context.Connector;
import com.king.caesar.gamma.rpc.api.context.Context;
import com.king.caesar.gamma.rpc.service.RemoteService;
import com.king.caesar.gamma.rpc.service.wrapper.RemoteServiceWrapper;

/**
 * 
 * 快速失败，一旦服务调用失败，立即返回失败响应不再发起任何调用。
 * 
 * @author: Caesar
 * @date: 2017年6月3日 下午1:47:28
 */
public class FailFastStrategy implements HaStrategy
{
    
    @Override
    public Object availableCall(Context context, List<ProviderService> services, Balancer balancer)
    {
        String serviceName = context.getRequest().getMessageHeader().getService();
        ProviderService refer = balancer.selectOne(services, serviceName);
        Connector destConnector = context.getDestConnector();
        RemoteServiceWrapper serviceWrapper = (RemoteServiceWrapper)destConnector;
        RemoteService remoteService = serviceWrapper.getRemoteService(refer.getRemoteIp(), refer.getRemotePort());
        return remoteService.invoke(context.getRequest(), context);
    }
    
    @Override
    public String getType()
    {
        return "failfast";
    }
    
}
