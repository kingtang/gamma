package com.king.caesar.gamma.remoting.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.remoting.Dispatcher;
import com.king.caesar.gamma.remoting.api.Connection;
import com.king.caesar.gamma.rpc.api.buffer.BufferWrapper;
import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.util.CodecUtil;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

/**
 * 解码任务
 * 
 * @author: Caesar
 * @date: 2017年5月14日 下午2:45:04
 */
public class DecodeTask implements Runnable
{
    private static final Logger log = LoggerFactory.getLogger("service_log");
    
    private BufferWrapper buffer;
    
    private Connection connection;
    
    public DecodeTask(BufferWrapper buffer, Connection connection)
    {
        this.buffer = buffer;
        this.connection = connection;
    }
    
    @Override
    public void run()
    {
        Message request = null;
        try
        {
            // 解码
            request = (Message)CodecUtil.decode(buffer);
            log.debug("received msg is {}", request);
            
            // 解码客户端请求或服务端响应，并分发给合适的处理器。
            Dispatcher.getInstance().dispatch(request, connection);
            
        }
        catch (Throwable e)
        {
            log.error("Decode or dispatch message error.", e);
        }
        finally
        {
            if (buffer.refCnt() != 0)
            {
                ReferenceCountUtil.safeRelease(buffer);
            }
        }
    }
    
    public ByteBuf getBuffer()
    {
        return buffer;
    }
    
    public void setBuffer(BufferWrapper buffer)
    {
        this.buffer = buffer;
    }
}
