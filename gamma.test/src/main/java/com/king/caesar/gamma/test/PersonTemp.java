package com.king.caesar.gamma.test;

public class PersonTemp
{
    private String id;
    
    private int age;
    
    private StudentTemp temp;
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public int getAge()
    {
        return age;
    }
    
    public void setAge(int age)
    {
        this.age = age;
    }

    public StudentTemp getTemp()
    {
        return temp;
    }

    public void setTemp(StudentTemp temp)
    {
        this.temp = temp;
    }

    @Override
    public String toString()
    {
        return "PersonTemp [id=" + id + ", age=" + age + ", temp=" + temp + "]";
    }
    
}
