package com.ibrahim.spring_boot_rest.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// Aspect oriented programming
@Component
@Aspect
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    // Return type, class name.method name, args
    @Before("execution(* com.ibrahim.spring_boot_rest.service.JobService.updateJob(..)) || execution(* com.ibrahim.spring_boot_rest.service.JobService.getJob(..))")
    public void logMethodCall(JoinPoint jp) {
        LOGGER.info("Method called " + jp.getSignature().getName());
    }

    @After("execution(* com.ibrahim.spring_boot_rest.service.JobService.updateJob(..)) || execution(* com.ibrahim.spring_boot_rest.service.JobService.getJob(..))")
    public void logMethodExecuted(JoinPoint jp) {
        LOGGER.info("Method executed " + jp.getSignature().getName());
    }

    @AfterThrowing("execution(* com.ibrahim.spring_boot_rest.service.JobService.updateJob(..)) || execution(* com.ibrahim.spring_boot_rest.service.JobService.getJob(..))")
    public void logMethodCrash(JoinPoint jp) {
        LOGGER.info("Method has some issues " + jp.getSignature().getName());
    }

    @AfterReturning("execution(* com.ibrahim.spring_boot_rest.service.JobService.updateJob(..)) || execution(* com.ibrahim.spring_boot_rest.service.JobService.getJob(..))")
    public void logMethodExecutedSuccess(JoinPoint jp) {
        LOGGER.info("Method executed successfully " + jp.getSignature().getName());
    }


}
