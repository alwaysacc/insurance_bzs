package com.bzs.utils.threadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @program: insurance_bzs
 * @description: 先执行完的线程先做处理
 * @author: dengl
 * @create: 2019-04-22 16:19
 */
public class CompletionCallable {

    //存放处理结果的集合
    private static ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<Integer, Integer>();

    public static void main(String[] args) {
        try {
            completionServiceCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用completionService收集callable结果
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void completionServiceCount() throws InterruptedException, ExecutionException {
        Long start = System.currentTimeMillis();
        //创建线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();
        //开启线程
        //ExecutorService exs = Executors.newFixedThreadPool(5);
        try {
            CompletionService<Integer> cs = new ExecutorCompletionService<Integer>(threadPool);
            List<Integer> list = new ArrayList();
            //添加任务
            int threadNum = 5;
            for (int i = 0; i < threadNum; i++) {
                cs.submit(getTask(i));
            }
            //==================结果归集===================
            //方法1：future是提交时返回的，遍历queue则按照任务提交顺序，获取结果
//            for (Future<Integer> future : futureList) {
//                System.out.println("====================");
//                Integer result = future.get();//线程在这里阻塞等待该任务执行完毕,按照
//                System.out.println("任务result="+result+"获取到结果!"+new Date());
//                list.add(result);
//            }
            int sum = 0, temp = 0;
            for (int i = 0; i < threadNum; i++) {
                //先执行完的线程,先返回结果
                temp = cs.take().get();//采用completionService.take()，内部维护阻塞队列，任务先完成的先获取到
                sum += map.get(temp);
                System.out.print(temp + "\t");
            }
            System.out.println("Result->" + sum);
            System.out.println("总耗时=" + (System.currentTimeMillis() - start));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    public static Callable<Integer> getTask(final int no) {
        final Random rand = new Random();
        Callable<Integer> task = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int time = rand.nextInt(100) * 100;
                map.put(Integer.valueOf(no), time);
                System.out.println("Thread-" + no + " sleep :" + time);
                Thread.sleep(time);
                return no;
            }
        };
        return task;
    }
}
