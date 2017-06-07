package com.king.caesar.gamma.test;

import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
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

import com.esotericsoftware.reflectasm.ConstructorAccess;

/**
 * Benchmark                      Mode  Cnt   Score   Error  Units
 * InstanceBenchmark.areflect     avgt   10   7.498 ± 0.494  ns/op
 * InstanceBenchmark.bReflectAsm  avgt   10   7.324 ± 0.160  ns/op
 * InstanceBenchmark.jdkNew       avgt   10   0.333 ± 0.005  ns/op
 * InstanceBenchmark.obenSis      avgt   10  14.412 ± 0.303  ns/op
 * 
 * @author: Caesar
 * @date:   2017年5月29日 上午11:10:38
 */
@Fork(1)
@State(Scope.Thread)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class InstanceBenchmark
{
    private static final Objenesis OBJENESIS_STD = new ObjenesisStd();
    
    private ConstructorAccess<Person> constructorAccess;
    
    private ConcurrentHashMap<Class, ConstructorAccess> cache;
    private Charset utf;
    public static void main(String[] args)
        throws Exception
    {
        Options opt = new OptionsBuilder().include(InstanceBenchmark.class.getSimpleName()).build();
        new Runner(opt).run();
    }
    
    @Setup
    public void prepare()
    {
        System.out.println("aaa");
        cache = new ConcurrentHashMap<>();
        utf = Charset.forName("UTF-8");
    }
    
    @Benchmark
    public void aaCharset()
    {
        "a".getBytes(utf);
    }
    
    @Benchmark
    public void abCharset()
    {
        "a".getBytes(Charset.forName("UTF-8"));
    }
    
    /**
     * 直接反射效率并不低
     * @throws Exception
     */
    @Benchmark
    public void areflect()
        throws Exception
    {
        Person person = Person.class.newInstance();
    }
    
    @Benchmark
    public void bReflectAsm()
    {
        ConstructorAccess constructorAccess2 = cache.get(Person.class);
        if(null == constructorAccess2)
        {
            constructorAccess2 = ConstructorAccess.get(Person.class);
            cache.putIfAbsent(Person.class, constructorAccess2);
        }
        Person person = (Person)constructorAccess2.newInstance();
    }
    
    @Benchmark
    public void cReflectWithCache()
    {
        ConstructorAccess<Person> constructorAccess2 = ConstructorAccess.getFromCache(Person.class);
        Person person = (Person)constructorAccess2.newInstance();
    }
    
    @Benchmark
    public void obenSis()
    {
        Person person = OBJENESIS_STD.newInstance(Person.class);
    }
    
    @Benchmark
    public void jdkNew()
    {
        Person person = new Person();
    }
    
}
