package com.king.caesar.gamma.core.util;

import static com.king.caesar.gamma.core.constants.Result.*;
import com.king.caesar.gamma.core.exception.GammaException;

/**
 * 断言，用于参数合法性检查
 * 
 * @author: Caesar
 * @date: 2017年4月20日 下午9:39:27
 */
public abstract class Assert
{
    // 非空校验
    public static void notNull(Object object, String message)
    {
        if (object == null)
        {
            throw new GammaException(Code.PARAM_NULL, String.format(CodeDesc.PARAM_NULL, message));
        }
    }
}
