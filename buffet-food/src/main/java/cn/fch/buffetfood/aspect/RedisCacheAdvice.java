package cn.fch.buffetfood.aspect;

import cn.fch.buffetcommon.aspect.AfterClearCache;
import cn.fch.buffetcommon.aspect.BeforeClearCache;
import cn.fch.buffetcommon.aspect.Cache;
import cn.fch.buffetcommon.utils.RedissonUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @program: BuffetOrder
 * @description: redis缓存增强方法
 * @CreatedBy: fch
 * @create: 2023-02-02 18:47
 **/
@Aspect
@Component
@RequiredArgsConstructor
public class RedisCacheAdvice {

    @Pointcut("@annotation(cn.fch.buffetcommon.aspect.BeforeClearCache)")
    private void BeforeClearCacheByKey() {}

    @Before("BeforeClearCacheByKey()")
    public void beforeClearCache(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        BeforeClearCache cache = method.getAnnotation(BeforeClearCache.class);
        RedissonUtils.delKey(cache.key());
    }

    @Pointcut("@annotation(cn.fch.buffetcommon.aspect.AfterClearCache)")
    private void afterClearCacheByKey() {}

    @After("afterClearCacheByKey()")
    public void afterClearCache(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AfterClearCache cache = method.getAnnotation(AfterClearCache.class);
        if (cache.fromArg()){
            RedissonUtils.delKey("buffetorder:" + joinPoint.getArgs()[cache.keyIndex()]);
        }else {
            RedissonUtils.delKey(cache.key());
        }
    }

    @Pointcut("@annotation(cn.fch.buffetcommon.aspect.Cache)")
    public void cachePoint() {
    }

    @Around("cachePoint()")
    public Object cacheAround(ProceedingJoinPoint jp) throws Throwable {
        // 从切入点中获取方法签名
        MethodSignature signature = (MethodSignature) jp.getSignature();
        // 获取到方法对象
        Method method = signature.getMethod();
        Cache cache = method.getAnnotation(Cache.class);
        // 获取注解中的值 获取时间
        Object[] args = jp.getArgs();
        String key = StringUtils.hasText(cache.key()) ? cache.key() : String.format("buffetorder:%s", args[cache.keyIndex()].toString());
        // 从redis中获取数据
        Object result = RedissonUtils.getStr(key);
        if (Objects.isNull(result)) {
            // 从数据库取数据 让目标方法继续执行
            result = jp.proceed();
            RedissonUtils.setStr(key, result, cache.ttl());
        }
        return result;
    }
}
