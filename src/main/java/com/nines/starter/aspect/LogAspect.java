package com.nines.starter.aspect;

import com.nines.starter.util.BrowserUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class LogAspect {

//    @Resource
//    private LogService logService;

    // 切入点 任何修饰符 controller 下的所有类的所有参数的方法
    @Pointcut("execution(* com.nines.starter.controller.*.*(..))")
    public void log(){}

//    @Pointcut("execution(* com.nines.starter.controller.SysUserController.*(..))")
//    public void blogLog(){}

//    @Before("blogLog()")
//    public void doBlogBefore(JoinPoint joinPoint){
//        // 获取 HttpServletRequest
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
//
//        // 获取请求的url
//        String url = request.getRequestURL().toString();
//        // 获取浏览器信息
//        String browserInfo = BrowserUtil.getOsAndBrowserInfo(request);
//        // 获取请求者的 ip地址
//        String ip = request.getRemoteAddr();
//        // 获取请求的 方法
//        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
//
//        try {
//            logService.addLog(new SysLog(ip, new Date(), classMethod, url, browserInfo));
//        }catch (Exception e){
//            log.error(e.getMessage());
//        }
//    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        // 获取 HttpServletRequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        // 获取请求的url
        String url = request.getRequestURL().toString();
        // 获取请求者的 ip地址
        String ip = request.getRemoteAddr();
        // 获取请求的 方法
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        // 获取请求的参数
        Object[] args = joinPoint.getArgs();

        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);

        log.info("Request : {}", requestLog);
//        System.out.println("-----Before------");
    }

    @After("log()")
    public void doAfter(){
//        System.out.println("-------After------");
    }

    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterReturn(Object result){
        // 获取返回值
        log.info("Result : {}", result);
    }

}
