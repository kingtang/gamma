package com.king.caesar.gamma.registry.zookeeper.listener;

import com.king.caesar.gamma.rpc.api.service.Service;

/**
 * 服务监听器，提供
 * <p>服务新增回调</p>
 * 
 * @author: Caesar
 * @date:   2017年6月10日 下午6:36:03
 */
public interface ServiceListener
{
    void serviceAdded(Service addedService,String servicePath);
    
    void serviceDeleted(Service deletedService,String servicePath);
    
    void serviceUpdated(Service updatedService,String servicePath);
}
