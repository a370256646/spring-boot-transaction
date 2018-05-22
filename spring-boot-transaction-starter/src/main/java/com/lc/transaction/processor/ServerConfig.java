package com.lc.transaction.processor;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 当前server的配置
 * @author liucheng
 * @create 2018-05-21 20:47
 **/
@Component
public class ServerConfig {
    @Getter
    @Value("${spring.application.name}")
    private String sponsor;
    @Getter
    @Value("${server.port}")
    private String port;

    /**
     * 获得服务器唯一标识
     *
     * @return
     */
    protected String getSossessor() throws Exception {
        return sponsor + port;
    }
}
