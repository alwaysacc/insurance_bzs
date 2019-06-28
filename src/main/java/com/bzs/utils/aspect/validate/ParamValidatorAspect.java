package com.bzs.utils.aspect.validate;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;
import java.util.Iterator;
import java.util.Set;

/**
 * @program: insurance_bzs
 * @description: controller 层入参校验切面
 * @author: dengl
 * @create: 2019-06-26 15:06
 */
@Order(100)
@Component
@Aspect
public class ParamValidatorAspect {
    @Resource
    private LocalValidatorFactoryBean localValidatorFactoryBean;

    public ParamValidatorAspect() {
    }
    /**
     * 方式1：切入点(
     */
    @Pointcut( "@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
                    "||@annotation(org.springframework.web.bind.annotation.GetMapping)" +
                    "||@annotation(org.springframework.web.bind.annotation.PostMapping)" +
                    "||@annotation(org.springframework.web.bind.annotation.PutMapping)"
    )
//    @Pointcut("execution(* com.*..controller.*.*(..))")//方式2
    private void parameterPointCut() {
    }
    /**
     * 处理
     *
     * @param joinPoint
     * @param request
     */
    @Before("parameterPointCut() && args(request,..)")
    public void validateParameter(JoinPoint joinPoint, MyRequest request) {
        Set<ConstraintViolation<MyRequest>> validErrors = this.localValidatorFactoryBean.validate(request, new Class[]{Default.class});
        Iterator iterator = validErrors.iterator();
        StringBuilder errorMsg = new StringBuilder();
        while (iterator.hasNext()) {
            ConstraintViolation constraintViolation = (ConstraintViolation) iterator.next();
            String error = constraintViolation.getPropertyPath() + ":" + constraintViolation.getMessage();
            errorMsg.append(iterator.hasNext() ? error + "; " : error);
        }
        if (!validErrors.isEmpty()) {
            throw new BusinessException(BusinessCode.PARAM_ILLEGAL, errorMsg.toString());
        }
    }
}
