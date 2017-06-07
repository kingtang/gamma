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
import com.king.caesar.gamma.rpc.invoker.ServiceExporter;
import com.king.caesar.gamma.spring.bean.service.ExportServiceBeanDefinition;

/**
 * 
 * 服务导出标签解析，对应的标签为exporter
 * 解析用户配置的服务导出属性
 * 
 * @author: Caesar
 * @date: 2017年4月16日 下午10:04:37
 */
public class ServiceExporterParser extends AbstractSingleBeanDefinitionParser
{
    private static Logger debugLog = LoggerFactory.getLogger(ServiceExporterParser.class);
    
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder)
    {
        debugLog.debug("Begin to parse export serivce xml.");
        ExportServiceBeanDefinition bean = new ExportServiceBeanDefinition();
        bean.setName(element.getAttribute("name").trim());
        
        String interfaceClass = element.getAttribute("interface").trim();
        Assert.notNull(interfaceClass, "export interface");
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
        
        builder.addPropertyValue("interfaceClass", interfaceClazz);
        builder.addPropertyValue("serviceBeanDefinition", bean);
        builder.addPropertyReference("ref",element.getAttribute("ref"));
    }
    
    @Override
    protected Class<?> getBeanClass(Element element)
    {
        return ServiceExporter.class;
    }
    
    protected boolean shouldGenerateId()
    {
        return true;
    }
}
