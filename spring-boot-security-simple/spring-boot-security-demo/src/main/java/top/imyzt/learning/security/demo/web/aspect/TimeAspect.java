package top.imyzt.learning.security.demo.web.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author imyzt
 * @date 2019/6/4
 * @description TimeAspect
 *
 * 切点: @Pointcut
 * 前置通知: @Before
 * 后置通知: @After
 * 返回通知: @AfterReturning
 * 异常通知: @AfterThrowing
 * 环绕通知: @Around
 */
@Aspect
@Component
@Slf4j
public class TimeAspect {

    @Pointcut("execution(* top.imyzt.learning.security.demo.web.controller.UserController.*(..))")
    public void declareJoinPointExpression() {}

    @Around("declareJoinPointExpression()")
    public Object handlerControllerMethod(ProceedingJoinPoint joinPoint) {

        // 前置通知
        log.info("time aspect start");
        LocalDateTime startTime = LocalDateTime.now();

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            log.info("arg is {}", arg);
        }

        Object returnValue = null;
        try {
            // 返回通知
            returnValue = joinPoint.proceed();
        } catch (Throwable throwable) {
            // 异常通知
            log.error("异常通知: ", throwable);
        }

        // 后置通知
        long millis = Duration.between(startTime, LocalDateTime.now()).toMillis();
        log.info("time aspect 耗时: {} 毫秒", millis);
        log.info("time aspect end");

        return returnValue;
    }

}
