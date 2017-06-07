/*package com.king.caesar.gamma.test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.king.caesar.gamma.remoting.api.serializer.Serializer;
import com.king.caesar.gamma.remoting.serializer.ProtostuffSerializer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.protostuff.GraphIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ProtostuffSerializerBenchmark
{
    private Person person;
    
    private ObjectPack objectPack;
    
    private ConcurrentHashMap<Class, Schema> cache;
    
    private Serializer serializer;
    
    private Schema schema;
    
    public static void main(String[] args)
        throws Exception
    {
        Options opt = new OptionsBuilder().include(ProtostuffSerializerBenchmark.class.getSimpleName()).build();
        new Runner(opt).run();
    }
    
    @Setup(Level.Trial)
    public void prepare()
    {
        System.out.println("prepare");
        person = new Person();
        person.setAge(1);
        person.setId("a");
        
        Student student = new Student();
        student.setName("haha");
        person.setStudent(student);
        
        // init cahce
        cache = new ConcurrentHashMap<>();
        serializer = new ProtostuffSerializer();
        
        objectPack = new ObjectPack();
        objectPack.setData(person);
        
        schema = RuntimeSchema.getSchema(ObjectPack.class);
    }
    
    @Benchmark
    public void testSerializer()
    {
        GraphIOUtil.toByteArray(person, RuntimeSchema.getSchema(Person.class), LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }
    
    @Benchmark
    public void testSerializerUsePack()
    {
        GraphIOUtil.toByteArray(objectPack, RuntimeSchema.getSchema(ObjectPack.class), LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }
    
    @Benchmark
    public void testSerializerUsePack2()
    {
        GraphIOUtil.toByteArray(objectPack, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }
    
    @Benchmark
    public void testSerializerWithNettyBuffer()
    {
        ByteBuf buffer = PooledByteBufAllocator.DEFAULT.heapBuffer(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        GraphIOUtil.toByteArray(person, RuntimeSchema.getSchema(Person.class),LinkedBuffer.use(buffer.array()));
        buffer.release();
    }
    
    @Benchmark
    public void testSerializerWithMinBuffer()
    {
        GraphIOUtil.toByteArray(person, RuntimeSchema.getSchema(Person.class), LinkedBuffer.allocate(LinkedBuffer.MIN_BUFFER_SIZE));
    }
    
    @Benchmark
    public void testSerializer2()
    {
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        // createFrom
        Schema<Person> schema = RuntimeSchema.createFrom(Person.class);
        GraphIOUtil.toByteArray(person, schema, buffer);
    }
    
    @Benchmark
    public void testSerializerWithCacheSchema()
    {
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        
        Schema<Person> schema = cache.get(Person.class);
        if (null == schema)
        {
            schema = RuntimeSchema.createFrom(Person.class);
            cache.put(Person.class, schema);
        }
        GraphIOUtil.toByteArray(person, schema, buffer);
    }
    
    @Benchmark
    public void testSerializerWithUtil()
    {
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        Schema<Person> schema = RuntimeSchema.getSchema(Person.class);
        ProtostuffIOUtil.toByteArray(person, schema, buffer);
    }
    
    @Benchmark
    public void testSerializerWithCustom()
    {
        serializer.serialize(person);
    }
}
*/