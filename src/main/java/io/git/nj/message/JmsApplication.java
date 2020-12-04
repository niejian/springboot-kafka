package io.git.nj.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @desc: cn.com.bluemoon.message.JmsApplication
 * @author: niejian9001@163.com
 * @date: 2020/11/27 16:01
 */
@Slf4j
@SpringBootApplication
public class JmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(JmsApplication.class, args);
        log.info("ms server started...");
    }
}
