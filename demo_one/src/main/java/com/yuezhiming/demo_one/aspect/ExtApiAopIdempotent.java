package com.yuezhiming.demo_one.aspect;/**
 * Created by ASUSon 2020/4/18 20:05
 */

import com.yuezhiming.demo_one.annotation.ExtApiIdempotent;
import com.yuezhiming.demo_one.annotation.ExtApiToken;
import com.yuezhiming.demo_one.commons.ConstantUtils;
import com.yuezhiming.demo_one.commons.RedisTokenUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: demo_one

 * @description:

 * @author: yuezm

 * @create: 2020-04-18 20:05
 **/
@Component
@Aspect
public class ExtApiAopIdempotent {

    @Autowired
    private RedisTokenUtil redisTokenUtil;

    public static final Logger logger = LoggerFactory.getLogger(RedisTokenUtil.class);

    public static final String TOKEN_KEY = "token";

    @Pointcut("execution(public * com.yuezhiming.demo_one.controller.*.*(..))")
    public void setToken(){
        logger.info("进入切面，准备就绪");
    }

    @Before("setToken()")
    public void before(JoinPoint point){
        MethodSignature signature = (MethodSignature) point.getSignature();
        ExtApiToken extApiToken = signature.getMethod().getDeclaredAnnotation(ExtApiToken.class);
        if(extApiToken != null) extApiToken();
    }

    private void extApiToken() {
        HttpServletRequest request = getRequest();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        String token = redisTokenUtil.getToken();
        response.setHeader("token", token);
        request.setAttribute("token", token);

    }


    @Around("setToken()")
    public Object before(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{

        //判断方法是否有加ExtApiIdempotent注解
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        ExtApiIdempotent extApiIdempotent = signature.getMethod().getDeclaredAnnotation(ExtApiIdempotent.class);
        if(extApiIdempotent == null) return proceedingJoinPoint.proceed();
        if(!extApiIdempotent(extApiIdempotent)) return null;
        //放行方法
        Object proceed = proceedingJoinPoint.proceed();
        return proceed;

    }

    public boolean extApiIdempotent(ExtApiIdempotent extApiIdempotent) throws IOException{
        HttpServletRequest request = getRequest();
        String token = null;
        if(extApiIdempotent != null){
            String type = extApiIdempotent.value();
            type = type == null ? ConstantUtils.EXTAPIHEAD : type;
            if (type.equals(ConstantUtils.EXTAPIHEAD)) {
                token = request.getHeader("token");
            }else {
                token = request.getParameter("token");
            }
        }

        if(StringUtils.isEmpty(token)){
            response("认证参数缺失");
            return false;
        }
        if(!redisTokenUtil.isExistToken.test(token)){
            response("认证失败，请重新刷新页面");
            return false;
        }
        return true;
    }
    private void response(String msg) throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        try {
            writer.println(msg);
        }catch(Exception e){

        }finally {
            writer.close();
        }
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }




}
