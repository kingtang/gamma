package com.king.caesar.gamma.test;

import com.esotericsoftware.reflectasm.ConstructorAccess;

import io.protostuff.GraphIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.runtime.RuntimeSchema;

public class Test2
{
    public static void main(String[] args)
    {
        ObjectPack pack = new ObjectPack();
        Person p = new Person();
        Student object = new Student();
        p.setStudent(object);
        object.setName("aa");
        pack.setData(p);
        byte[] packBytes = GraphIOUtil.toByteArray(pack,
            RuntimeSchema.getSchema(ObjectPack.class),
            LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        System.out.println(packBytes.length);
        
        byte[] objectBytes = GraphIOUtil.toByteArray(p,
            RuntimeSchema.getSchema(Person.class),
            LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        System.out.println(objectBytes.length);
        
        ConstructorAccess constructorAccess2 = ConstructorAccess.get(Person.class);
        Person person = (Person)constructorAccess2.newInstance();
    }
}
