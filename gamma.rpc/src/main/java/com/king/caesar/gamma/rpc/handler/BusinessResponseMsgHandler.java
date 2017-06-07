package com.king.caesar.gamma.rpc.handler;

import static com.king.caesar.gamma.core.constants.GammaConstants.Attachment.REQUESTID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.remoting.api.AbstractConnection;
import com.king.caesar.gamma.remoting.api.Connection;
import com.king.caesar.gamma.remoting.api.handler.MessageHandler;
import com.king.caesar.gamma.rpc.api.context.Context;
import com.king.caesar.gamma.rpc.api.message.AttachmentObject;
import com.king.caesar.gamma.rpc.api.message.Message;

/**
 * 服务端响应处理
 * 
 * @author: Caesar
 * @date: 2017年5月30日 下午12:41:07
 */
public class BusinessResponseMsgHandler implements MessageHandler
{
    private static final Logger log = LoggerFactory.getLogger(BusinessResponseMsgHandler.class);
    
    @Override
    public void handleMessage(Message message, Connection connection)
    {
        AttachmentObject attachRequestId = (AttachmentObject)message.getMessageHeader().getAndRemove(REQUESTID);
        Long requestId = (Long)attachRequestId.getValue();
        AbstractConnection conn = (AbstractConnection)connection;
        Context context = conn.unregisterCallback(requestId);
        // 有可能客户端已经超时服务端才给响应
        if (null == context)
        {
            log.warn("Can not find the session {}", requestId);
            return;
        }
        synchronized (context)
        {
            context.setResponse(message);
            context.notifyAll();
        }
    }
    
}
