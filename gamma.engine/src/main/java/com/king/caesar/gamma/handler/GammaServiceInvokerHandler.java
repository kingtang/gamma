package com.king.caesar.gamma.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.king.caesar.gamma.balance.Balancer;
import com.king.caesar.gamma.core.attribute.Attribute;
import com.king.caesar.gamma.ha.HaStrategy;
import com.king.caesar.gamma.registry.center.ServiceCenter;
import com.king.caesar.gamma.registry.instance.ProviderService;
import com.king.caesar.gamma.registry.instance.ServiceTriple;
import com.king.caesar.gamma.rpc.api.context.Context;
import com.king.caesar.gamma.rpc.api.message.MessageHeader;

/**
 * 远程服务调用
 * 
 * @author: Caesar
 * @date: 2017年4月26日 下午9:24:46
 */
public class GammaServiceInvokerHandler extends AbstractOutboundHandler implements ApplicationContextAware
{
    private ApplicationContext springContext;
    
    private Map<String, HaStrategy> haStrategis = new HashMap<String, HaStrategy>();
    
    private Map<String, Balancer> balancers = new HashMap<String, Balancer>();
    
    public void init()
    {
        Map<String, HaStrategy> haBeans = springContext.getBeansOfType(HaStrategy.class);
        Iterator<Entry<String, HaStrategy>> iterator = haBeans.entrySet().iterator();
        while (iterator.hasNext())
        {
            Entry<String, HaStrategy> next = iterator.next();
            haStrategis.put(next.getValue().getType(), next.getValue());
        }
        
        Map<String, Balancer> balancerBeans = springContext.getBeansOfType(Balancer.class);
        Iterator<Entry<String, Balancer>> iterator2 = balancerBeans.entrySet().iterator();
        while (iterator2.hasNext())
        {
            Entry<String, Balancer> next = iterator2.next();
            balancers.put(next.getValue().getType(), next.getValue());
        }
    }
    
    @Override
    public void handle(Context context)
    {
        MessageHeader messageHeader = context.getRequest().getMessageHeader();
        String group = messageHeader.getGroup();
        String service = messageHeader.getService();
        String version = messageHeader.getVersion();
        ServiceTriple<String, String, String> serviceKey =
            new ServiceTriple<String, String, String>(group, service, version);
        // 获取所有的服务
        List<ProviderService> services = ServiceCenter.getInstance().getServices(serviceKey);
        
        String haStrategy = (String)context.getAttribute(Attribute.OPTION_HA);
        String balancerType = (String)context.getAttribute(Attribute.OPTION_BALANCER);
        HaStrategy highAvaliable = haStrategis.get(haStrategy);
        Balancer balancer = balancers.get(balancerType);
        highAvaliable.availableCall(context, services, balancer);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException
    {
        springContext = applicationContext;
    }
    
}
