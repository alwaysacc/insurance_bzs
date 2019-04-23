package com.bzs.utils.threadUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @program: insurance_bzs
 * @description: 根据线程的执行顺序打印
 * @author: dengl
 * @create: 2019-04-22 16:12
 */
public class AsyncThread {
    private static List<Future<String>> futureList = new ArrayList<Future<String>>();

    public static void main(String[] args) {
       /* AsyncThread t = new AsyncThread();
        t.generate(3);
        t.doOtherThings();
        t.getResult(futureList);*/
        test();
    }

    /**
     * 生成指定数量的线程，都放入future数组
     *
     * @param threadNum
     */
    public void generate(int threadNum) {
        ExecutorService service = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; i++) {
            Callable<String> job = getJob(i);
            Future<String> f = service.submit(job);
            futureList.add(f);
        }
        //关闭线程池,不影响线程的执行
        service.shutdown();
    }

    /**
     * other things
     */
    public void doOtherThings() {
        try {
            for (int i = 0; i < 3; i++) {
                System.out.println("Do thing No:" + i);
                Thread.sleep(new Random().nextInt(5000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从future中获取线程结果，打印结果
     *
     * @param fList
     */
    public void getResult(List<Future<String>> fList) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(getCollectJob(fList));
        service.shutdown();
    }

    /**
     * 生成指定序号的线程对象
     *
     * @param i
     * @return
     */
    public Callable<String> getJob(final int i) {
        final int time = new Random().nextInt(10);
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000 * time);
                return "thread-" + i;
            }
        };
    }

    /**
     * 生成结果收集线程对象
     *
     * @param fList
     * @return
     */
    public Runnable getCollectJob(final List<Future<String>> fList) {
        return new Runnable() {
            @Override
            public void run() {
                for (Future<String> future : fList) {
                    try {
                        while (true) {
                            if (future.isDone() && !future.isCancelled()) {
                                System.out.println("Future:" + future + ", Result:" + future.get());
                                break;
                            } else {
                                System.out.println("休眠100ms...");
                                Thread.sleep(100);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
    public static void test() {
        Long start = System.currentTimeMillis();
        //开启多线程
        ExecutorService exs = Executors.newFixedThreadPool(5);
        try {
            //结果集
            List<Integer> list = new ArrayList<Integer>();
            List<FutureTask<Integer>> futureList = new ArrayList<FutureTask<Integer>>();
            //启动线程池，10个任务固定线程数为5
            for (int i = 0; i < 10; i++) {
                FutureTask<Integer> futureTask = new FutureTask<Integer>(new CallableTask(i + 1));
                //提交任务，添加返回
                exs.submit(futureTask);//Runnable特性
                futureList.add(futureTask);//Future特性
            }
            Long getResultStart = System.currentTimeMillis();
            System.out.println("结果归集开始时间=" + new Date());
            //2.结果归集，遍历futureList,高速轮询（模拟实现了并发）获取future状态成功完成后获取结果，退出当前循环
            for (FutureTask<Integer> future : futureList) {
                while (true) {//CPU高速轮询：每个future都并发轮循，判断完成状态然后获取结果，这一行，是本实现方案的精髓所在。即有10个future在高速轮询，完成一个future的获取结果，就关闭一个轮询
                    if (future.isDone() && !future.isCancelled()) {//获取future成功完成状态，如果想要限制每个任务的超时时间，取消本行的状态判断+future.get(1000*1, TimeUnit.MILLISECONDS)+catch超时异常使用即可
                        Integer i = future.get();//Future特性
                        System.out.println("i=" + i + "获取到结果!" + new Date());
                        list.add(i);
                        break;//当前future获取结果完毕，跳出while
                    } else {
                        Thread.sleep(1);////每次轮询休息1毫秒（CPU纳秒级)避免CPU高速轮循耗空CPU，可以休息一下。
                        System.out.println("sleep==1ms");
                    }
                }
            }
            System.out.println("list=" + list);
            System.out.println("总耗时=" + (System.currentTimeMillis() - start) + ",取结果归集耗时=" + (System.currentTimeMillis() - getResultStart));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exs.shutdown();
        }


    }

    static class CallableTask implements Callable<Integer> {
        Integer i;

        public CallableTask(Integer i) {
            super();
            this.i = i;
        }

        @Override
        public Integer call() throws Exception {
            if (i == 1) {
                Thread.sleep(3000);//任务1耗时3秒
            } else if (i == 5) {
                Thread.sleep(5000);//任务5耗时5秒
            } else {
                Thread.sleep(1000);//其它任务耗时1秒
            }
            System.out.println("线程：[" + Thread.currentThread().getName() + "]任务i=" + i + ",完成！" + new Date());
            return i;

        }

    }

}
