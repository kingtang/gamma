package com.king.caesar.gamma.core.constants;

/**
 * 
 * 错误码和错误描述常量 四位错误码 10表示初始化阶段
 * 
 * @author: Caesar
 * @date: 2017年4月20日 下午9:26:42
 */
public interface Result
{
    /**
     * 错误码
     * 
     * @author: Caesar
     * @date: 2017年4月20日 下午9:31:46
     */
    interface Code
    {
        // 框架初始化错误
        String FRAMEWORK_INITERROR = "1001";
        
        String INITCONFIG_ERROR = "1002";
        
        String NO_REGISTRY = "1003";
        
        // 加载资源错误
        String RESOURCE_LOAD_ERROR = "1004";
        
        // 参数为空异常
        String PARAM_NULL = "2001";
        
        String PARAM_FORMAT_ILLEGAL = "2002";
        
        String DEST_CONNECTOR_NOT_FOUND = "2003";
        
        // remoting测异常
        String RPC_SERVER_OPENERROR = "3001";
        
        String RPC_CLIENT_CONNECTERROR = "3002";
        
        // 调用服务时找不到路由
        String NO_ROUTE_ERROR = "3003";
        
        // 请求超时
        String REQUEST_TIMEOUT = "3004";
        
        // 消息非法
        String MESSAGE_ILLEGAL = "4001";
        
        String VERSION_ILLEGAL = "4002";
        
        String MESSAGEBYTES_ILLEGAL = "4003";
        
        // 消息发送失败
        String MSG_SEND_ERROR = "5001";
        
        // 连接状态异常
        String CONNECTION_STATUS_ILLEGAL = "5002";
    }
    
    interface CodeDesc
    {
        String INIT_ERROR = "gamma framework init error.";
        
        String PARAM_NULL = "%s must not be null.";
        
        String PARAM_FORMAT_ILLEGAL = "%s format is illegal.";
        
        String MESSAGE_ILLEGAL = "%s is illegal";
    }
}
