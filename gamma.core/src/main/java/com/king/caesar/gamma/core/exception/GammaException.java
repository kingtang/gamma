package com.king.caesar.gamma.core.exception;

/**
 * 
 * @Description:自定义异常类
 * @author: Caesar
 * @date: 2017年4月16日 下午4:08:18
 */
public class GammaException extends RuntimeException
{
    private static final long serialVersionUID = 8395037885246742044L;
    
    // 错误码
    private String errorCode;
    
    // 错误描述
    private String errorDesc;
    
    // cause the cause
    private Throwable nestedException;
    
    public GammaException(String errorCode, String errorDesc)
    {
        super(errorDesc);
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }
    
    public GammaException(String errorCode, String errorDesc, Throwable nestedException)
    {
        super(errorDesc, nestedException);
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.nestedException = nestedException;
    }
    
    @Override
    public String toString()
    {
        return "gammaException [errorCode=" + errorCode + ", errorDesc=" + errorDesc + ", nestedException="
            + nestedException + "]";
    }
    
    public String getErrorCode()
    {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }
    
    public String getErrorDesc()
    {
        return errorDesc;
    }
    
    public void setErrorDesc(String errorDesc)
    {
        this.errorDesc = errorDesc;
    }
    
    public Throwable getNestedException()
    {
        return nestedException;
    }
    
    public void setNestedException(Throwable nestedException)
    {
        this.nestedException = nestedException;
    }
}
