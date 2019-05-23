package com.bzs.utils.threadUtil;

import com.bzs.utils.dateUtil.DateUtil;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.record.formula.functions.T;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: insurance_bzs
 * @description: completableFuture类测试练习
 * @author: dengl
 * @create: 2019-04-23 08:56
 */
public class CompletableFutureDemo {
    public static void main(String[] args) {
        //test1();
        //lambda();
        try {
            test7();
        } catch (Exception e) {

        }

    }

    public static void test1() {
        Long start = System.currentTimeMillis();//任务启动的时间
        //结果集
        List<String> list = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        ExecutorService ex = Executors.newFixedThreadPool(10);
        //List<CompletableFuture<String>> futureList = new ArrayList<>();
        List<Integer> taskList = Lists.newArrayList(2, 1, 3, 4, 5, 6, 7, 8, 9, 10);
        try {
            //方式一：循环创建CompletableFuture list,调用sequence()组装返回一个有返回值的CompletableFuture，返回结果get()获取
//            for(int i=0;i<taskList.size();i++){
//                final int j=i+1;
//                CompletableFuture<String> future = CompletableFuture.supplyAsync(()->calc(j), exs)//异步执行
//                        .thenApply(e->Integer.toString(e))//Integer转换字符串    thenAccept只接受不返回不影响结果
////                        .whenComplete((v, e) -> {//如需获取任务完成先手顺序，此处代码即可
////                            System.out.println("任务"+v+"完成!result="+v+"，异常 e="+e+","+new Date());
////                            list2.add(v);
////                        })
//                        ;
//                futureList.add(future);
//            }
//            //流式获取结果
//            list = sequence(futureList).get();//[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]此处不理解为什么是这样的顺序？谁知道求告知
            //方式二：全流式处理转换成CompletableFuture[]+组装成一个无返回值CompletableFuture，
            // join等待执行完毕。返回结果whenComplete获取
            @SuppressWarnings("rawtypes")
            CompletableFuture[] cfs = taskList.stream().map(object -> CompletableFuture.supplyAsync(() -> calc(object), ex)
                    .thenApply(h -> Integer.toString(h))
                    .whenComplete((v, e) -> {//如需获取任务完成先手顺序，此处代码即可
                        System.out.println("任务" + v + "完成!result=" + v + "，异常 e=" + e + "," + DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
                        list2.add(v);
                    }))
                    .toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(cfs).join();//封装后无返回值，必须自己whenComplete()获取
            System.out.println("list2=" + list2 + "list=" + list + ",耗时=" + (System.currentTimeMillis() - start));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ex.shutdown();
        }
    }

    public static Integer calc(Integer i) {
        try {
            if (i == 1) {
                Thread.sleep(3000);//任务1耗时3秒
            } else if (i == 5) {
                Thread.sleep(5000);//任务5耗时5秒
            } else {
                Thread.sleep(1000);//其它任务耗时1秒
            }
            System.out.println("task线程：" + Thread.currentThread().getName() + "任务i=" + i + ",完成！" + DateUtil.getDateToString(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i;
    }
    // allOf方法是当所有的CompletableFuture都执行完后执行计算。
    // anyOf方法是当任意一个CompletableFuture执行完后就会执行计算，计算的结果相同。

    /**
     * @param futures List
     * @return
     * @Description 组合多个CompletableFuture为一个CompletableFuture, 所有子任务全部完成，组合后的任务才会完成。带返回值，可直接get.
     * @author diandian.zhang
     * @date 2017年6月19日下午3:01:09
     * @since JDK1.8
     */
    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        //1.构造一个空CompletableFuture，子任务数为入参任务list size
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        //2.流式（每个子任务join操作后转换为list）往空CompletableFuture中添加结果
        return allDoneFuture.thenApply(v -> futures.parallelStream().map(CompletableFuture::join).collect(Collectors.<T>toList()));
    }
    /**
     * @param futures Stream
     * @return
     * @Description Stream流式类型futures转换成一个CompletableFuture, 所有子任务全部完成，组合后的任务才会完成。带返回值，可直接get.
     * @author diandian.zhang
     * @date 2017年6月19日下午6:23:40
     * @since JDK1.8
     */
    public static <T> CompletableFuture<List<T>> sequence(Stream<CompletableFuture<T>> futures) {
        List<CompletableFuture<T>> futureList = futures.filter(f -> f != null).collect(Collectors.toList());
        return sequence(futureList);
    }
    public static List<Integer>test7(){
        Long start=System.currentTimeMillis();
        CompletableFuture<Integer> f1=  CompletableFuture.supplyAsync(()->{
            Random rand = new Random();
            int time = rand.nextInt(1000);
            System.out.println("执行时间"+time);
            return  time;
        }) ;
        CompletableFuture<Integer> f2=  CompletableFuture.supplyAsync(()->{
            Random rand = new Random();
            int time = rand.nextInt(1000);
            System.out.println("执行时间"+time);
            return  time;
        }) ;
        List<CompletableFuture<Integer>> list=new ArrayList<CompletableFuture<Integer>>();
        list.add(f1);
        list.add(f2);
       List<Integer>lists=null;
        try{
            lists =sequence(list).get();
            System.out.println("任务总耗时:"+(System.currentTimeMillis()-start));
            for (Integer i: lists) {
                System.out.println(i);
            }
        }catch(Exception e){

        }
        Long end=System.currentTimeMillis();
        System.out.println("总耗时:"+(end-start));
        return lists;
    }

    /**
     * Java Future转CompletableFuture:
     *
     * @param future
     * @param executor
     * @param <T>
     * @return
     */
    public static <T> CompletableFuture<T> toCompletable(Future<T> future, Executor executor) {

        return CompletableFuture.supplyAsync(() -> {

            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }
    public static void test6(){
        String.format("%s 的价格是 %.2f", "sss",1212);
        List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5);//准备的请求参数
        System.out.println( "输出"+ids.parallelStream().getClass());
    }
    public static void test2() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 1).thenApply(i -> i + 5).thenApply(i -> i * 3);
        Future<Integer> f = future.whenComplete((r, e) -> System.out.println(r));
        System.out.println("阻塞完成" + f.get());
        //System.in.read();
        Integer future1 = CompletableFuture.supplyAsync(() -> 1).thenApply(i -> i + 1).thenApply(i -> i * 2).join();
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "hello,").thenApply(i -> i + "world").thenApply(String::toUpperCase);
        System.out.println("" + future1);
        System.out.println("" + future2.get());
        CompletableFuture.supplyAsync(() -> "hello,").thenApply(i -> i + "world").thenApply(String::toUpperCase).thenCombine(CompletableFuture.completedFuture("Java"), (String s1, String s2) -> s1 + s2).thenAccept(System.out::println);
        List<String> StrList = new ArrayList<>();
        StrList.add("aa");
        StrList.add("cc");
        StrList.add("cc");
        List<CompletableFuture> futureList = new ArrayList<>();
        for (String str : StrList) {
            futureList.add(CompletableFuture.supplyAsync(() -> str).thenApply(e -> e.toUpperCase()));
        }
        CompletableFuture.anyOf(futureList.toArray(new CompletableFuture[futureList.size()]))
                .whenComplete((r, e) -> {
                    if (null != e) {
                        System.out.println("quabu");
                    } else {
                        throw new RuntimeException();
                    }
                    System.out.println("全部完成");
                });

    }

    public static void test3() {
        Random rand = new Random();

        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            int time = rand.nextInt(1000);
            try {
                Thread.sleep(time);
                System.out.println("执行时间" + time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return time;
        });

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            int time = 100 + rand.nextInt(1000);
            try {
                Thread.sleep(time);
                System.out.println("执行时间" + time);
            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return time;

        });


//CompletableFuture<Void> f =  CompletableFuture.allOf(future1,future2);

        CompletableFuture<Integer> f = future1.applyToEither(future2, i -> i);
        try {
            System.out.println(f.get());
        } catch (Exception e) {
            System.out.println("出现异常 ");
        }


    }

    public static void test4() {
        Random rand = new Random();

        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {

            try {
                int time = rand.nextInt(1000);
                Thread.sleep(time);
                System.out.println("执行时间" + time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {

            try {
                int time = 100 + rand.nextInt(1000);
                Thread.sleep(time);
                System.out.println("执行时间" + time);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return "abc";

        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {

            int time = 10 + rand.nextInt(1000);
            try {
                Thread.sleep(time);
                System.out.println("执行时间" + time);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

            return "def";

        });

//CompletableFuture<Void> f =  CompletableFuture.allOf(future1,future2);

        CompletableFuture<Object> f = CompletableFuture.anyOf(future1, future2, future3);
        try {
            System.out.println(f.get());
        } catch (Exception e) {
            System.out.println("出现异常 ");
        }


    }

    public static void test5() {
        Random rand = new Random();
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int time = 10 + rand.nextInt(1000);
            try {
                Thread.sleep(time);
                System.out.println("执行时间" + time);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }
            return time;
        });

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            int time = rand.nextInt(1000);
            try {
                Thread.sleep(time);
                System.out.println("执行时间" + time);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }
            return time;

        });
        CompletableFuture<String> f = future.thenCombine(future2, (x, y) -> y + "-" + x);
        try {
            System.out.println(f.get()); //abc-100
        } catch (Exception e) {

        }

    }

    //类名::方法名
    public static void lambda() {
        LambdaTest l = (Integer m) -> m;
        Integer s = l.get(55);
        System.out.println(s);
    }

    interface LambdaTest {
        Integer get(Integer s);
    }
}
