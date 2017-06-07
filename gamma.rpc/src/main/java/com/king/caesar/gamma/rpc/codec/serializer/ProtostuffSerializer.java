package com.king.caesar.gamma.rpc.codec.serializer;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.king.caesar.gamma.core.exception.GammaException;
import com.king.caesar.gamma.rpc.api.codec.serializer.Serializer;

import io.protostuff.GraphIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.Schema;
import io.protostuff.StringMapSchema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * Protostuff序列化实现
 * <p>
 * 从性能层面来讲二进制通常会比文本快，其中谷歌的Protobuf不管从效率还是编码后的字节大小 表现的都算不错，但是Protobuf没办法做到运行时的序列化，需要配置文件提前编译。而面向java
 * 的Protostuff解决了这个问题，引入它的runtime包可以很方便的完成正反序列化。其核心为 runtimeschema，内置了很多不同的schema，而schema的获取比较耗时，通常我们会缓存起来
 * 不用每次都生成schema。同时Protostuff的tag注解使得它的兼容性也比较好。综上所述，框架 默认提供Protostuff的实现，当然也可以扩展这层的实现来提供其他的序列化方式。
 * <p>
 * 我们的微基准测试{@com.king.caesar.gamma.test.ProtostuffSerializerBenchmark}显示通过getSchema获取schema性能比较高，其内部已经缓存过了
 * 因此可以不用再做一层缓存，这样反而会慢一些。
 * <p>
 * 同样来自微基准测试的结果显示GraphIOUtil比ProtostuffIOUtil稍慢一些，这个可以预见，因为GraphIOUtil还会处理循环的场景
 * 作为一个框架我们总是假定会有循环的情况出现，因此内部选择了GraphIOUtil实现。
 * <p>
 * 至于用户对象的序列化，可以有两种做法：
 * <p>
 * 一种是用一个通用的ObjectPack包装用户的对象，这样序列化后的字节会增多大概40个字节，而且由于深了一层，序列化大概慢100ns。但是在构造对象的时候不需要反射调用，由于明确知道
 * 类型为ObjectPack可以直接通过new构造实例。 <code>
 *  public class ObjectPack
 *  {
 *      private Object value;
 *  }
 * </code>
 * <p>
 * 另外一种是不使用通用对象，而是写入对象的全路径，这样在反序列化的时候先获取类路径，然后反射得到class，相比直接new构造实例，反射大概慢15ns。
 * <p>
 * 我们采用第二种方式。
 * 
 * @author: Caesar
 * @date: 2017年5月28日 下午5:26:49
 */
public class ProtostuffSerializer implements Serializer
{
    public static final byte PROTOSTUFF = 1;
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public byte[] serialize(Object data)
        throws GammaException
    {
        if (null == data)
        {
            return null;
        }
        Schema schema = (Schema)RuntimeSchema.getSchema(data.getClass());
        return GraphIOUtil.toByteArray(data, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }
    
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz)
        throws GammaException
    {
        if (null == bytes)
        {
            return null;
        }
        Schema<T> schema = (Schema<T>)RuntimeSchema.getSchema(clazz);
        // 实例化
        T message = ConstructorAccess.getFromCache(clazz).newInstance();
        GraphIOUtil.mergeFrom(bytes, message, schema);
        return message;
    }
    
    @Override
    public byte getType()
    {
        return PROTOSTUFF;
    }
    
    /**
     * 序列化map对象，对于Protostuff来说并没有直接提供针对map的序列化schema，需要通过MapSchema构造。
     * 如果直接通过RuntimeSchema获取map的schema来序列化的话，map的元素将不会被序列化,因此需要特殊处理。 单独提供一个针对map对象序列化的方法，让业务决定使用哪种方式。
     * <p>
     * 不支持Map<Object,Objcet>其中的K和V不能为根类型，这跟Protostuff的实现机制有关，它不会将类路径序列化 因此反序列化的时候，如果有多个类型会有问题。
     */
    @Override
    public <V extends Object> byte[] serializeMap(Map<String, V> map, Class<V> valueClazz)
    {
        if (null == map || null == valueClazz)
        {
            return null;
        }
        return GraphIOUtil.toByteArray(map,
            new StringMapSchema<>(RuntimeSchema.getSchema(valueClazz)),
            LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }
    
    @Override
    public <V extends Object> Map<String, V> deserializeMap(byte[] bytes, Class<V> valueClazz)
        throws GammaException
    {
        Map<String, V> obj = new HashMap<String, V>();
        GraphIOUtil.mergeFrom(bytes, obj, new StringMapSchema<>(RuntimeSchema.getSchema(valueClazz)));
        return obj;
    }
}
