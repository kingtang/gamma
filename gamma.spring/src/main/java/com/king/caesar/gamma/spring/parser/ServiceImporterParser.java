package com.king.caesar.gamma.spring.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.king.caesar.gamma.core.constants.GammaConstants;
import com.king.caesar.gamma.core.util.Assert;
import com.king.caesar.gamma.core.util.ClassUtils;
import com.king.caesar.gamma.core.util.StringUtils;
import com.king.caesar.gamma.rpc.invoker.ServiceInvoker;
import com.king.caesar.gamma.spring.bean.service.ImportServiceBeanDefinition;

/**
 * 
 * 服务引用标签解析，对应的标签为importer
 * 
 * @author: Caesar
 * @date: 2017年4月16日 下午10:04:37
 */
public class ServiceImporterParser extends AbstractSingleBeanDefinitionParser
{
    private static Logger debugLog = LoggerFactory.getLogger(ServiceImporterParser.class);
    
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder)
    {
        debugLog.debug("Begin to parse serivce xml.");
        ImportServiceBeanDefinition bean = new ImportServiceBeanDefinition();
        bean.setName(element.getAttribute("name").trim());
        
        String interfaceClass = element.getAttribute("interface").trim();
        Assert.notNull(interfaceClass, "import interface");
        Class<?> interfaceClazz = null;
        interfaceClazz = ClassUtils.forName(interfaceClass, Thread.currentThread().getContextClassLoader());
        bean.setInterfaceClass(interfaceClazz);
        
        String timeout = element.getAttribute("timeout");
        bean.setTimeout(timeout);
        
        String group = element.getAttribute("group");
        bean.setGroup(StringUtils.trimAndGet(group, GammaConstants.DefaultValue.GROUP));
        
        String version = element.getAttribute("version");
        bean.setVersion(StringUtils.trimAndGet(version, GammaConstants.DefaultValue.VERSION));
        
        String protocol = element.getAttribute("protocol");
        bean.setProtocol(StringUtils.trimAndGet(protocol, GammaConstants.DefaultValue.PROTOCOL));
        
        String registryType = element.getAttribute("registryType");
        bean.setRegistryType(StringUtils.trimAndGet(registryType, GammaConstants.DefaultValue.REGISTRY));
        
        String referServiceId = element.getAttribute("remoteServiceId");
        builder.addPropertyValue("referServiceId", referServiceId);
        
        String directUrl = element.getAttribute("directUrl");
        builder.addPropertyValue("directUrl", directUrl);
        builder.addPropertyValue("interfaceClass", interfaceClazz);
        
        // 高可用策略
        String haStrategy = element.getAttribute("ha");
        builder.addPropertyValue("haStrategy", haStrategy);
        String balancer = element.getAttribute("balancer");
        builder.addPropertyValue("loadbalancer", balancer);
        builder.addPropertyValue("serviceBeanDefinition", bean);
        
    }
    
    @Override
    protected Class<?> getBeanClass(Element element)
    {
        return ServiceInvoker.class;
    }
    
    protected boolean shouldGenerateId()
    {
        return true;
    }
}
