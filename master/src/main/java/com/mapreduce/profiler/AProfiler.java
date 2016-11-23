package com.mapreduce.profiler;


import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.mapreduce.config.Config;

@Aspect
public class AProfiler {

  @Pointcut("@annotation(profiler)")
  public void annotationPointCutDefinition(Profiler profiler) {}


  @Around("execution(* *(..)) && annotationPointCutDefinition(profiler)")
  public Object around(ProceedingJoinPoint point, Profiler profiler) throws Throwable {
    long start = System.currentTimeMillis();
    Object result = point.proceed();
    Logger logger = Logger.getLogger("");
    logger.setUseParentHandlers(false);
    LogManager.getLogManager().reset();
    FileHandler fileHandler = new FileHandler(Config.PROFILER_LOG_LOCATION, true);
    SimpleFormatter formatter = new SimpleFormatter();
    fileHandler.setFormatter(formatter);
    logger.addHandler(fileHandler);
    logger.info(String.format("%s(%s):%d ms", MethodSignature.class.cast(point.getSignature())
        .getMethod().getName(), profiler.name(), System.currentTimeMillis() - start));
    return result;
  }

}
