package io.git.nj.message.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 公共线程池
 * @desc: cn.com.bluemoon.message.config.ThreadPoolConfig
 * @author: niejian9001@163.com
 * @date: 2020/12/4 16:20
 */
@Configuration
public class ThreadPoolConfig {
    @Value("${thread.pool.core.size:5}")
    private int threadPoolCoreSize;
    @Value("${thread.pool.max.size:20}")
    private int threadPoolMaxSize;
    @Value("${thread.pool.queue.size:20}")
    private int threadPoolQueueSize;
    @Value("${thread.pool.keep.alive.seconds:2000}")
    private int threadPoolKeepAliveSeconds;
    @Value("${thread.pool.name: myThread-name}")
    private String threadPoolName;

    @Primary
    @Bean
    public ThreadPoolTaskExecutor threadPoolExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        /** 核心线程数，默认为1 **/
        threadPoolTaskExecutor.setCorePoolSize(threadPoolCoreSize);
        /** 最大线程数，默认为Integer.MAX_VALUE **/
        threadPoolTaskExecutor.setMaxPoolSize(threadPoolMaxSize);
        /** 队列最大长度，一般需要设置值: 大于等于notifyScheduledMainExecutor.maxNum；默认为Integer.MAX_VALUE **/
        threadPoolTaskExecutor.setQueueCapacity(threadPoolQueueSize);
        /** 线程池维护线程所允许的空闲时间，默认为60s **/
        threadPoolTaskExecutor.setKeepAliveSeconds(threadPoolKeepAliveSeconds);
        /**
         * 线程池对拒绝任务（无线程可用）的处理策略，目前只支持AbortPolicy、CallerRunsPolicy；默认为后者
         *
         * AbortPolicy:直接抛出java.util.concurrent.RejectedExecutionException异常
         * CallerRunsPolicy:主线程直接执行该任务，执行完之后尝试添加下一个任务到线程池中，可以有效降低向线程池内添加任务的速度
         * DiscardOldestPolicy:抛弃旧的任务、暂不支持；会导致被丢弃的任务无法再次被执行
         * DiscardPolicy:抛弃当前任务、暂不支持；会导致被丢弃的任务无法再次被执行
         */
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        threadPoolTaskExecutor.setThreadFactory(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName(threadPoolName);
            return thread;
        });

        return threadPoolTaskExecutor;

    }

}
