package com.tingyu.xblog.app.handler.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import com.tingyu.xblog.app.config.properties.XBlogProperties;
import com.tingyu.xblog.app.exception.ForbiddenException;
import com.tingyu.xblog.app.model.annotation.DisableOnCondition;
import com.tingyu.xblog.app.model.enums.Mode;

/**
 * 自定义注解DisableApi的切面
 *
 * @author guqing
 * @date 2020-02-14 14:08
 */
@Aspect
@Slf4j
@Component
public class DisableOnConditionAspect {

    private final XBlogProperties XBlogProperties;

    public DisableOnConditionAspect(XBlogProperties XBlogProperties) {
        this.XBlogProperties = XBlogProperties;
    }

    @Pointcut("@annotation(com.tingyu.xblog.app.model.annotation.DisableOnCondition)")
    public void pointcut() {
    }

    @Around("pointcut() && @annotation(disableApi)")
    public Object around(ProceedingJoinPoint joinPoint,
                         DisableOnCondition disableApi) throws Throwable {
        Mode mode = disableApi.mode();
        if (XBlogProperties.getMode().equals(mode)) {
            throw new ForbiddenException("禁止访问");
        }

        return joinPoint.proceed();
    }
}
