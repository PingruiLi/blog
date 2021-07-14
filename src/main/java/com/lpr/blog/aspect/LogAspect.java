package com.lpr.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
@Aspect
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final static String REQUEST_INFO = "Request URL: %s, IP: %s, classMethod: %s, args: {%s}";

    @Before(value = "execution(* com.lpr.blog.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("----------------Do Before--------------------");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String argsString = Arrays.toString(args);
        String requestInfo = String.format(REQUEST_INFO, url, ip, classMethod, argsString);
        logger.info("Request: { " + requestInfo + " }");
    }

    @After(value = "execution(* com.lpr.blog.controller.*.*(..))")
    public void logAfter() {
        logger.info("---------------Do After-------------------");
    }

    @AfterReturning(value = "execution(* com.lpr.blog.controller.*.*(..))", returning = "result")
    public void logAfterReturn(Object result) {
        logger.info("----------------Returned result---------------------");
        logger.info("Result : {}", result);
    }


}
