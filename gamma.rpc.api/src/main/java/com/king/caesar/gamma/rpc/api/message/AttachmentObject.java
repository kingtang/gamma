package com.king.caesar.gamma.rpc.api.message;

/**
 * 通常远程传输的扩展字段不需要有丰富的类型，大多数情况下可能都为基础类型，但是为了框架的扩展性这里定义为Object。
 * 这就需要在序列化的时候写入类的全路径，这样的话字节会有浪费，一个折中的做法是，两边约定基础类型的枚举，判断Object的类型然后做相应的序列化。例如： <code>
 * if(value instanceOf String)
 * {
 *      writeString();
 * }
 * else if(value instanceOf Long)
 * {
 *      writeLong();
 * }
 * ...
 * </code>
 * 
 * @author: Caesar
 * @date: 2017年5月29日 上午11:46:43
 */
public class AttachmentObject
{
    private AttachmentType type;
    
    private Object value;
    
    public AttachmentObject(AttachmentType type, Object value)
    {
        this.type = type;
        this.value = value;
    }
    
    public AttachmentType getType()
    {
        return type;
    }
    
    public void setType(AttachmentType type)
    {
        this.type = type;
    }
    
    public Object getValue()
    {
        return value;
    }
    
    public void setValue(Object value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "AttachmentObject [type=" + type + ", value=" + value + "]";
    }
}
