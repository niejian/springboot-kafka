package io.git.nj.message.controller;

import io.git.nj.message.utils.UserUtils;
import io.git.nj.message.vo.UserVo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @desc: cn.com.bluemoon.message.controller.MessageController
 * @author: niejian9001@163.com
 * @date: 2020/11/27 16:44
 */
@Slf4j
@RestController
public class MessageController {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TEST_TOPIC = "test";
    private static final String MSG_KEY = "getDate";

    UserUtils userUtils = new UserUtils();

    @GetMapping(value = "/getTime")
    public String getTime() {
        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaTemplate.send("test", LocalDateTime.now().toString());
        listenableFuture.addCallback(successResult -> log.info("发送消息成"),
                ex -> log.info("发送消息失败：{}", ex.getMessage()));
        return "SUCCESS";
    }

    @GetMapping(value = "/getTimes")
    public String getTime2() {
        String now = LocalDateTime.now().toString();
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TEST_TOPIC, MSG_KEY, now);
        // 判断消息是否发送成功
        future.addCallback(successResult -> {
            ProducerRecord<String, Object> record = successResult.getProducerRecord();
            String key = record.key();
            Integer partition = record.partition();
            Long timestamp = record.timestamp();
            String value = (String)record.value();
            log.info("=============");
            log.info("消息发送成功: {}");
            log.info("key: {}, partition: {}, timestamp:{}, value:{}", key, partition,
                    LocalDateTime.ofEpochSecond(timestamp,0, ZoneOffset.ofHours(8)).toString(),
                    value);
            log.info("=============");
        }, failException -> {
            // 消息发送失败
            log.error("消息发送失败：{}", failException.getMessage());
        });

        return "SUCCESS";
    }

    @GetMapping(value = "/getUser")
    public String getUser() {
        UserVo userVo = userUtils.generateUser();
        if (null == userVo) {
            return "获取用户失败";
        }

        String user = new Gson().toJson(userVo);
        String key = userVo.getAge() % 9 + "";

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("user-topic", key, user);
        future.addCallback(successCallBack -> {
            log.info("发送成功");
        }, failCallback -> {
            failCallback.getMessage();
            failCallback.printStackTrace();
        });
        return user;

    }
}
