package com.king.caesar.gamma.test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class GenericType
{
    public static void main(String[] args)
    {
        Map<String,String> map = new HashMap<String,String>();
        TypeVariable<?>[] typeParameters = map.getClass().getTypeParameters();
        TypeVariable<?> typeVariable = typeParameters[0];
        System.out.println(typeVariable.getName());
        System.out.println(typeVariable instanceof ParameterizedType);
        
        System.out.println(map.keySet().getClass().getComponentType());
        System.out.println(map.keySet().getClass().getSigners());
        System.out.println(map.keySet().getClass().getGenericInterfaces());
        Type[] type = map.keySet().getClass().getGenericInterfaces();
        System.out.println(type.length);
        Type genericSuperclass = map.keySet().getClass().getGenericSuperclass();
        System.out.println(genericSuperclass);
        System.out.println(genericSuperclass instanceof ParameterizedType);
        ParameterizedType pType = (ParameterizedType)genericSuperclass;
        Type t = pType.getActualTypeArguments()[0];
        System.out.println(t);
        System.out.println(pType.getRawType());
        System.out.println(pType.getOwnerType());
    }
}
