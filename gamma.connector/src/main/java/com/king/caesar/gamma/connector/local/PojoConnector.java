package com.king.caesar.gamma.connector.local;

import static com.king.caesar.gamma.core.constants.GammaConstants.Attachment.REQUESTID;

import java.util.HashMap;
import java.util.Map;

import com.king.caesar.gamma.connector.AbstractConnector;
import com.king.caesar.gamma.core.constants.GammaConstants;
import com.king.caesar.gamma.core.constants.GammaConstants.Attribute;
import com.king.caesar.gamma.engine.Engine;
import com.king.caesar.gamma.engine.EngineAware;
import com.king.caesar.gamma.rpc.api.context.Context;
import com.king.caesar.gamma.rpc.api.message.AttachmentObject;
import com.king.caesar.gamma.rpc.api.message.AttachmentType;
import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.api.message.MessageType;
import com.king.caesar.gamma.rpc.message.MessageImpl;
import com.king.caesar.gamma.rpc.service.LocalService;
import com.king.caesar.gamma.rpc.service.LocalService.ServiceKey;
import com.king.caesar.gamma.rpc.service.wrapper.LocalServiceWrapper;
import com.king.caesar.gamma.rpc.util.RequestId;

/**
 * Connector负责同步和异步
 * 
 * @author: Caesar
 * @date: 2017年4月24日 下午9:56:13
 */
public class PojoConnector extends AbstractConnector implements EngineAware, LocalServiceWrapper
{
    private Engine engine;
    
    private Map<ServiceKey, LocalService> localService = new HashMap<ServiceKey, LocalService>();
    
    @SuppressWarnings("unchecked")
    @Override
    protected <T> T doOnReceive(Context context)
    {
        // 设置流程方向
        context.setAttribute(Attribute.PROCESSTYPEKEY, Attribute.OUTPROCESS);
        context.setSrcConnector(this);
        engine.service(context);
        Message response = context.getResponse();
        return (T)response.getPayload();
    }
    
    @Override
    public void setEngine(Engine engine)
    {
        this.engine = engine;
    }
    
    @Override
    public Message createRequest()
    {
        MessageImpl request = new MessageImpl();
        request.setMessageType(MessageType.REQUEST);
        // 生成reqID
        AttachmentObject requestId = new AttachmentObject(AttachmentType.REMOTE_IMPLICIT, RequestId.get());
        request.getMessageHeader().addAttachment(REQUESTID, requestId);
        request.setSessionId((Long)requestId.getValue());
        return request;
    }
    
    @Override
    public String getProtocol()
    {
        return "pojo";
    }
    
    @Override
    public void addService(LocalService service)
    {
        ServiceKey key = new ServiceKey(service.getServiceName(), service.getTargetMethod().getName());
        localService.put(key, service);
    }
    
    @Override
    public Message createResponse(Message request)
    {
        MessageImpl response = new MessageImpl();
        response.setMessageType(MessageType.RESPONSE);
        response.getMessageHeader().setProtocol(GammaConstants.Protocal.POJO);
        Map<String, AttachmentObject> attachments = request.getMessageHeader().getAttachments();
        response.getMessageHeader().addAttachments(attachments);
        return response;
    }
    
    @Override
    public LocalService findService(ServiceKey serviceKey)
    {
        return localService.get(serviceKey);
    }
    
}
