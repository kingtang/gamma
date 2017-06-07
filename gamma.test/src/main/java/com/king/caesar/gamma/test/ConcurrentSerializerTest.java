package com.king.caesar.gamma.test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

import io.protostuff.GraphIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ConcurrentSerializerTest
{
    private Person person;
    
    private CopyOnWriteMap<Class, Schema> cache;
    
    private ExecutorService executor1;
    
    private ClassLoader loader;
    
    public static void main(String[] args)
        throws Exception
    {
        Options opt = new OptionsBuilder().include(ConcurrentSerializerTest.class.getSimpleName()).build();
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
        cache = new CopyOnWriteMap<>();
        
        executor1 = Executors.newFixedThreadPool(40);
        loader = this.getClass().getClassLoader();
    }
    
    /**
     * 40个线程
     * @throws ClassNotFoundException 
     */
   /* @Benchmark
    public void test40()
    {
        executor1.execute(new SerializerTask(cache, person));
    }*/
    
    @Benchmark
    public void forname() throws ClassNotFoundException
    {
        Class.forName("com.king.caesar.gamma.test.Person");
    }
    
    @Benchmark
    public void reflectGetClass() throws ClassNotFoundException
    {
        loader.loadClass("com.king.caesar.gamma.test.Person");
    }
    
    public static class SerializerTask implements Runnable
    {
        private Map<Class, Schema> cache;
        private Person person;
        public SerializerTask(Map<Class, Schema> cache,Person person)
        {
            this.cache = cache;
            this.person = person;
        }
        
        @Override
        public void run()
        {
            serialization();
        }
        private void serialization()
        {
            long start = System.nanoTime();
            LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
            Schema<Person> schema = cache.get(Person.class);
            if (null == schema)
            {
                schema = RuntimeSchema.createFrom(Person.class);
                cache.put(Person.class, schema);
            }
            GraphIOUtil.toByteArray(person, schema, buffer);
            System.out.println(System.nanoTime() - start);
        }
    }
}
