package com.mapreduce.profiler;


import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.mapreduce.Config;

@Aspect
public class AProfiler {

  @Pointcut("@annotation(profiler)")
  public void annotationPointCutDefinition(Profiler profiler) {}


  @Around("execution(* *(..)) && annotationPointCutDefinition(profiler)")
  public Object around(ProceedingJoinPoint point, Profiler profiler) throws Throwable {
    long start = System.currentTimeMillis();
    Object result = point.proceed();
    FileHandler fh = new FileHandler(Config.PROFILER_LOG_LOCATION, true);
    Logger logger = Logger.getLogger("");
    SimpleFormatter formatter = new SimpleFormatter();
    fh.setFormatter(formatter);
    logger.addHandler(fh);
    logger.info(String.format("%s(%s):%d ms", MethodSignature.class.cast(point.getSignature())
        .getMethod().getName(), profiler.name(), System.currentTimeMillis() - start));
    return result;
  }

}
