package com.king.caesar.gamma.spring.bean.service;

import com.king.caesar.gamma.spring.api.servicebean.AbstractServiceBeanDefinition;

/**
 * 服务导入定义，对应importer标签。 屏蔽默认值的处理，外部获取该对象的属性时已经有了默认值。
 * 
 * @author: Caesar
 * @date: 2017年4月19日 下午8:08:34
 */
public class ImportServiceBeanDefinition extends AbstractServiceBeanDefinition
{
    // 导入服务的类型
    private Class<?> interfaceClass;
    
    // 点对点直连服务提供地址
    private String directUrl;
    
    public Class<?> getInterfaceClass()
    {
        return interfaceClass;
    }
    
    public void setInterfaceClass(Class<?> interfaceClass)
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
}
