package com.king.caesar.gamma.core.config;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 启动xml配置
 * 
 * @author: Caesar
 * @date: 2017年5月21日 下午1:39:05
 */
@XmlRootElement(name = "Startup")
public class StartupConfig
{
    
    private List<ContextParam> params;
    
    @XmlElements(value = {@XmlElement(name = "ContextParam", type = ContextParam.class)})
    public List<ContextParam> getParams()
    {
        return params;
    }
    
    public void setParams(List<ContextParam> params)
    {
        this.params = params;
    }
}
