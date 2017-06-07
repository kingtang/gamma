package com.king.caesar.gamma.rpc.codec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import static com.king.caesar.gamma.core.constants.GammaConstants.Attachment.*;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;
import com.king.caesar.gamma.core.util.ContextHolder;
import com.king.caesar.gamma.rpc.api.buffer.BufferWrapper;
import com.king.caesar.gamma.rpc.api.codec.Codec;
import com.king.caesar.gamma.rpc.api.codec.serializer.Serializer;
import com.king.caesar.gamma.rpc.api.message.AttachmentObject;
import com.king.caesar.gamma.rpc.api.message.AttachmentType;
import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.api.message.MessageHeader;
import com.king.caesar.gamma.rpc.api.message.MessageType;
import com.king.caesar.gamma.rpc.codec.buffer.NettyBufferWrapper;
import com.king.caesar.gamma.rpc.codec.serializer.ProtostuffSerializer;
import com.king.caesar.gamma.rpc.message.MessageImpl;
import com.king.caesar.gamma.rpc.util.SerializerFactory;

/**
 * Gamma协议编解码
 * 
 * @author: Caesar
 * @date: 2017年5月29日 下午5:26:36
 */
public class GammaCodec implements Codec<Message>
{
    
    private static final Codec<Message> INSTANCE = new GammaCodec();
    
    private GammaCodec()
    {
    }
    
    /**
     * CodecUtil已经读取到了消息长度字段
     */
    @Override
    public Message doDecode(BufferWrapper buffer, Message msg)
        throws IOException
    {
        // 读取消息长度
        int length = buffer.readInt();
        if (length != buffer.readableBytes())
        {
            throw new GammaException(Result.Code.MESSAGEBYTES_ILLEGAL,
                "Length field and readable bytes size not match.");
        }
        String serviceName = buffer.readString();
        String operationName = buffer.readString();
        String group = buffer.readString();
        String version = buffer.readString();
        
        long reqId = buffer.readLong();
        byte serialType = buffer.readByte();
        // 解码payload
        Serializer serializer = getSerializer(serialType);
        if (MessageType.REQUEST == msg.getMessageType())// 解码客户端请求
        {
            Object[] params = buffer.readObjectsDirect(serializer);
            msg.setPayload(params);
        }
        else if (MessageType.RESPONSE == msg.getMessageType())// 解码服务端响应
        {
            Object response = buffer.readObjectDirect(serializer);
            msg.setPayload(response);
        }
        msg.getMessageHeader().setService(serviceName);
        msg.getMessageHeader().setOperation(operationName);
        msg.getMessageHeader().setGroup(group);
        msg.getMessageHeader().setVersion(version);
        msg.getMessageHeader().addAttachment(REQUESTID, new AttachmentObject(AttachmentType.REMOTE_IMPLICIT, reqId));
        msg.getMessageHeader().addAttachment(SERIALIZETYPE,
            new AttachmentObject(AttachmentType.REMOTE_IMPLICIT, serialType));
        
        // 读取扩展字段
        Map<String, AttachmentObject> attachments = buffer.readMap(AttachmentObject.class, serializer);
        msg.getMessageHeader().addAttachments(attachments);
        return msg;
    }
    
    /**
     * 编码
     */
    @Override
    public BufferWrapper doEncode(Message msg)
    {
        if (msg instanceof MessageImpl)
        {
            MessageImpl msgImpl = (MessageImpl)msg;
            // 开辟256B内存空间
            BufferWrapper buffer = NettyBufferWrapper.allocate();
            // 2字节魔数
            buffer.writeShort(MAGIC);
            // 1字节消息类型
            buffer.writeByte(msgImpl.getMessageType().originalValue());
            // 1字节版本号
            buffer.writeByte(VERSION);
            // 消息长度，不包括魔数和该字段本身的长度，此处先占用
            buffer.writeInt(0);
            // serviceName
            MessageHeader messageHeader = msgImpl.getMessageHeader();
            String service = messageHeader.getService();
            buffer.writeString(service);
            // operationName
            String operation = messageHeader.getOperation();
            buffer.writeString(operation);
            // group
            String group = messageHeader.getGroup();
            buffer.writeString(group);
            // version
            String version = messageHeader.getVersion();
            buffer.writeString(version);
            // requestId
            AttachmentObject requestId = (AttachmentObject)messageHeader.getAndRemove(REQUESTID);
            buffer.writeLong((Long)requestId.getValue());
            // 序列化方式
            AttachmentObject serializeType = (AttachmentObject)messageHeader.getAndRemove(SERIALIZETYPE);
            byte serilizerType =
                (null == serializeType.getValue() ? ProtostuffSerializer.PROTOSTUFF : (Byte)serializeType.getValue());
            // 写入序列化方式，默认是protostuff
            buffer.writeByte(serilizerType);
            // 请求参数更多的是复合类型
            Serializer serializer = getSerializer(serilizerType);
            // 写入payload，针对请求来说可能有多个，针对响应最多一个
            if (MessageType.REQUEST == msgImpl.getMessageType())// 客户端请求
            {
                Object[] params = (Object[])msgImpl.getPayload();
                buffer.writeObjectsDirect(params, serializer);
            }
            else if (MessageType.RESPONSE == msgImpl.getMessageType())// 服务端响应
            {
                Object response = msgImpl.getPayload();
                buffer.writeObjectDirect(response, serializer);
            }
            // 写入扩展字段，剔除本地参数
            Map<String, AttachmentObject> remote = getRemotedObject(messageHeader);
            buffer.writeMap(remote, AttachmentObject.class, serializer);
            // 写入消息长度
            buffer.setInt(4, buffer.readableBytes() - 8);
            return buffer;
        }
        else
        {
            BufferWrapper buffer = NettyBufferWrapper.allocate();
            buffer.writeBytes(msg.toString().getBytes());
            return buffer;
        }
    }
    
    private Serializer getSerializer(byte serilizerType)
    {
        SerializerFactory factory = (SerializerFactory)ContextHolder.getContext().getBean("serializer_factory");
        Serializer serializer = factory.getSerializer(serilizerType);
        return serializer;
    }
    
    private Map<String, AttachmentObject> getRemotedObject(MessageHeader messageHeader)
    {
        Map<String, AttachmentObject> attachments = messageHeader.getAttachments();
        Map<String, AttachmentObject> remote = new HashMap<String, AttachmentObject>();
        Iterator<Entry<String, AttachmentObject>> iterator = attachments.entrySet().iterator();
        while (iterator.hasNext())
        {
            Entry<String, AttachmentObject> next = iterator.next();
            if (AttachmentType.REMOTE_IMPLICIT == next.getValue().getType())
            {
                remote.put(next.getKey(), next.getValue());
            }
        }
        return remote;
    }
    
    public static Codec<Message> getInstance()
    {
        return INSTANCE;
    }
    
}
