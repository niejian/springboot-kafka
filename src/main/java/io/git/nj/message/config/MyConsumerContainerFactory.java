package io.git.nj.message.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

/**
 * 当消费端设置为手动提交，就要设置一个ackmode
 * @desc: cn.com.bluemoon.message.config.MyConsumerContainerFactory
 * @author: niejian9001@163.com
 * @date: 2020/11/28 11:37
 */
@Configuration
public class MyConsumerContainerFactory {

    @Value("${spring.kafka.consumer.polling.timeout:1500}")
    private Integer consumerPollingTimeOut;
    @Bean
    public KafkaListenerContainerFactory<?> manualCommitContainerFactory(ConsumerFactory consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(10);
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setPollTimeout(consumerPollingTimeOut);
        factory.setBatchListener(true);
        // 设置手动提交
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
}
