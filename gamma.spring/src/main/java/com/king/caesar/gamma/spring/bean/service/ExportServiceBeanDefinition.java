package com.king.caesar.gamma.spring.bean.service;

import com.king.caesar.gamma.spring.api.servicebean.AbstractServiceBeanDefinition;

/**
 * 服务导出定义，对应exporter标签
 * 
 * @author: Caesar
 * @date: 2017年4月19日 下午8:08:13
 */
public class ExportServiceBeanDefinition extends AbstractServiceBeanDefinition
{
    // 导入服务的类型
    private Class<?> interfaceClass;
    
    public Class<?> getInterfaceClass()
    {
        return interfaceClass;
    }
    
    public void setInterfaceClass(Class<?> interfaceClass)
    {
        this.interfaceClass = interfaceClass;
    }
}
