package com.lc.transaction.service;


/**
 * 注册处理实例
 *
 * @author liucheng
 * @create 2018-05-18 17:30
 **/
public interface ConfigService {
    /**
     * 获得一个处理class
     *
     * @param key key
     * @return
     */
    Class getClass(String key);
}
