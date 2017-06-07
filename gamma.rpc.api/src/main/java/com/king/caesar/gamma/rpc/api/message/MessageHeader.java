package com.king.caesar.gamma.rpc.api.message;

import java.util.HashMap;
import java.util.Map;

public class MessageHeader
{
    private String group;
    
    private String version;
    
    private String service;
    
    private String operation;
    
    private String srcAddress;
    
    private String destAddress;
    
    private String protocol;
    
    private Map<String, AttachmentObject> attachments = new HashMap<String, AttachmentObject>();
    
    public String getGroup()
    {
        return group;
    }
    
    public void setGroup(String group)
    {
        this.group = group;
    }
    
    public String getVersion()
    {
        return version;
    }
    
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public String getService()
    {
        return service;
    }
    
    public void setService(String service)
    {
        this.service = service;
    }
    
    public String getOperation()
    {
        return operation;
    }
    
    public void setOperation(String operation)
    {
        this.operation = operation;
    }
    
    public String getSrcAddress()
    {
        return srcAddress;
    }
    
    public void setSrcAddress(String srcAddress)
    {
        this.srcAddress = srcAddress;
    }
    
    public String getDestAddress()
    {
        return destAddress;
    }
    
    public void setDestAddress(String destAddress)
    {
        this.destAddress = destAddress;
    }
    
    public Map<String, AttachmentObject> getAttachments()
    {
        return attachments;
    }
    
    public Object getAndRemove(String key)
    {
        return attachments.remove(key);
    }
    
    public void setAttachments(Map<String, AttachmentObject> attachments)
    {
        this.attachments = attachments;
    }
    
    public void addAttachment(String key, AttachmentObject attachment)
    {
        attachments.put(key, attachment);
    }
    
    public void addAttachments(Map<String,AttachmentObject> attachmentAdded)
    {
        attachments.putAll(attachmentAdded);
    }
    
    public String getProtocol()
    {
        return protocol;
    }
    
    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }
}
