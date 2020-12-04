package io.git.nj.message.utils;

import java.util.concurrent.*;

/**
 * @desc: cn.com.bluemoon.message.utils.ThreadUtils
 * @author: niejian9001@163.com
 * @date: 2020/12/4 15:43
 */
public class ThreadUtils {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(2, 10, 1000,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(10),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setName("my-thread");
                        return t;
                    }
                });

        executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("executorService.submit 是否为守护线程 :" + Thread.currentThread().isDaemon());
                return null;
            }
        });

        final CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync( () -> {

            System.out.println("this is lambda supplyAsync");
            System.out.println("supplyAsync 是否为守护线程 " + Thread.currentThread().isDaemon());
            try {
                //TODO 注视点get(), 此处不会再执行，因为主程序执行完就退出了
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("this lambda is executed by forkJoinPool");
            return "result1";

        });

        final CompletableFuture<String> future = CompletableFuture.supplyAsync( () -> {
            System.out.println("this is task with executor");
            System.out.println("supplyAsync 使用executorService 时是否为守护线程 : " + Thread.currentThread().isDaemon());
            return "result2";

        }, executorService);

//        System.out.println(completableFuture.get());
//        System.out.println(future.get());
        executorService.shutdown();
    }



}
