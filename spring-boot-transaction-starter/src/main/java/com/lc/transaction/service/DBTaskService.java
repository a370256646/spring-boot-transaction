package com.lc.transaction.service;

import com.lc.transaction.common.mysql.model.DBTransactionTask;
import com.lc.transaction.common.mysql.model.DistributionTaskInvoke;
import com.lc.transaction.common.redis.model.DistributionTask;
import com.lc.transaction.common.redis.model.TransactionTask;

/**
 * db操作的业务logs
 *
 * @author liucheng
 * @create 2018-05-22 08:37
 **/
public interface DBTaskService {
    /**
     * 保存任务
     *
     * @param transactionTask 任务
     */
    void seveTransactionTask(TransactionTask transactionTask);

    /**
     * 修改任务状态
     *
     * @param taskId 任务id
     * @param status 状态
     */
    void updateTransactionTask(Long taskId, Integer status);

    /**
     * 根据id获得一个任务
     *
     * @param taskId id
     * @return 任务对象  不存在等于null
     */
    DBTransactionTask getDBTransactionTask(Long taskId);

    /**
     * 任务异常保存
     *
     * @param taskId    任务id
     * @param errorText 异常消息
     */
    void updateTransactionTaskError(Long taskId, String errorText);

    /**
     * 保存子任务执行结果
     *
     * @param distributionTaskInvoke 子任务执行结果
     */
    void seveDistributionTaskInvoke(DistributionTaskInvoke distributionTaskInvoke);
}
