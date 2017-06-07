package com.king.caesar.gamma.rpc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;
import com.king.caesar.gamma.remoting.api.ClientOption;
import com.king.caesar.gamma.remoting.api.RemotingClient;
import com.king.caesar.gamma.rpc.api.context.Context;
import com.king.caesar.gamma.rpc.api.message.Message;

/**
 * 封装请求细节
 * 
 * @author: Caesar
 * @date: 2017年6月4日 下午6:05:15
 */
public class RemoteService
{
    private static final Logger log = LoggerFactory.getLogger(LocalService.class);
    
    private RemotingClient client;
    
    public Object invoke(Message request, Context context)
    {
        // 状态检查
        // client.isConnected();
        // 获取客户端超时时间
        int timeOutMs = context.getTimeOutMs();
        client.getRegister().registerCallback(request.getSessionId(), context);
        // 发送异步请求
        client.sendOneway(request);
        Message response = context.getResponse();
        if (null == response)
        {
            synchronized (context)
            {
                int waitTime = timeOutMs;
                long startTime = System.currentTimeMillis();
                // 如果为空则继续wait，但是当到达超时时间后，需要主动跳出循环
                while (null == context.getResponse() && waitTime >= 0)
                {
                    try
                    {
                        context.wait(waitTime);
                        waitTime = timeOutMs - (int)(System.currentTimeMillis() - startTime);
                    }
                    catch (InterruptedException e)
                    {
                        // ignore
                    }
                }
            }
            if (null == context.getResponse())
            {
                ClientOption option = client.getClientOption();
                String errorInfo = String.format("Request time out.[%s:%s/%s]",
                    option.getRemoteIp(),
                    option.getPort(),
                    context.getRequest().getMessageHeader().getService());
                throw new GammaException(Result.Code.REQUEST_TIMEOUT, errorInfo);
            }
        }
        return context.getResponse();
    }
    
    public void setClient(RemotingClient client)
    {
        this.client = client;
    }
}
