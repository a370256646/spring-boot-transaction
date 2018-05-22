package com.example.demo.test;

import com.lc.transaction.common.mysql.model.DistributionTaskInvoke;
import com.lc.transaction.service.TransactionService;

/**
 * 事务 操作实例
 *
 * @author liucheng
 * @create 2018-05-15 21:00
 **/
public class Test extends TransactionService {

    @Override
    public DistributionTaskInvoke exec(Object... objects) throws Exception {
        DistributionTaskInvoke rlt = new DistributionTaskInvoke();
        System.out.println("test exec");
        return rlt;
    }
}
