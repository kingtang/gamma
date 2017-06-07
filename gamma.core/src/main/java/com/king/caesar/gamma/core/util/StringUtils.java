package com.king.caesar.gamma.core.util;

/**
 * 常用的字符串操作
 * 
 * @author: Caesar
 * @date: 2017年4月23日 下午5:17:03
 */
public abstract class StringUtils
{
    /**
     * 去掉字符串两边的空格，如果传入的原字符串是空，则返回默认值
     * 
     * @param original
     * @param defaultValue
     * @return
     */
    public static String trimAndGet(String original, String defaultValue)
    {
        if (isEmpty(original))
        {
            return defaultValue;
        }
        return original.trim();
    }
    
    /**
     * 判断字符串是否为空，传入的为null对象或者trim后的值长度为0则返回true 其他情况返回false
     * 
     * @param value
     * @return
     */
    public static boolean isEmpty(String value)
    {
        return (null == value || value.trim().length() < 1);
    }
    
    /**
     * 多个对象的hash code
     * 
     * @param objects
     * @return
     */
    public static int hashCode(Object... objects)
    {
        int result = 1;
        
        if (objects != null)
        {
            for (final Object obj : objects)
            {
                result = 31 * result + (obj == null ? 0 : obj.hashCode());
            }
        }
        
        return result;
    }
}
