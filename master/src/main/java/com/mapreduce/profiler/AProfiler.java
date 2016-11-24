package com.mapreduce.profiler;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.google.common.base.Objects;

@Aspect
public class AProfiler {

  @Pointcut("@annotation(profiler)")
  public void annotationPointCutDefinition(Profiler profiler) {}


  @Around("execution(* *(..)) && annotationPointCutDefinition(profiler)")
  public Object around(ProceedingJoinPoint point, Profiler profiler) throws Throwable {
    String methodName = MethodSignature.class.cast(point.getSignature()).getMethod().getName();
    long start = System.currentTimeMillis();
    if (Objects.equal(methodName, "main")) {
      Logging.get().info("START");
    }
    Object result = point.proceed();
    long end = System.currentTimeMillis() - start;
    Logging.get().info(String.format("%s(%s):%d ms", methodName, profiler.name(), end));
    if (Objects.equal(methodName, "main")) {
      Logging.get().info("END");
    }
    return result;
  }
}
