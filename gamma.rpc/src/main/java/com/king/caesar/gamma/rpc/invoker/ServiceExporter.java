package com.king.caesar.gamma.rpc.invoker;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;
import com.king.caesar.gamma.rpc.api.context.Connector;
import com.king.caesar.gamma.rpc.api.exporter.Exporter;
import com.king.caesar.gamma.rpc.api.service.Service;
import com.king.caesar.gamma.rpc.service.LocalService;
import com.king.caesar.gamma.rpc.service.ServiceInfo;
import com.king.caesar.gamma.rpc.service.wrapper.LocalServiceWrapper;
import com.king.caesar.gamma.spring.api.servicebean.AbstractServiceBeanDefinition;

/**
 * 服务导出器，负责在spring生命周期refreshed的时候导出自己的服务。
 * 
 * @author: Caesar
 * @date: 2017年6月10日 下午8:12:08
 */
public class ServiceExporter
    implements Exporter, ApplicationContextAware, InitializingBean, ApplicationListener<ContextRefreshedEvent>
{
    private volatile boolean isExported;
    
    // 所导出服务的属性
    private AbstractServiceBeanDefinition serviceBeanDefinition;
    
    // 包装模式
    private Connector connector;
    
    private ApplicationContext context;
    
    // 导出服务的接口信息
    private Class<?> interfaceClass;
    
    // 服务实现
    private Object ref;
    
    @Override
    public void afterPropertiesSet()
        throws Exception
    {
        String protocol = serviceBeanDefinition.getProtocol();
        Map<String, Connector> connectors = context.getBeansOfType(Connector.class);
        if (CollectionUtils.isEmpty(connectors))
        {
            throw new GammaException(Result.Code.FRAMEWORK_INITERROR, "Can not find any connector.");
        }
        Iterator<Entry<String, Connector>> iterator = connectors.entrySet().iterator();
        while (iterator.hasNext())
        {
            Entry<String, Connector> curConnector = iterator.next();
            // TODO 改为annotation
            Connector connectorTemp = curConnector.getValue();
            if (protocol.equals(connectorTemp.getProtocol()))
            {
                connector = connectorTemp;
            }
            // 处理local servicewrapper的场景
            if (connectorTemp instanceof LocalServiceWrapper)
            {
                LocalServiceWrapper serviceWrapper = (LocalServiceWrapper)connectorTemp;
                Method[] methods = interfaceClass.getMethods();
                // 解析待导出服务的本地映射
                for (Method method : methods)
                {
                    // 排除object的方法
                    if (ReflectionUtils.isObjectMethod(method))
                    {
                        continue;
                    }
                    LocalService service = new LocalService();
                    service.setServiceName(serviceBeanDefinition.getName());
                    service.setTargetMethod(method);
                    service.setTarget(ref);
                    service.setMessageFactory(connectorTemp);
                    serviceWrapper.addService(service);
                }
            }
        }
        if (null == connector)
        {
            throw new GammaException(Result.Code.FRAMEWORK_INITERROR, "Can not find effective connector.");
        }
        
    }
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        // spring容器完成初始化后再导出服务，避免服务端未ready的情况产生。
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setExporter(true);
        serviceInfo.setGroup(serviceBeanDefinition.getGroup());
        serviceInfo.setName(serviceBeanDefinition.getName());
        serviceInfo.setVersion(serviceBeanDefinition.getVersion());
        serviceInfo.setInterfaceName(interfaceClass.getName());
        serviceInfo.setProtocol(serviceBeanDefinition.getProtocol());
        serviceInfo.getExtInfos().put("registryType", serviceBeanDefinition.getRegistryType());
        
        export(serviceInfo);
    }
    
    @Override
    public void export(Service service)
    {
        // 一个服务只会导出一遍
        if (connector instanceof Exporter && !isExported)
        {
            Exporter exporter = (Exporter)connector;
            //服务导出由对应的connector负责
            exporter.export(service);
            isExported = true;
        }
    }
    
    @Override
    public void unexport()
    {
        
    }
    
    public AbstractServiceBeanDefinition getServiceBeanDefinition()
    {
        return serviceBeanDefinition;
    }
    
    public void setServiceBeanDefinition(AbstractServiceBeanDefinition serviceBeanDefinition)
    {
        this.serviceBeanDefinition = serviceBeanDefinition;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException
    {
        context = applicationContext;
    }
    
    public Object getRef()
    {
        return ref;
    }
    
    public void setRef(Object ref)
    {
        this.ref = ref;
    }
    
    public Class<?> getInterfaceClass()
    {
        return interfaceClass;
    }
    
    public void setInterfaceClass(Class<?> interfaceClass)
    {
        this.interfaceClass = interfaceClass;
    }
}
