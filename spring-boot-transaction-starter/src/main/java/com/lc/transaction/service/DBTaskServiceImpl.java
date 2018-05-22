package com.lc.transaction.service;

import com.lc.transaction.common.mysql.model.DBTransactionTask;
import com.lc.transaction.common.mysql.model.DistributionTaskInvoke;
import com.lc.transaction.common.mysql.repository.DBTransactionTaskRepository;
import com.lc.transaction.common.mysql.repository.DistributionTaskInvokeRepository;
import com.lc.transaction.common.redis.model.TransactionTask;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * db实现
 *
 * @author liucheng
 * @create 2018-05-22 08:41
 **/
@Service
public class DBTaskServiceImpl implements DBTaskService {

    @Autowired
    DistributionTaskInvokeRepository distributionTaskInvokeRepository;
    @Autowired
    DBTransactionTaskRepository dBTransactionTaskRepository;

    @Override
    public void seveTransactionTask(TransactionTask transactionTask) {
        DBTransactionTask dbTransactionTask = new DBTransactionTask();
        BeanUtils.copyProperties(transactionTask, dbTransactionTask);
        dbTransactionTask.setUpdateAt(new Date());
        dbTransactionTask.setStatus(DBTransactionTask.StatusEnum.NOT_STARTED.key);
        dBTransactionTaskRepository.save(dbTransactionTask);
    }

    @Override
    public void updateTransactionTask(Long taskId, Integer status) {
        DBTransactionTask dbTransactionTask = getDBTransactionTask(taskId);
        if (dbTransactionTask == null) {
            return;
        }
        dbTransactionTask.setStatus(status);
        dBTransactionTaskRepository.save(dbTransactionTask);
    }

    @Override
    public DBTransactionTask getDBTransactionTask(Long taskId) {
        DBTransactionTask dbTransactionTask = dBTransactionTaskRepository.getOne(taskId);
        if (dbTransactionTask == null || dbTransactionTask.getStatus() == null) {
            return null;
        }
        if (dbTransactionTask.getStatus().equals(DBTransactionTask.StatusEnum.CHECK_SUCCEED.key)) {
            return null;
        }
        return dbTransactionTask;
    }

    @Override
    public void updateTransactionTaskError(Long taskId, String errorText) {
        DBTransactionTask dbTransactionTask = getDBTransactionTask(taskId);
        if (dbTransactionTask == null) {
            return;
        }
        dbTransactionTask.setStatus(DBTransactionTask.StatusEnum.FAILURE.key);
        dbTransactionTask.setErrorText(errorText);
        dBTransactionTaskRepository.save(dbTransactionTask);
    }

    @Override
    public void seveDistributionTaskInvoke(DistributionTaskInvoke distributionTaskInvoke) {
        distributionTaskInvokeRepository.save(distributionTaskInvoke);
    }
}
