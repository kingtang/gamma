package com.king.caesar.gamma.rpc.codec;

import static org.junit.Assert.*;

import org.junit.Test;

import com.king.caesar.gamma.rpc.api.buffer.BufferWrapper;
import com.king.caesar.gamma.rpc.api.codec.Codec;
import com.king.caesar.gamma.rpc.api.message.MessageType;
import com.king.caesar.gamma.rpc.message.MessageImpl;
import com.king.caesar.gamma.rpc.util.CodecUtil;

public class GammaCodecTest
{
    
    @Test
    public void testHeartbeatEncode()
    {
        MessageImpl msg = new MessageImpl();
        msg.setMessageType(MessageType.HEARTBEAT_REQ);
        
        BufferWrapper encodedInfo = CodecUtil.encode(msg);
        short magic = encodedInfo.readShort();
        assertEquals(Codec.MAGIC, magic);
        
        byte msgType = encodedInfo.readByte();
        assertEquals(MessageType.HEARTBEAT_REQ.originalValue(), msgType);
    }
    
    @Test
    public void testHeartbeatDecode()
    {
    }
}
