package com.bzs.utils.queueUtil;

/**
 * @program: insurance_bzs
 * @description:
 * @author: dengl
 * @create: 2019-05-27 17:38
 */
public class QueueTestNode {
    public static void main(String[] args) {
    LinkQueue<Integer> queue = new LinkQueue<Integer>();

        queue.enQueue(1);
        queue.enQueue(2);
        queue.enQueue(3);
        queue.enQueue(4);

        System.out.println("size："+queue.size());

        System.out.println("出队列："+queue.deQueue());
        System.out.println("出队列："+queue.deQueue());
        System.out.println("出队列："+queue.deQueue());
        System.out.println("出队列："+queue.deQueue());

        System.out.println("删完重新添加==============");
        queue.enQueue(11);
        queue.enQueue(22);
        queue.enQueue(33);
        queue.enQueue(44);

        System.out.println("size："+queue.size());

        System.out.println("出队列："+queue.deQueue());
        System.out.println("出队列："+queue.deQueue());
        System.out.println("出队列："+queue.deQueue());
        System.out.println("出队列："+queue.deQueue());
//blog.csdn.net/qq_34975710/article/details/78028302
}
}
