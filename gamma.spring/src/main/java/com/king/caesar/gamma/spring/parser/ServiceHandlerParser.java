package com.king.caesar.gamma.spring.parser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.king.caesar.gamma.spring.bean.handler.DefaultHandlerBeanDefinition;

/**
 * 用于解析服务引擎中使用到的Handler
 * 
 * @author: Caesar
 * @date: 2017年5月21日 下午3:27:54
 */
public class ServiceHandlerParser extends AbstractSingleBeanDefinitionParser
{
    @Override
    protected Class<?> getBeanClass(Element element)
    {
        return DefaultHandlerBeanDefinition.class;
    }
    
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder)
    {
        builder.addPropertyValue("name", element.getAttribute("name"));
        builder.addPropertyValue("head", Boolean.parseBoolean(element.getAttribute("isHead")));
        builder.addPropertyValue("tail", Boolean.parseBoolean(element.getAttribute("isTail")));
        builder.addPropertyValue("preName", element.getAttribute("before"));
        builder.addPropertyValue("nextName", element.getAttribute("after"));
        builder.addPropertyValue("handler", element.getAttribute("ref"));
    }
    
    protected boolean shouldGenerateId()
    {
        return true;
    }
}
