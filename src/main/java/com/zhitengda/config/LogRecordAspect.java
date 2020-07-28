package com.zhitengda.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author langao_q
 * @create 2020-07-16 14:28
 * 请求日志输出类
 */
@Slf4j
@Aspect
@Configuration
public class LogRecordAspect {

    /**
     * 定义切点Pointcut
     */
    @Pointcut("execution(* com.zhitengda.controller.*Controller*.*(..))")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String paraString = JSON.toJSONString(request.getParameterMap());
        log.info("-------------------------"+ DateUtil.now()+"-----------------------------");
        log.info("【请求开始】URI: {}, method: {}, params: {}", uri, method, paraString);

        Object result = pjp.proceed();
        log.info("【请求结束】controller的返回值是 " + JSONUtil.toJsonStr(result) );
        log.info("-------------------------------------------------------------------------"+ "\n");
        return result;
    }
}
