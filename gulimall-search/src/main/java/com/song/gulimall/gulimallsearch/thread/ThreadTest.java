package com.song.gulimall.gulimallsearch.thread;

import java.util.concurrent.*;

/* *
 * @program: gulimall
 * @description
 * @author: swq
 * @create: 2021-03-24 10:52
 **/
public class ThreadTest {
    public static ExecutorService executor = Executors.newFixedThreadPool(10);


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main....start");

        // 不带返回值
//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
//            System.out.println("Runnable 当前线程号。。。。。" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println(i);
//        }, executor);


        // 带返回值
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("Runnable 当前线程号。。。。。" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println(i);
//            return i;
//        }, executor).whenComplete((result, exception) -> {
//            System.out.println("执行成功的结果。。。。" + result + ";异常时。。。" + exception);
//        }).exceptionally((throwable) -> {  // 如果发生异常 执行默认返回值
//
//            return 10;
//        });

//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("Runnable 当前线程号。。。。。" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println(i);
//            return i;
//        }, executor).handle((result, throwable) -> {  // 如果发生异常 执行默认返回值
//            if (result != null) {
//                return result * 2;
//            }
//            if (throwable != null) {
//                return 0;
//            }
//            return 2;
//        });


//        // 线程串行化  thenRunAsync  不接受上一步的返回值
//        CompletableFuture.supplyAsync(() -> {
//            System.out.println("Runnable 当前线程号。。。。。" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println(i);
//            return i;
//        }, executor).thenRunAsync(() -> {
//            System.out.println("任务2 当前线程号。。。。。" + Thread.currentThread().getId());
//        },executor);


//        // 线程串行化  thenAcceptAsync  接受上一步的返回值 无返回值
//        CompletableFuture.supplyAsync(() -> {
//            System.out.println("Runnable 当前线程号。。。。。" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println(i);
//            return i;
//        }, executor).thenAcceptAsync((result) -> {
//            System.out.println("上一步的执行结果是。。。" + result);
//            System.out.println("任务2 当前线程号。。。。。" + Thread.currentThread().getId());
//        }, executor);


        // 线程串行化  thenApplyAsync  接受上一步的返回值 有返回值
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("Runnable 当前线程号。。。。。" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println(i);
//            return i;
//        }, executor).thenApplyAsync((result) -> {
//            System.out.println("上一步的执行结果是。。。" + result);
//            System.out.println("任务2 当前线程号。。。。。" + Thread.currentThread().getId());
//            return 5;
//        }, executor);


        CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务1 当前线程号。。。。。" + Thread.currentThread().getId());
            int i = 10 / 2;
            return i;
        }, executor);


        CompletableFuture<Integer> future02 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务2 当前线程号。。。。。" + Thread.currentThread().getId());
            int i = 10 / 5;
            return i;
        }, executor);

//        CompletableFuture<Void> future = CompletableFuture.allOf();
//        System.out.println("zuihou " + future01.get() + future02.get());
        CompletableFuture<Object> any = CompletableFuture.anyOf(future01,future02);
        System.out.println("zuihou..........."+any.get());


//        future01.thenAcceptBothAsync(future02, (f1, f2) -> {
//            System.out.println("任务3....." + "f1的結果" + f1 + "=======f2的結果" + f2);
//        }, executor);


//        future01.runAfterBothAsync(future02, () -> {
//            System.out.println("任务3.....");
//        }, executor);


//        System.out.println("future...." + future.get());


        System.out.println("main....end");
    }

    public void thread(String[] args) throws ExecutionException, InterruptedException {


        System.out.println("main....start");

        /**
         * 1、继承Thread类
         * 2、实现Runnable接口
         * 3、实现Callable接口  结合FutureTask使用
         * 4、线程池
         */

        // 1、继承Thread类
//        Thread01 thread01 = new Thread01();
//        thread01.start();

        // 实现Runnable接口
//        Runnable01 runnable01 = new Runnable01();
//        Thread thread = new Thread(runnable01);
//        thread.start();


        // 实现Callable接口
//        Callable01 callable01 = new Callable01();
//        FutureTask<Integer> futureTask = new FutureTask<>(callable01);
//        new Thread(futureTask).start();

        // 阻塞等待整个线程运行完成，返回运行结果
//        Integer integer = futureTask.get();
//        System.out.println(integer);

        // 线程池
//        service.execute(new Runnable01());


        /* *
         * int corePoolSize,
         * int maximumPoolSize,
         * long keepAliveTime,
         * TimeUnit unit,
         * BlockingQueue<Runnable> workQueue,
         * ThreadFactory threadFactory,
         * RejectedExecutionHandler handler
         */
//        ThreadPoolExecutor threadPoolExecutor =new ThreadPoolExecutor(5,
//                200,
//                10,
//                TimeUnit.SECONDS,
//                new LinkedBlockingDeque<>(10000),
//                Executors.defaultThreadFactory(),
//                new ThreadPoolExecutor.AbortPolicy());


        System.out.println("main....end");
    }

    /* *
     * 实现Callable接口  结合FutureTask使用
     */
    public static class Callable01 implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("Runnable 当前线程号。。。。。" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println(i);
            return i;
        }
    }

    /* *
     * 实现Runnable接口
     */
    public static class Runnable01 implements Runnable {
        @Override
        public void run() {
            System.out.println("Runnable 当前线程号。。。。。" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println(i);
        }
    }


    /* *
     * 继承Thread类
     */
    public static class Thread01 extends Thread {
        @Override
        public void run() {
            System.out.println("当前线程号。。。。。" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println(i);
        }
    }
}
