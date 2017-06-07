package com.king.caesar.gamma.engine;

import static com.king.caesar.gamma.core.constants.GammaConstants.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.king.caesar.gamma.core.constants.Result;
import com.king.caesar.gamma.core.exception.GammaException;
import com.king.caesar.gamma.core.util.ContextHolder;
import com.king.caesar.gamma.core.util.StringUtils;
import com.king.caesar.gamma.handler.Handler;
import com.king.caesar.gamma.pipeline.DefaultPipeline;
import com.king.caesar.gamma.pipeline.Pipeline;
import com.king.caesar.gamma.rpc.api.context.Connector;
import com.king.caesar.gamma.rpc.api.context.Context;
import com.king.caesar.gamma.spring.api.servicebean.HandlerBeanDefinition;

/**
 * RPC服务引擎
 * 
 * @author: Caesar
 * @date: 2017年4月23日 下午4:45:43
 */
public class ServiceEngine extends AbstractEngine
{
    private Pipeline pipeline;
    
    private Map<String, Connector> connectorMapper;
    
    @Override
    public void init()
        throws GammaException
    {
        // 父类先初始化
        super.init();
        
        // 初始化connector
        Map<String, Connector> connectors = ContextHolder.getContext().getBeansOfType(Connector.class);
        Collection<Connector> connectorList = connectors.values();
        for (Connector connector : connectorList)
        {
            if (connector instanceof EngineAware)
            {
                ((EngineAware)connector).setEngine(this);
            }
        }
        connectorMapper = connectors;
        
        // 初始化pipeline
        pipeline = new DefaultPipeline();
        Map<String, HandlerBeanDefinition> chainNodes =
            ContextHolder.getContext().getBeansOfType(HandlerBeanDefinition.class);
        if (null == chainNodes || chainNodes.size() < 1)
        {
            throw new GammaException(Result.Code.FRAMEWORK_INITERROR, "Can not find handler bean definition.");
        }
        
        // 获取头结点信息
        HandlerBeanDefinition header = getHeader(chainNodes);
        if (null == header)
        {
            throw new GammaException(Result.Code.FRAMEWORK_INITERROR,
                "Can not find handler bean definition of header.");
        }
        pipeline.addLast(header.getName(), ContextHolder.getContext().getBean(header.getHandler(), Handler.class));
        // 递归增加Handler
        addHandler(pipeline, header, chainNodes);
    }
    
    private void addHandler(Pipeline pipeline, HandlerBeanDefinition currentNode,
        Map<String, HandlerBeanDefinition> chainNodes)
    {
        // 获取下一节点的名称
        String nextNodeName = currentNode.getNextName();
        HandlerBeanDefinition nextNode = null;
        // 未指定下一个节点
        if (null == nextNodeName || 0 == nextNodeName.length())
        {
            // 获取当前节点的名称
            String currentName = currentNode.getName();
            // 获取pre节点为当前节点的节点
            nextNode = getNodeWithPre(currentName, chainNodes);
            // 当前已经为尾节点
            if (null == nextNode)
            {
                return;
            }
        }
        else
        {
            nextNode = chainNodes.get(nextNodeName);
        }
        pipeline.addLast(nextNode.getName(), ContextHolder.getContext().getBean(nextNode.getHandler(), Handler.class));
        addHandler(pipeline, nextNode, chainNodes);
    }
    
    // 获取前驱为currentName的节点
    private HandlerBeanDefinition getNodeWithPre(String currentName, Map<String, HandlerBeanDefinition> chainNodes)
    {
        Iterator<Entry<String, HandlerBeanDefinition>> iterator = chainNodes.entrySet().iterator();
        while (iterator.hasNext())
        {
            Entry<String, HandlerBeanDefinition> next = iterator.next();
            HandlerBeanDefinition chainNode = next.getValue();
            if (!StringUtils.isEmpty(chainNode.getPreName()) && chainNode.getPreName().equals(currentName))
            {
                return chainNode;
            }
        }
        return null;
    }
    
    // 获取pipeline的头结点
    private HandlerBeanDefinition getHeader(Map<String, HandlerBeanDefinition> chainNodes)
    {
        Iterator<Entry<String, HandlerBeanDefinition>> iterator = chainNodes.entrySet().iterator();
        while (iterator.hasNext())
        {
            Entry<String, HandlerBeanDefinition> next = iterator.next();
            HandlerBeanDefinition chainNode = next.getValue();
            if (chainNode.isHead())
            {
                return chainNode;
            }
        }
        return null;
    }
    
    @Override
    public Pipeline getPipeline()
    {
        return pipeline;
    }
    
    /**
     * RPC请求由该类处理
     */
    @Override
    public void service(Context context)
    {
        // 获取destConnector
        Connector srcConnector = context.getSrcConnector();
        Connector destConnector = getDestConnector(srcConnector.getProtocol());
        context.setDestConnector(destConnector);
        pipeline.fireProcess(context);
    }
    
    private Connector getDestConnector(String protocal)
    {
        if (Protocal.GAMMA.equals(protocal))
        {
            return connectorMapper.get("pojoConnector");
        }
        else if (Protocal.POJO.equals(protocal))
        {
            return connectorMapper.get("gammaConnector");
        }
        throw new GammaException(Result.Code.DEST_CONNECTOR_NOT_FOUND, "Can not find dest connector.");
    }
    
    @Override
    public void setPipeline(Pipeline pipeline)
    {
        this.pipeline = pipeline;
    }
    
}
