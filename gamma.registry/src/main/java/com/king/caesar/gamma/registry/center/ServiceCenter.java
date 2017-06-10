package com.king.caesar.gamma.registry.center;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.king.caesar.gamma.registry.instance.ProviderService;
import com.king.caesar.gamma.registry.instance.ServiceTriple;
import com.king.caesar.gamma.registry.util.RegistryUtil;
import com.king.caesar.gamma.registry.zookeeper.listener.ServiceListener;
import com.king.caesar.gamma.rpc.api.service.Service;

/**
 * 服务中心，用于管理服务
 * 
 * @author: Caesar
 * @date: 2017年5月6日 下午1:20:50
 */
public class ServiceCenter implements ServiceListener
{
    public static final int SERVICETRIPLE_IDX = 3;
    
    public static final int PROVIDER_IDX = 5;
    
    public static final int GROUP_IDX = 0;
    
    public static final int SERVICE_IDX = 1;
    
    public static final int VERSION_IDX = 2;
    
    public static final String COLON = ":";
    
    public static final Logger log = LoggerFactory.getLogger(ServiceCenter.class);
    
    private static class ServiceCenterHolder
    {
        private static final ServiceCenter INSTANCE = new ServiceCenter();
    }
    
    // 服务三元组，分组|服务名|服务版本号
    private ConcurrentMap<ServiceTriple<String, String, String>, List<ProviderService>> providerServices =
        Maps.newConcurrentMap();
    
    public static ServiceCenter getInstance()
    {
        return ServiceCenterHolder.INSTANCE;
    }
    
    public List<ProviderService> getServices(ServiceTriple<String, String, String> serviceKey)
    {
        return providerServices.get(serviceKey);
    }
    
    /**
     * ConcurrentMap<NotifyListener, ChildListener> listeners =
     * zkListeners.get(url); if (listeners == null) {
     * zkListeners.putIfAbsent(url, new
     * ConcurrentHashMap<NotifyListener, ChildListener>()); listeners
     * = zkListeners.get(url); }
     */
    /**
     * 服务路径--->/gamma/application/service/providers/provider
     */
    @Override
    public synchronized void serviceAdded(Service addedService, String servicePath)
    {
        String[] paths = servicePath.split(RegistryUtil.PATH_SEPARATOR);
        ServiceTriple<String, String, String> serviceKey = createServiceTriple(paths);
        
        List<ProviderService> services = providerServices.get(serviceKey);
        if (null == services)
        {
            providerServices.putIfAbsent(serviceKey, new CopyOnWriteArrayList<ProviderService>());
            services = providerServices.get(serviceKey);
        }
        String provider = paths[PROVIDER_IDX];
        String[] ipPortArray = provider.split(COLON);
        ProviderService providerService = new ProviderService();
        providerService.setName(addedService.getName());
        providerService.setRemoteIp(ipPortArray[0]);
        providerService.setRemotePort(ipPortArray[1]);
        providerService.setProtocol(addedService.getProtocol());
        providerService.setWeight(addedService.getWeight());
        if (!services.contains(providerService))
        {
            services.add(providerService);
        }
        log.info("The new service has been added to the service center.[{}]", providerService);
    }
    
    private ServiceTriple<String, String, String> createServiceTriple(String[] paths)
    {
        String serviceTriple = paths[SERVICETRIPLE_IDX];
        
        log.info("There is a new provider service online and the service name is {}.", serviceTriple);
        
        String[] serviceArray = serviceTriple.split(Service.SERVICE_SEPERATOR);
        ServiceTriple<String, String, String> serviceKey = new ServiceTriple<String, String, String>(
            serviceArray[GROUP_IDX], serviceArray[SERVICE_IDX], serviceArray[VERSION_IDX]);
        return serviceKey;
    }
    
    @Override
    public synchronized void serviceDeleted(Service deletedService, String servicePath)
    {
        String[] paths = servicePath.split(RegistryUtil.PATH_SEPARATOR);
        ServiceTriple<String, String, String> serviceKey = createServiceTriple(paths);
        String provider = paths[PROVIDER_IDX];
        log.info("There is a provider[{}] offline.", provider);
        String[] ipPortArray = provider.split(COLON);
        ProviderService service = new ProviderService();
        service.setRemoteIp(ipPortArray[0]);
        service.setRemotePort(ipPortArray[1]);
        List<ProviderService> services = providerServices.get(serviceKey);
        if (null != services)
        {
            services.remove(service);
        }
    }
    
    @Override
    public void serviceUpdated(Service updatedService, String servicePath)
    {
        
    }
}
