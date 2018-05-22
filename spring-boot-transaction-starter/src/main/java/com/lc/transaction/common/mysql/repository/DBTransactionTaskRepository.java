package com.lc.transaction.common.mysql.repository;
import com.lc.transaction.common.mysql.model.DBTransactionTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
/**
 * @author liucheng
 * @create 2018-05-22 09:02
 **/
@Repository
public interface DBTransactionTaskRepository extends JpaRepository<DBTransactionTask, Serializable>
        , JpaSpecificationExecutor<DBTransactionTask> {
}
