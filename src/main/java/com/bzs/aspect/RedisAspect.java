package com.bzs.aspect;

import com.bzs.cache.RedisAnnotation;
import com.bzs.redis.RedisUtil;
import com.bzs.utils.redisConstant.RedisConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class RedisAspect {


    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ObjectMapper mapper;
    /**
     * 拦截所有元注解RedisCache注解的方法
     */
    @Pointcut("@annotation(com.bzs.cache.RedisAnnotation)")
    public void pointcutMethod(){

    }

    /**
     * 环绕处理，先从Redis里获取缓存,查询不到，就查询MySQL数据库，
     * 然后再保存到Redis缓存里
     * @param joinPoint
     * @return
     */
    @Around("pointcutMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws JsonProcessingException {
        //前置：从Redis里获取缓存
        //先获取目标方法参数
        long startTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        RedisAnnotation redisAnnotation = method.getAnnotation(RedisAnnotation.class);
        String redisKey=redisAnnotation.key();
        int time=redisAnnotation.time();
        if (redisKey.equals(RedisConstant.MENU_LIST_NAME)){
            Object[] ss=joinPoint.getArgs();
            redisKey=redisKey+ss[0];
        }
        //获取目标方法所在类
        String target = joinPoint.getTarget().toString();
        String className = target.split("@")[0];

        //获取目标方法的方法名称
        String methodName = joinPoint.getSignature().getName();

        //redis中key格式：    applId:方法名称

        Object obj = null;

        if(redisUtil.hasKey(redisKey)){
            obj=redisUtil.get(redisKey);
            log.info("**********从Redis中查到了数据**********");
            log.info("Redis的KEY值:"+redisKey);
            log.info("REDIS的VALUE值:"+obj.toString());
            return obj;
        }
        long endTime = System.currentTimeMillis();
        log.info("Redis缓存AOP处理所用时间:"+(endTime-startTime));
        log.info("**********没有从Redis查到数据**********");
        try{
            obj = joinPoint.proceed();
        }catch(Throwable e){
            e.printStackTrace();
        }
        log.info("**********开始从MySQL查询数据**********");
//        后置：将数据库查到的数据保存到Redis
        if( redisUtil.set(redisKey,obj,time)){
            log.info("**********数据成功保存到Redis缓存!!!**********");
            log.info("Redis的KEY值:"+redisKey);
            log.info("REDIS的VALUE值:"+obj.toString());
            log.info("REDIS的VALUE值:"+mapper.writeValueAsString(obj));
        }
        return obj;
    }
}
