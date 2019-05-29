package com.bzs.utils.queueUtil;

import java.util.Stack;

/**
 * @program: insurance_bzs
 * @description: 队列类
 * @author: dengl
 * @create: 2019-05-27 17:31
 */
public class Queue {
    Stack<Integer> stackA = new Stack<Integer>();
    Stack<Integer> stackB = new Stack<Integer>();
    //入队
    public void in(int n) {
        stackA.push(n);
    }

    //出队
    public int out() {
        if(stackB.isEmpty()){
            while (stackA.size() > 0) {
                stackB.push(stackA.pop());
            }
        }
        return stackB.pop();
    }

}
