package com.king.caesar.gamma.rpc.pool;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;
import com.king.caesar.gamma.rpc.api.codec.Codec;
import com.king.caesar.gamma.rpc.api.message.MessageType;
import com.king.caesar.gamma.rpc.api.pool.Executor;

import io.netty.buffer.ByteBuf;

/**
 * 服务线程池，根据serviceName和operationName获取对应的线程池 以此做到不同的服务隔离，
 * 比如核心服务可能需要单独的线程池来避免其他的 非核心接口出问题后拖垮核心服务。 如果未配置则使用默认的线程池
 * 
 * @author: Caesar
 * @date: 2017年5月17日 下午7:28:04
 */
public class ServiceExecutor implements Executor
{
    public static final String SERVICE_DELIMITER = "#";
    
    public static final String DEFAULT_EXECUTOR = "DEFAULT_EXECUTOR";
    
    private Map<String, Map<String, ExecutorService>> threadPools = new HashMap<String, Map<String, ExecutorService>>();
    
    private ExecutorService defaultExecutor;
    
    // 处理服务端响应的线程池
    private ExecutorService responseProcessExecutor;
    
    // 消息头长度
    private static final int HEADER_LENGTH = 8;
    
    // String类型的字段长度
    private static final int STRINGWORD_LENGTH = 4;
    
    @Override
    public void execute(ByteBuf buffer, Runnable task)
    {
        // 读取magic数
        short magic = buffer.readShort();
        if (magic != Codec.MAGIC)
        {
            throw new GammaException(Result.Code.MESSAGE_ILLEGAL,
                String.format(Result.CodeDesc.MESSAGE_ILLEGAL, "magic"));
        }
        // 读取消息类型
        byte type = buffer.readByte();
        buffer.readerIndex(0);
        // 如果是请求消息
        if (MessageType.REQUEST == MessageType.valueOf(type))
        {
            int servcieNameLength = buffer.getInt(HEADER_LENGTH);
            byte[] bytes = new byte[servcieNameLength];
            buffer.getBytes(HEADER_LENGTH + STRINGWORD_LENGTH, bytes, 0, servcieNameLength);
            String serviceName = new String(bytes, Charset.forName("UTF-8"));
            int operationIdx = HEADER_LENGTH + STRINGWORD_LENGTH + bytes.length;
            int operationNameLength = buffer.getInt(operationIdx);
            bytes = new byte[operationNameLength];
            buffer.getBytes(operationIdx + STRINGWORD_LENGTH, bytes, 0, operationNameLength);
            String operationName = new String(bytes, Charset.forName("UTF-8"));
            
            Map<String, ExecutorService> methodExecutorMapping = threadPools.get(serviceName);
            // 未配置此服务对应的线程池，使用默认线程池
            if (null != methodExecutorMapping)
            {
                ExecutorService executorService = methodExecutorMapping.get(operationName);
                if (null != executorService)
                {
                    executorService.execute(task);
                    return;
                }
            }
        }
        // 非业务请求消息或者没找到对应线程池
        defaultExecutor.execute(task);
    }
    
    public void addExecutorService(String service, String operation, ExecutorService executorService)
    {
        Map<String, ExecutorService> executors = threadPools.get(service);
        if (null == executors)
        {
            executors = new HashMap<String, ExecutorService>();
            executors.put(operation, executorService);
            threadPools.put(service, executors);
        }
        else
        {
            executors.put(operation, executorService);
        }
    }
    
    public ExecutorService getDefaultExecutor()
    {
        return defaultExecutor;
    }
    
    public void setDefaultExecutor(ExecutorService defaultExecutor)
    {
        this.defaultExecutor = defaultExecutor;
    }
    
    @Override
    public void execute(Runnable task)
    {
        responseProcessExecutor.execute(task);
    }
    
    public void setResponseProcessExecutor(ExecutorService responseProcessExecutor)
    {
        this.responseProcessExecutor = responseProcessExecutor;
    }
    
}
