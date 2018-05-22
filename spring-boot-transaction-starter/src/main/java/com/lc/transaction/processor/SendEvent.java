package com.lc.transaction.processor;

import com.lc.transaction.common.mysql.model.DBTransactionTask;
import com.lc.transaction.common.redis.model.TransactionTask;
import com.lc.transaction.common.redis.repository.TransactionTaskRepository;
import com.lc.transaction.common.Topcs;
import com.lc.transaction.service.DBTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 发送事件
 *
 * @author liucheng
 * @create 2018-05-11 17:57
 **/
@Slf4j
@Component
@EnableAsync
public class SendEvent {
    @Autowired
    KafkaTemplate kafkaTemplate;
    @Autowired
    TransactionTaskRepository transactionTaskRepository;
    @Autowired
    ServerConfig serverConfig;
    @Autowired
    DBTaskService dBTaskService;


    /**
     * 发送消息
     *
     * @param top     top发送地址
     * @param content 发送内容
     */
    @Async
    public void send(String top, String content) {
        kafkaTemplate.send(top, content);
    }

    /**
     * 发送事务消息
     *
     * @param theme      要处理的事件主题
     * @param parameters 执行参数
     * @param expects    要参与的人名称
     */
    @Async
    public void send(String theme, Object[] parameters, Set<String> expects) {
        TransactionTask transactionTask = null;
        try {
            transactionTask = transactionTaskRepository.save(TransactionTask.builder()
                    .sponsor(serverConfig.getSossessor())
                    .theme(theme)
                    .parameters(parameters)
                    .expects(expects)
                    .expectNum(expects.size())
                    .build());
            dBTaskService.seveTransactionTask(transactionTask);
            send(Topcs.TRANSACTION, transactionTask.getId().toString());
            dBTaskService.updateTransactionTask(transactionTask.getId(), DBTransactionTask.StatusEnum.WAIT_CHECK.key);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("kafka消息发送失败：theme=" + theme + " |parameters=" + parameters.toString() +
                    " |expects=" + expects.toString(), e);
            dBTaskService.updateTransactionTaskError(transactionTask.getId(), e.getStackTrace().toString());
        }
    }


}