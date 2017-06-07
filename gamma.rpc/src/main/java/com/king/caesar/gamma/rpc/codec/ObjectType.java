package com.king.caesar.gamma.rpc.codec;

/**
 * 对象类型
 * 
 * @author: Caesar
 * @date:   2017年5月29日 下午4:40:59
 */
public interface ObjectType
{
    byte NULL = -1;
    
    byte STRING = 0;
    
    byte LONG = 1;
    
    byte INTEGER = 2;
    
    byte SHORT = 3;
    
    byte DOUBLE = 4;
    
    byte FLOAT = 5;
    
    byte BOOLEAN = 6;
    
    byte BYTE = 7;
    
    byte OBJECT = 8;
    
    byte CHAR = 9;
}
