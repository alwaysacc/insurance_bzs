package com.bzs.controller;

public class Test {
    public static void main(String[] args) {

    }
    private String run(){

        return  "";
    }
    class run1 implements Runnable{
        int n=0;
        int times=0;
        public run1(int n)
        {
            this.n=n;
        }
        @Override
        public void run() {
            for (int i = 0; i < n; i++) {
                System.out.println("run"+n);
            }
        }
    }
}
