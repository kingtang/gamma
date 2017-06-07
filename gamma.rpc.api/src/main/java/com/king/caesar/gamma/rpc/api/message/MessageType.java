package com.king.caesar.gamma.rpc.api.message;

public enum MessageType
{
    REQUEST((byte)0x01), RESPONSE((byte)0x02), HEARTBEAT_REQ((byte)0x03), HEARTBEAT_RSP((byte)0x04),EXCEPTION((byte)0x05);
    private byte value;
    
    MessageType(byte value)
    {
        this.value = value;
    }
    
    public byte originalValue()
    {
        return value;
    }
    
    public static MessageType valueOf(byte type)
    {
        switch (type)
        {
            case (byte)0x01:
                return MessageType.REQUEST;
            case (byte)0x02:
                return MessageType.RESPONSE;
            case (byte)0x03:
                return MessageType.HEARTBEAT_REQ;
            case (byte)0x04:
                return MessageType.HEARTBEAT_RSP;
            case (byte)0x05:
                return MessageType.EXCEPTION;
            default:
                return MessageType.HEARTBEAT_REQ;
        }
    }
}
