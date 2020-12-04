package io.git.nj.message.intercept;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * producer 拦截器
 * @desc: cn.com.bluemoon.message.intercept.ProducerInterceptor
 * @author: niejian9001@163.com
 * @date: 2020/11/28 21:25
 */
@Component
public class MyMsgProducerInterceptor implements ProducerInterceptor {

    /**
     * 消息提供者拦截器
     *
     * @param record
     * @return
     */
    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        String newValue = "prefix:" + record.value();
        ProducerRecord producerRecord = new ProducerRecord(record.topic(), newValue);
        return producerRecord;

    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
