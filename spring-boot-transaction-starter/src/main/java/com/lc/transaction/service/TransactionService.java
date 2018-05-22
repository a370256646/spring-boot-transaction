package com.lc.transaction.service;

import com.lc.transaction.common.mysql.model.DistributionTaskInvoke;

/**
 * @author liucheng
 * @create 2018-05-15 20:53
 **/
public abstract class TransactionService {

    public TransactionService() {
    }

    public DistributionTaskInvoke execute(Object... objects) {
        DistributionTaskInvoke rlt;
        try {
            System.out.println("TransactionService exec");
            rlt = this.exec(objects);
            rlt.setSucceed(true);
        } catch (Exception e) {
            rlt = new DistributionTaskInvoke();
            rlt.setSucceed(false);
            rlt.setText(e.getStackTrace().toString());
        }
        return rlt;
    }

    public abstract DistributionTaskInvoke exec(Object... objects) throws Exception;
}
