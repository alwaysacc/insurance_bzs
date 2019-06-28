package com.bzs.utils.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @program: insurance_bzs
 * @description: 切面测试类
 * @author: 原文：https://blog.csdn.net/u010096717/article/details/82221263
 * @create: 2019-06-25 10:27
 */
@Order(5)
@Aspect
@Component
@Slf4j
public class TestAspect {
    //com.kzj.kzj_rabbitmq.controller 包中所有的类的所有方法切面
    //@Pointcut("execution(public *  com.bzs.controller.*.*(..))")

    //只针对 MessageController 类切面
    //@Pointcut("execution(public * com.kzj.kzj_rabbitmq.controller.MessageController.*(..))")

    //统一切点,对com.kzj.kzj_rabbitmq.controller及其子包中所有的类的所有方法切面
    @Pointcut("execution(public * com.bzs.controller..*.*(..))&&@annotation(com.bzs.utils.aspect.AspectInterface)")
    public void Pointcut() {
        log.info("执行了切点");
    }

    //前置通知
    @Before("Pointcut()")
    public void beforeMethod(JoinPoint joinPoint){
        log.info("调用了前置通知");

    }

    //@After: 后置通知
    @After("Pointcut()")
    public void afterMethod(JoinPoint joinPoint){
        log.info("调用了后置通知");
    }
    //@AfterRunning: 返回通知 rsult为返回内容
    @AfterReturning(value="Pointcut()",returning="result")
    public void afterReturningMethod(JoinPoint joinPoint,Object result){
        log.info("调用了返回通知");
    }
    //@AfterThrowing: 异常通知
    @AfterThrowing(value="Pointcut()",throwing="e")
    public void afterReturningMethod(JoinPoint joinPoint, Exception e){
        log.info("调用了异常通知");
    }

    //@Around：环绕通知
    @Around("Pointcut()")
    public Object Around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("around执行方法之前");
        Object object = pjp.proceed();
        log.info("around执行方法之后--返回值：" +object);
        return object;
    }
}
