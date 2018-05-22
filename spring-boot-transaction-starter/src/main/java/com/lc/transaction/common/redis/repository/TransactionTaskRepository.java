package com.lc.transaction.common.redis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.lc.transaction.common.redis.model.TransactionTask;
import java.io.Serializable;

/**
 * @author liucheng
 * @create 2018-05-11 18:12
 **/
@Repository
public interface TransactionTaskRepository extends CrudRepository<TransactionTask, Serializable> {
}
