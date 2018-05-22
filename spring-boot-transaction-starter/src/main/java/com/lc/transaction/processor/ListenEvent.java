package com.lc.transaction.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.transaction.common.Topcs;
import com.lc.transaction.common.mysql.model.DistributionTaskInvoke;
import com.lc.transaction.common.redis.model.DistributionTask;
import com.lc.transaction.common.redis.model.TransactionTask;
import com.lc.transaction.common.redis.repository.DistributionTaskRepository;
import com.lc.transaction.common.redis.repository.TransactionTaskRepository;
import com.lc.transaction.reflex.BeanInvoke;
import com.lc.transaction.service.ConfigService;
import com.lc.transaction.service.DBTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 消息监听
 *
 * @author liucheng
 * @create 2018-05-11 17:32
 **/
@Slf4j
@Component
@EnableAsync
public class ListenEvent {
    @Autowired
    TransactionTaskRepository transactionTaskRepository;
    @Autowired
    DistributionTaskRepository distributionTaskRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ConfigService configService;
    @Autowired
    ServerConfig serverConfig;
    @Autowired
    DBTaskService dBTaskService;

    @KafkaListener(topics = Topcs.TRANSACTION)
    public void handle(String msg, Acknowledgment ack) {
        log.info("收到kafka消息：" + msg);
        try {
            if (handleListener(Long.valueOf(msg))) {
                ack.acknowledge();
                log.info("kafka消息处理完成：" + msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("kafka消息处理失败：" + msg, e);
        }
    }


    /**
     * 消息处理器
     *
     * @param taskId 任务id
     * @return 返回是否消费掉消息
     */
    private boolean handleListener(Long taskId) throws Exception {
        //任务
        TransactionTask transactionTask;
        //当前执行的子任务
        DistributionTask lock;
        //获得任务并且根据处理机制过滤
        Object task = getHandleTask(taskId);
        if (task instanceof Boolean) {
            return (boolean) task;
        } else {
            transactionTask = (TransactionTask) task;
        }
        //执行锁策略
        Object check = checkLock(transactionTask);
        if (check instanceof Boolean) {
            return (boolean) check;
        } else {
            lock = (DistributionTask) check;
        }
        //执行事务机制
        boolean fag = invoke(transactionTask,lock);
        if (!fag) {
            log.error("当前任务执行事务失败 taskId:" + taskId);
        }
        return fag;
    }

    /**
     * 根据taskId获取要处理的任务
     *
     * @param taskId 任务id
     * @return 返回任务是否正确 / 需要执行的任务对象
     */
    private Object getHandleTask(Long taskId) throws Exception {
        Optional<TransactionTask> optional = transactionTaskRepository.findById(taskId);
        if (!optional.isPresent()) {
            log.error("当前任务id无法查询到任务taskId:" + taskId);
            return false;
        }
        TransactionTask transactionTask = optional.get();
        if (!transactionTask.getExpects().contains(serverConfig.getSponsor())) {
            log.info("当前服务不是该任务的参与者，默认消费掉 taskId:" + taskId);
            return true;
        }
        return transactionTask;
    }


    /**
     * 获得当前当前任务下当前服务组所有的锁
     *
     * @param transactionTask 主任务
     * @return 返回加锁操作是否成功 /  返回当前获得操作权限的子对象锁
     */
    private Object checkLock(TransactionTask transactionTask) throws Exception {
        //当前服务识别唯一标识
        String possessor = serverConfig.getSossessor();
        //加锁
        DistributionTask prepareLock = defaultLock(transactionTask, possessor);
        //检测锁是否生效
        List<DistributionTask> distributionList = distributionTaskRepository.findByTaskIdAndPossessorOrderByIdAsc(
                transactionTask.getId(), possessor);
        if (null == distributionList || distributionList.isEmpty()) {
            log.error("加锁失败:taskId:" + transactionTask.getId() + " sponsor:" + possessor);
            return false;
        }
        DistributionTask lock = distributionList.get(0);
        //比较生效的锁
        for (int i = 0; i < distributionList.size(); i++) {
            if (i != 0) {
                //删除锁
                distributionTaskRepository.delete(distributionList.get(i));
            }
        }
        if (!lock.getId().equals(prepareLock.getId()) && !lock.getPossessor().equals(possessor)) {
            //当前锁没生效
            log.info("当前服务没有抢占到任务锁" + lock.getId() + "，默认消费掉 taskId:" + transactionTask.getId() + " prepareLockId:" + prepareLock.getId());
            return true;
        }
        return lock;
    }


    /**
     * 增加默认锁
     *
     * @param transactionTask 主任务
     * @param possessor       当前服务识别唯一标识
     * @return 返回子任务对象
     */
    private DistributionTask defaultLock(TransactionTask transactionTask, String possessor) throws Exception {
        return distributionTaskRepository.save(DistributionTask.builder()
                .taskId(transactionTask.getId())
                .possessor(possessor)
                .createAt(new Date())
                .build());
    }

    /**
     * 代理执行具体的事务实例
     *
     * @param transactionTask 事务任务
     * @return 返回成功或者失败
     */
    private boolean invoke(TransactionTask transactionTask ,DistributionTask lock) throws Exception{
        DistributionTaskInvoke rlt = new DistributionTaskInvoke();
        try {
            Class clz = configService.getClass(transactionTask.getTheme());
            if (clz == null) {
                log.error("找不到处理实例 taskId:" + transactionTask.getId() + " theme:" + transactionTask.getTheme());
                rlt.setSucceed(false);
            } else {
                log.info("开始执行事务 " + transactionTask.toString());
                rlt = BeanInvoke.invoke(clz, transactionTask.getParameters());
            }
        } catch (Exception e) {
            e.printStackTrace();
            rlt.setSucceed(false);
            rlt.setText(e.getStackTrace().toString());
        } finally {
            //保存到数据库
            log.info("持久化子任务结果 taskId:" + transactionTask.getId());
            rlt.setDistributionTaskId(lock.getId());
            rlt.setPossessor(serverConfig.getSossessor());
            dBTaskService.seveDistributionTaskInvoke(rlt);
        }
        return rlt.getSucceed();
    }
}
