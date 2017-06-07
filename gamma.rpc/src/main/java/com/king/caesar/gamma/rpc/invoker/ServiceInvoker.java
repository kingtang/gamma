package com.king.caesar.gamma.rpc.invoker;

import static com.king.caesar.gamma.core.constants.GammaConstants.Attachment.SERIALIZETYPE;
import static com.king.caesar.gamma.core.constants.GammaConstants.DefaultValue.GROUP;
import static com.king.caesar.gamma.core.constants.GammaConstants.DefaultValue.PROTOCOL;
import static com.king.caesar.gamma.core.constants.GammaConstants.DefaultValue.VERSION;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import com.alibaba.fastjson.JSON;
import com.king.caesar.gamma.core.attribute.Attribute;
import com.king.caesar.gamma.core.attribute.ConfigAttribute;
import com.king.caesar.gamma.core.util.ConfigName;
import com.king.caesar.gamma.core.util.ContextHolder;
import com.king.caesar.gamma.core.util.StringUtils;
import com.king.caesar.gamma.registry.Registry;
import com.king.caesar.gamma.registry.RegistryFactory;
import com.king.caesar.gamma.registry.center.ServiceCenter;
import com.king.caesar.gamma.rpc.api.context.Connector;
import com.king.caesar.gamma.rpc.api.context.Context;
import com.king.caesar.gamma.rpc.api.invoker.Invoker;
import com.king.caesar.gamma.rpc.api.message.AttachmentObject;
import com.king.caesar.gamma.rpc.api.message.AttachmentType;
import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.api.message.MessageHeader;
import com.king.caesar.gamma.rpc.context.ServiceContext;
import com.king.caesar.gamma.rpc.service.ServiceInfo;
import com.king.caesar.gamma.spring.api.servicebean.AbstractServiceBeanDefinition;

/**
 * 负责创建服务代理,封装客户端调用远程服务的细节。
 * 
 * @author: Caesar
 * @date: 2017年5月6日 下午2:39:11
 * @param <T>
 */
public class ServiceInvoker<T>
    implements FactoryBean<T>, MethodInterceptor, Invoker<T>, InitializingBean, ApplicationContextAware
{
    private static final Logger log = LoggerFactory.getLogger(ServiceInvoker.class);
    
    // 服务属性，客户端超时等信息
    private AbstractServiceBeanDefinition serviceBeanDefinition;
    
    // 服务引用的接口信息
    private Class<T> interfaceClass;
    
    // 客户端指定的直连服务地址
    private String directUrl;
    
    // 引用的服务端服务Id
    private String referServiceId;
    
    // 服务代理对象，remote请求由该类处理
    private T proxyObject;
    
    // 高可用策略ID
    private String haStrategy;
    
    // 负载均衡策略ID
    private String loadbalancer;
    
    private Connector connector;
    
    private AtomicBoolean inited = new AtomicBoolean(false);
    
    public T getObject()
        throws Exception
    {
        if (inited.compareAndSet(false, true))
        {
            String registryType = serviceBeanDefinition.getRegistryType();
            Registry registry = RegistryFactory.getInstance().getRegistry(registryType);
            // 避免spring生成实例的时候产生不用的加载，使用时需调用connect方法，到此时才会真正产生到registry的连接。
            registry.connect(ConfigName.REGISTRY_ADDRESS);
            
            ServiceInfo serviceInfo = new ServiceInfo();
            // importer
            serviceInfo.setExporter(false);
            serviceInfo.setGroup(serviceBeanDefinition.getGroup());
            serviceInfo.setName(referServiceId);
            serviceInfo.setVersion(serviceBeanDefinition.getVersion());
            serviceInfo.setInterfaceName(interfaceClass.getName());
            serviceInfo.setProtocol(serviceBeanDefinition.getProtocol());
            // 作为客户端注册服务消费者信息
            registry.register(serviceInfo.getPath(), JSON.toJSONString(serviceInfo));
            // 订阅服务
            registry.subscribeService(serviceInfo.getProvidersPath(), ServiceCenter.getInstance());
        }
        return proxyObject;
    }
    
    public Class<?> getObjectType()
    {
        return getInterfaceClass();
    }
    
    public boolean isSingleton()
    {
        return false;
    }
    
    /**
     * 客户端调用服务的代理方法
     */
    public Object invoke(MethodInvocation invocation)
        throws Throwable
    {
        if (ReflectionUtils.isObjectMethod(invocation.getMethod()))
        {
            return "Can not invoke object method.";
        }
        Method method = invocation.getMethod();
        Object[] arguments = invocation.getArguments();
        
        String group = serviceBeanDefinition.getGroup();
        String versioin = serviceBeanDefinition.getVersion();
        String protocol = serviceBeanDefinition.getProtocol();
        String methodName = method.getName();
        
        // 构造服务调用上下文
        ServiceContext context = new ServiceContext();
        
        context.setTimeOutMs(Integer.parseInt(serviceBeanDefinition.getTimeout()));
        context.setAttribute(Attribute.OPTION_HA, haStrategy);
        context.setAttribute(Attribute.OPTION_BALANCER, loadbalancer);
        
        Message request = connector.createRequest();
        MessageHeader msgHeader = request.getMessageHeader();
        AttachmentObject serialType = new AttachmentObject(AttachmentType.REMOTE_IMPLICIT, (byte)1);
        msgHeader.addAttachment(SERIALIZETYPE, serialType);
        msgHeader.setGroup(StringUtils.trimAndGet(group, GROUP));
        msgHeader.setVersion(StringUtils.trimAndGet(versioin, VERSION));
        msgHeader.setProtocol(StringUtils.trimAndGet(protocol, PROTOCOL));
        msgHeader.setOperation(methodName);
        msgHeader.setService(referServiceId);
        
        request.setPayload(arguments);
        context.setRequest(request);
        
        return invoke(context);
        
    }
    
    private void setLoadbalancer()
    {
        if (StringUtils.isEmpty(loadbalancer))
        {
            loadbalancer = ConfigAttribute.BALANCER.value;
        }
    }
    
    private void setHaStrategy()
    {
        if (StringUtils.isEmpty(haStrategy))
        {
            haStrategy = ConfigAttribute.HA.value;
        }
    }
    
    private void setTimeout()
    {
        // 获取客户端超时时间
        String timeout = serviceBeanDefinition.getTimeout();
        // 如果服务定义文件未定义超时时间则取全局配置
        if (StringUtils.isEmpty(timeout))
        {
            timeout = ConfigAttribute.TIMEOUT.value;
        }
        serviceBeanDefinition.setTimeout(timeout);
    }
    
    @Override
    public Object invoke(Context context)
    {
        return connector.onReceive(context);
    }
    
    public AbstractServiceBeanDefinition getServiceBeanDefinition()
    {
        return serviceBeanDefinition;
    }
    
    public void setServiceBeanDefinition(AbstractServiceBeanDefinition serviceBeanDefinition)
    {
        this.serviceBeanDefinition = serviceBeanDefinition;
    }
    
    public Class<T> getInterfaceClass()
    {
        return interfaceClass;
    }
    
    public void setInterfaceClass(Class<T> interfaceClass)
    {
        this.interfaceClass = interfaceClass;
    }
    
    public String getDirectUrl()
    {
        return directUrl;
    }
    
    public void setDirectUrl(String directUrl)
    {
        this.directUrl = directUrl;
    }
    
    public String getReferServiceId()
    {
        return referServiceId;
    }
    
    public void setReferServiceId(String referServiceId)
    {
        this.referServiceId = referServiceId;
    }
    
    public T getProxyObject()
    {
        return proxyObject;
    }
    
    public void setProxyObject(T proxyObject)
    {
        this.proxyObject = proxyObject;
    }
    
    public void afterPropertiesSet()
        throws Exception
    {
        log.info("Generate service proxy,interface is {} and service name is {}.",
            interfaceClass,
            serviceBeanDefinition.getName());
        // 设置客户端超时时间
        setTimeout();
        setHaStrategy();
        setLoadbalancer();
        // 创建代理类
        this.proxyObject = ProxyFactory.getProxy(getInterfaceClass(), this);
        // connector
        connector = (Connector)ContextHolder.getContext().getBean("pojoConnector");
    }
    
    public Connector getConnector()
    {
        return connector;
    }
    
    public void setConnector(Connector connector)
    {
        this.connector = connector;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException
    {
        ContextHolder.setContext(applicationContext);
    }
    
    public String getHaStrategy()
    {
        return haStrategy;
    }
    
    public void setHaStrategy(String haStrategy)
    {
        this.haStrategy = haStrategy;
    }
    
    public String getLoadbalancer()
    {
        return loadbalancer;
    }
    
    public void setLoadbalancer(String loadbalancer)
    {
        this.loadbalancer = loadbalancer;
    }
    
}
