package com.king.caesar.gamma.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.king.caesar.gamma.spring.parser.ServiceExporterParser;
import com.king.caesar.gamma.spring.parser.ServiceHandlerParser;
import com.king.caesar.gamma.spring.parser.ServiceImporterParser;

/**
 * 定制spring命名空间用于解析自定义标签。
 * 
 * @author: Caesar
 * @date: 2017年4月16日 下午5:28:38
 */
public class GammaNamespaceHandler extends NamespaceHandlerSupport
{
    
    public void init()
    {
        System.out.println("namespace handler init");
        registerBeanDefinitionParser("importer", new ServiceImporterParser());
        registerBeanDefinitionParser("exporter", new ServiceExporterParser());
        registerBeanDefinitionParser("handler", new ServiceHandlerParser());
    }
    
}
