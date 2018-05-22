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
import java.util.Set;

/**
 * 持久化task
 *
 * @author liucheng
 * @create 2018-05-21 20:41
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "db_transaction_task", schema = "db_transaction")
@DynamicUpdate
@DynamicInsert
public class DBTransactionTask implements Serializable {

    /**
     * 手动主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "assigned")
    private Long id;
    /**
     * 发起者
     */
    @Column(name = "sponsor")
    private String sponsor;
    /**
     * topc
     */
    @Column(name = "topc")
    private String topc;
    /**
     * 事务主题
     */
    @Column(name = "theme")
    private String theme;
    /**
     * 执行参数
     */
    @Column(name = "parameters")
    private Object[] parameters;
    /**
     * 预期参与者
     */
    @Column(name = "expects")
    private Set<String> expects;
    /**
     * 预期参与者人数
     */
    @Column(name = "expect_num")
    private Integer expectNum;
    /**
     * 状态
     *
     * @see StatusEnum
     */
    @Column(name = "status")
    private Integer status;
    /**
     * 创建时间
     */
    @Column(name = "create_at")
    private Date createAt;
    /**
     * 修改时间
     */
    @Column(name = "update_at")
    private Date updateAt;
    /**
     * 执行结果
     */
    @Column(name = "error_text")
    private String errorText;

    /**
     * 任务状态
     */
    public enum StatusEnum {
        NOT_STARTED(0, "未发送"),
        FAILURE(1, "发送失败"),
        WAIT_CHECK(2, "发送完成待执行"),
        CHECK_SUCCEED(3, "执行成功"),
        CHECK_FAILURE(4, "执行失败");

        public String value;
        public Integer key;

        StatusEnum(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        public static String getName(Integer key) {
            for (StatusEnum c : StatusEnum.values()) {
                if (c.getKey() == key) {
                    return c.getValue();
                }
            }
            return null;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }
    }
}
