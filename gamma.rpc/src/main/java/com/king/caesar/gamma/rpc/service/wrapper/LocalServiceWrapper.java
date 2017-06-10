package com.king.caesar.gamma.rpc.service.wrapper;

import com.king.caesar.gamma.rpc.service.LocalService;

/**
 * 本地服务的包装类，所谓的本地服务也即同一个JVM进程内其他类提供的服务方法。
 * 通常同一个JVM进程内直接通过api调用，所以框架未提供JVM进程内调用的功能。
 * 
 * @author: Caesar
 * @date: 2017年6月10日 下午8:16:17
 */
public interface LocalServiceWrapper
{
    void addService(LocalService service);
    
    LocalService findService(LocalService.ServiceKey serviceKey);
}
