package com.king.caesar.gamma.test;

import io.protostuff.GraphIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.runtime.RuntimeSchema;

public class Test
{
    public static void main(String[] args)
    {
        System.out.println(Student.class.getName());
        /*Student student = new Student();
        student.setName("aa");
        byte[] byteArray = GraphIOUtil.toByteArray(student, RuntimeSchema.getSchema(Student.class), LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        
        Student s2 =  new Student();
        
        GraphIOUtil.mergeFrom(byteArray, s2, RuntimeSchema.getSchema(Student.class));
        System.out.println(s2.getName());
        
        StudentTemp st2 = new StudentTemp();
        GraphIOUtil.mergeFrom(byteArray, st2, RuntimeSchema.getSchema(StudentTemp.class));
        
        System.out.println(st2.getAddress());*/
        Person person = new Person();
        person.setAge(1);
        person.setId("a");
        
        Student student = new Student();
        student.setName("haha");
        person.setStudent(student);
        
        byte[] byteArray2 = GraphIOUtil.toByteArray(person, RuntimeSchema.getSchema(Person.class), LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        
        PersonTemp temp = new PersonTemp();
        System.out.println(byteArray2.length);
        GraphIOUtil.mergeFrom(byteArray2, temp, RuntimeSchema.getSchema(PersonTemp.class));
        System.out.println(temp);
        
        ObjectPack pack = new ObjectPack();
        pack.setData(person);
        byte[] byteArray3 = GraphIOUtil.toByteArray(pack, RuntimeSchema.getSchema(ObjectPack.class), LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        System.out.println(byteArray3.length);
    }
}
