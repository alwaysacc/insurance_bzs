package com.bzs.utils.queueUtil;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-05-27 17:32
 */
public class QueueTest {
    public static void main(String[] args) {

        System.out.println("3.两个堆栈实现一个队列：");
        Queue queue = new Queue();
        queue.in(1);
        queue.in(2);
        queue.in(3);
        System.out.println(queue.out());
        System.out.println(queue.out());
        queue.in(4);
        System.out.println(queue.out());
        System.out.println(queue.out());
        //queue.in(5);
        System.out.println(queue.out());

    }

}
