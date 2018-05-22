package com.example.demo.test;


import com.lc.transaction.service.TransactionHandleRegisterImpl;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置参与者名称与处理实例
 *
 * @author liucheng
 * @create 2018-05-18 17:17
 **/
@Component
@Order
public class TransactionHandleRegister extends TransactionHandleRegisterImpl implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Class> handleRegisterMap = new HashMap<>();
        //增加一个参与者  test，参与操作实例 Test.class
        handleRegisterMap.put("test", Test.class);
        super.init(handleRegisterMap);
    }
}
