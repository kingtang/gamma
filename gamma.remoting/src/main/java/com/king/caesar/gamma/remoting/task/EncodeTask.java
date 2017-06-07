package com.king.caesar.gamma.remoting.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.caesar.gamma.remoting.api.Connection;
import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.util.CodecUtil;

import io.netty.buffer.ByteBuf;

/**
 * 编码任务，可以指定独立的线程池完成耗时的编码任务
 * 
 * @author: Caesar
 * @date: 2017年5月28日 下午2:46:57
 */
public class EncodeTask implements Runnable
{
    private static final Logger log = LoggerFactory.getLogger(EncodeTask.class);
    
    private Message message;
    
    private Connection connection;
    
    public EncodeTask(Message message, Connection connection)
    {
        this.message = message;
        this.connection = connection;
    }
    
    @Override
    public void run()
    {
        try
        {
            // 编码
            ByteBuf encodedInfo = CodecUtil.encode(message);
            connection.getChannel().writeAndFlush(encodedInfo);
        }
        catch (Throwable e)
        {
            log.error("Send the msg error.", e);
        }
    }
    
}
