package com.king.caesar.gamma.rpc.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.rpc.api.context.MessageFactory;
import com.king.caesar.gamma.rpc.api.message.Message;

public class LocalService
{
    private static final Logger log = LoggerFactory.getLogger(LocalService.class);
    
    private String serviceName;
    
    private Object target;
    
    private Method targetMethod;
    
    private MessageFactory messageFactory;
    
    public Message invoke(Message request,Object... args)
    {
        Message response = messageFactory.createResponse(request);
        try
        {
            // 调用反射本地方法
            Object result = targetMethod.invoke(target, args);
            response.setPayload(result);
        }
        catch (IllegalAccessException e)
        {
            response.setPayload(e);
            log.error("Invoke local service error.", e);
        }
        catch (IllegalArgumentException e)
        {
            response.setPayload(e);
            log.error("Illegal argument.", e);
        }
        catch (InvocationTargetException e)
        {
            Throwable targetException = e.getTargetException();
            response.setPayload(targetException);
        }
        return response;
    }
    
    /**
     * ServiceKey封装，当前包含service和operation后面还会细化 重写其hashcode和equals方法
     * 
     * @author: Caesar
     * @date: 2017年5月28日 下午1:25:02
     */
    public static class ServiceKey
    {
        private String serviceName;
        
        private String operationName;
        
        public ServiceKey(String serviceName, String operationName)
        {
            this.serviceName = serviceName;
            this.operationName = operationName;
        }
        
        public String getServiceName()
        {
            return serviceName;
        }
        
        public void setServiceName(String serviceName)
        {
            this.serviceName = serviceName;
        }
        
        public String getOperationName()
        {
            return operationName;
        }
        
        public void setOperationName(String operationName)
        {
            this.operationName = operationName;
        }
        
        @Override
        public int hashCode()
        {
            return serviceName.hashCode() + operationName.hashCode();
        }
        
        @Override
        public boolean equals(Object obj)
        {
            if (null == obj)
            {
                return false;
            }
            if (!(obj instanceof ServiceKey))
            {
                return false;
            }
            ServiceKey other = (ServiceKey)obj;
            if (serviceName.equals(other.serviceName) && operationName.equals(other.operationName))
            {
                return true;
            }
            return false;
        }
        
    }
    
    public String getServiceName()
    {
        return serviceName;
    }
    
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }
    
    public Method getTargetMethod()
    {
        return targetMethod;
    }
    
    public void setTargetMethod(Method targetMethod)
    {
        this.targetMethod = targetMethod;
    }
    
    public Object getTarget()
    {
        return target;
    }
    
    public void setTarget(Object target)
    {
        this.target = target;
    }

    public MessageFactory getMessageFactory()
    {
        return messageFactory;
    }

    public void setMessageFactory(MessageFactory messageFactory)
    {
        this.messageFactory = messageFactory;
    }
}
