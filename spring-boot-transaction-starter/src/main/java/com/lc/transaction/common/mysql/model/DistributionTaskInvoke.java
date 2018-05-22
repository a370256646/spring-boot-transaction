package com.lc.transaction.common.mysql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 单个任务执行结果，存储到数据库
 *
 * @author liucheng
 * @create 2018-05-16 10:30
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "distribution_task_invoke", schema = "db_transaction")
@DynamicUpdate
@DynamicInsert
public class DistributionTaskInvoke implements Serializable {

    /**
     * 主键，自增
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * distributionTask的id
     */
    @Column(name = "distribution_task_id")
    private Long distributionTaskId;
    /**
     * TransactionTask的id
     */
    @Column(name = "task_id")
    private Long taskId;
    /**
     * 所有者
     */
    @Column(name = "possessor")
    private String possessor;
    /**
     * 执行结果 成功/失败
     */
    @Column(name = "succeed")
    private Boolean succeed;

    /**
     * 执行结果
     */
    @Column(name = "text")
    private String text;
    /**
     * 创建时间
     */
    @Column(name = "create_at")
    @Builder.Default
    private Date createAt=new Date();
}
