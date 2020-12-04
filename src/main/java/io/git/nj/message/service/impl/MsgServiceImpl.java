package io.git.nj.message.service.impl;

import io.git.nj.message.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @desc: cn.com.bluemoon.message.service.impl.MsgServiceImpl
 * @author: niejian9001@163.com
 * @date: 2020/12/4 16:36
 */
@Slf4j
@Service(value = "msgService")
public class MsgServiceImpl implements MsgService {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public String sendMsg(String topic, String key, String content) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, content);
        future.addCallback(successResult -> {
            ProducerRecord<String, Object> record = successResult.getProducerRecord();
            Integer partition = record.partition();
            Long timestamp = record.timestamp();
            String value = (String)record.value();
            log.info("=============");
            log.info("消息发送成功: {}");
            log.info("key: {}, partition: {}, timestamp:{}, value:{}", key, partition,
                    LocalDateTime.ofEpochSecond(timestamp,0, ZoneOffset.ofHours(8)).toString(),
                    value);
            log.info("=============");
        }, fail -> {
            fail.printStackTrace();
        });
        return "success";
    }
}
