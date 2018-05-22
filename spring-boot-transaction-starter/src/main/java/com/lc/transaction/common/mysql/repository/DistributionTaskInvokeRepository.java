package com.lc.transaction.common.mysql.repository;
import com.lc.transaction.common.mysql.model.DistributionTaskInvoke;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
/**
 * @author liucheng
 * @create 2018-05-22 09:02
 **/
@Repository
public interface DistributionTaskInvokeRepository extends JpaRepository<DistributionTaskInvoke, Serializable>
        , JpaSpecificationExecutor<DistributionTaskInvoke> {
}
