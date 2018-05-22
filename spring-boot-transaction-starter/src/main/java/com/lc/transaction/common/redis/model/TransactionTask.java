package com.lc.transaction.common.redis.model;

import com.lc.transaction.common.Topcs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 事务消息任务
 *
 * @author liucheng
 * @create 2018-05-11 18:00
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash
public class TransactionTask implements Serializable {

    @Id
    private Long id;
    /**
     * 发起者
     */
    @Indexed
    private String sponsor;
    /**
     * topc
     */
    @Indexed
    @Builder.Default
    private String topc = Topcs.TRANSACTION;
    /**
     * 事务主题
     */
    private String theme;
    /**
     * 执行参数
     */
    private Object[] parameters;
    /**
     * 预期参与者
     */
    private Set<String> expects;
    /**
     * 预期参与者人数
     */
    private Integer expectNum;
    /**
     * 创建时间
     */
    private Date createAt;
}
