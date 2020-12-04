package io.git.nj.message.listener;

import io.git.nj.message.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @desc: cn.com.bluemoon.message.listener.MsgListener
 * @author: niejian9001@163.com
 * @date: 2020/11/28 11:12
 */
@Slf4j
@Component
public class MsgListener {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private MsgService msgService;

    @KafkaListener(topics = "test", containerFactory = "manualCommitContainerFactory")
//    @Override
    public void onMessage(List<ConsumerRecord<String, String>> datas, Acknowledgment acknowledgment) {
        datas.forEach(data -> {
            String value = data.value();
            String key = data.key();
            long timestamp = data.timestamp();
            int partition = data.partition();
            log.info("消费端收到消息：value: {}, key:{},time:{}, partition", value, key,
                    LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC),
                    partition);

        });

        // 手动提交位移
        acknowledgment.acknowledge();
    }


    @KafkaListener(topics = "user-topic", containerFactory = "manualCommitContainerFactory")
    public void userTopic(List<ConsumerRecord<String, String>> datas, Acknowledgment acknowledgment) {
        datas.forEach(data -> {
            String value = data.value();
            String key = data.key();
            long timestamp = data.timestamp();
            int partition = data.partition();
            log.info("消费端收到消息：value: {}, key:{},time:{}, partition: {}", value, key,
                    LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC),
                    partition);
            // 必须手动提交位移
            acknowledgment.acknowledge();
        });
    }

    @KafkaListener(topics = "user-topic", groupId = "syncConsumer", containerFactory = "manualCommitContainerFactory")
    public void syncConsumUserTopic(List<ConsumerRecord<String, String>> datas, Acknowledgment acknowledgment) {
        datas.forEach(data -> {
            String value = data.value();
            String key = data.key();
            long timestamp = data.timestamp();
            int partition = data.partition();
            log.info("消费端收到消息：value: {}, key:{},time:{}, partition: {}", value, key,
                    LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC),
                    partition);
            // 必须手动提交位移
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                log.info("消费者获取到消息， 开始进行业务处理");
                try {
                    Thread.sleep(200);
                    msgService.sendMsg("test", "time", LocalDateTime.now().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return "消费成功, consum success";
            }, threadPoolTaskExecutor);
            try {

                // 业务逻辑处理完，回调
                future.whenCompleteAsync((s, e) -> {
                    log.info("获取业务返回结果：{}", s);
                    e.printStackTrace();
                });

            }  catch (Exception e) {
                e.printStackTrace();
            }
            acknowledgment.acknowledge();
        });

    }



}
