package com.king.caesar.gamma.engine.pipeline;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.king.caesar.gamma.handler.AbstractHandler;
import com.king.caesar.gamma.pipeline.DefaultPipeline;
import com.king.caesar.gamma.pipeline.Pipeline;
import com.king.caesar.gamma.rpc.api.context.Connector;
import com.king.caesar.gamma.rpc.api.context.Context;
import com.king.caesar.gamma.rpc.api.message.Message;
import com.king.caesar.gamma.rpc.api.message.MessageHeader;
import com.king.caesar.gamma.rpc.api.message.MessageType;

public class PipelineTest
{
    @Test
    public void testAddFirst()
    {
        Pipeline pipeline = new DefaultPipeline();
        Context context = createContext();
        
        pipeline.addFirst("handler1", new AbstractHandler()
        {
            
            @Override
            public void handle(Context context)
            {
                Message request = context.getRequest();
                List<String> payload = (List<String>)request.getPayload();
                assertEquals(1, payload.size());
                payload.add("a");
            }
        });
        
        pipeline.addFirst("handler2", new AbstractHandler()
        {
            
            @Override
            public void handle(Context context)
            {
                Message request = context.getRequest();
                List<String> payload = (List<String>)request.getPayload();
                assertEquals(0, payload.size());
                payload.add("b");
            }
        });
        
        pipeline.fireProcess(context);
        List<String> payload = (List<String>)context.getRequest().getPayload();
        List<String> expected = new ArrayList<String>();
        expected.add("b");
        expected.add("a");
        assertEquals(expected, payload);
    }
    @Test
    public void testAddLast()
    {
        Pipeline pipeline = new DefaultPipeline();
        Context context = createContext();
        pipeline.addLast("handler1", new AbstractHandler()
        {
            
            @Override
            public void handle(Context context)
            {
                Message request = context.getRequest();
                List<String> payload = (List<String>)request.getPayload();
                assertEquals(0, payload.size());
                payload.add("a");
            }
        });
        
        pipeline.addLast("handler2", new AbstractHandler()
        {
            
            @Override
            public void handle(Context context)
            {
                Message request = context.getRequest();
                List<String> payload = (List<String>)request.getPayload();
                assertEquals(1, payload.size());
                payload.add("b");
            }
        });
        
        pipeline.fireProcess(context);
        List<String> payload = (List<String>)context.getRequest().getPayload();
        List<String> expected = new ArrayList<String>();
        expected.add("a");
        expected.add("b");
        assertEquals(expected, payload);
    }

    private Context createContext()
    {
        Context context = new Context()
        {
            Message msg = new Message()
            {
                List<String> payload = new ArrayList<String>();
                @Override
                public void setPayload(Object payload)
                {
                    
                }
                
                @Override
                public void setMessageHeader(MessageHeader msgHeader)
                {
                    
                }
                
                @Override
                public Object getPayload()
                {
                    return payload;
                }
                
                @Override
                public MessageHeader getMessageHeader()
                {
                    return null;
                }

                @Override
                public MessageType getMessageType()
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public void setMessageType(MessageType type)
                {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public Long getSessionId()
                {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public void setSessionId(Long sessionId)
                {
                    // TODO Auto-generated method stub
                    
                }
            };
            
            @Override
            public void setResponse(Message response)
            {
                
            }
            
            @Override
            public void setRequest(Message request)
            {
                
            }
            
            @Override
            public Message getResponse()
            {
                return null;
            }
            
            @Override
            public Message getRequest()
            {
                return msg;
            }

            @Override
            public Connector getSrcConnector()
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Connector getDestConnector()
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setSrcConnector(Connector srcConnector)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void setDestConnector(Connector destConnector)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public Object getAttribute(String key)
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setAttribute(String key, Object value)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public int getTimeOutMs()
            {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public void setTimeOutMs(int timeOut)
            {
                // TODO Auto-generated method stub
                
            }
        };
        return context;
    }
    
}
