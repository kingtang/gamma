package com.king.caesar.gamma.rpc.api.context;

import com.king.caesar.gamma.rpc.api.message.Message;

/**
 * 服务调用上下文，贯穿整个服务调用周期
 * 
 * @author: Caesar
 * @date: 2017年6月3日 下午3:21:33
 */
public interface Context
{
    Message getRequest();
    
    void setRequest(Message request);
    
    Message getResponse();
    
    void setResponse(Message response);
    
    Connector getSrcConnector();
    
    Connector getDestConnector();
    
    void setSrcConnector(Connector srcConnector);
    
    void setDestConnector(Connector destConnector);
    
    // 获取超时时间
    int getTimeOutMs();
    
    void setTimeOutMs(int timeOut);
    
    Object getAttribute(String key);
    
    void setAttribute(String key, Object value);
    
}
