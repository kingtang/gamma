package com.king.caesar.gamma.rpc.api.context;


/**
 * Connector参考tomcat中的connector 负责连接Engine和Protocol
 * 
 * @author: Caesar
 * @date: 2017年4月23日 下午12:16:15
 */
public interface Connector extends MessageFactory
{
    <T> T onReceive(Context context);
    
    String getProtocol();
}
