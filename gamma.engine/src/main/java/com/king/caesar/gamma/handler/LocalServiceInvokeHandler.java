package com.king.caesar.gamma.handler;

import com.king.caesar.gamma.rpc.api.context.Connector;
import com.king.caesar.gamma.rpc.api.context.Context;
import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.service.LocalService;
import com.king.caesar.gamma.rpc.service.LocalService.*;
import com.king.caesar.gamma.rpc.service.wrapper.LocalServiceWrapper;

/**
 * 本地服务调用，本地服务是指直接发起进程内部的方法调用，不走网络。
 * 
 * @author: Caesar
 * @date:   2017年5月28日 下午2:26:13
 */
public class LocalServiceInvokeHandler extends AbstractInboundHandler
{
    @Override
    public void handle(Context context)
    {
        System.out.println("local service invoke.");
        // 前面流程保证destConnector不为空
        Connector destConnector = context.getDestConnector();
        if (destConnector instanceof LocalServiceWrapper)
        {
            LocalServiceWrapper serviceWrapper = (LocalServiceWrapper)destConnector;
            Message request = context.getRequest();
            String service = request.getMessageHeader().getService();
            String operation = request.getMessageHeader().getOperation();
            ServiceKey key = new ServiceKey(service, operation);
            LocalService candiateService = serviceWrapper.findService(key);
            // 获取请求参数
            Object[] args = (Object[])request.getPayload();
            //反射调用,如果需要动态生成字节码调用，可以改造此处的findService方法
            Message response = candiateService.invoke(request,args);
            context.setResponse(response);
        }
    }
    
}
