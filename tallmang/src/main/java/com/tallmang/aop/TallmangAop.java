package com.tallmang.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TallmangAop {
	
	//@Around("execution()")
	public Object commonBeforeProcess(final ProceedingJoinPoint joinPoint) throws Throwable
	{
		Object aopObject = joinPoint.proceed();
		return aopObject;
	}
	

}
