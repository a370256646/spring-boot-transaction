package com.lc.transaction.common.redis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.lc.transaction.common.redis.model.DistributionTask;

import java.io.Serializable;
import java.util.List;

/**
 * @author liucheng
 * @create 2018-05-11 18:12
 **/
@Repository
public interface DistributionTaskRepository extends CrudRepository<DistributionTask, Serializable> {

    /**
     * 查询同一个task下相同的参与者锁
     *
     * @param taskId    任务id
     * @param possessor 参与者
     * @return 返回锁列表
     */
    List<DistributionTask> findByTaskIdAndPossessorOrderByIdAsc(Long taskId, String possessor);
}
