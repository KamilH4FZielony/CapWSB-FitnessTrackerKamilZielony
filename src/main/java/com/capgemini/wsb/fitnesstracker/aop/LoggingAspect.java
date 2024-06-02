package com.capgemini.wsb.fitnesstracker.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging method invocations in service classes.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Logs method invocations for methods in classes annotated with @Service.
     *
     * @param joinPoint the join point representing the method invocation
     */
    @Before("execution(* com.capgemini.wsb.fitnesstracker..*Service.*(..))")
    public void logMethodInvocation(JoinPoint joinPoint) {
        String methodInfo = getMethodInfo(joinPoint);
        log.info("Before method invocation: {}", methodInfo);
    }

    /**
     * Logs method invocations and return values for methods in classes annotated with @Service.
     *
     * @param joinPoint the join point representing the method invocation
     * @param result    the value returned by the method
     */
    @AfterReturning(pointcut = "execution(* com.capgemini.wsb.fitnesstracker..*Service.*(..))", returning = "result")
    public void logMethodResult(JoinPoint joinPoint, Object result) {
        String methodInfo = getMethodInfo(joinPoint);
        log.info("After method invocation: {}, Returned value: {}", methodInfo, result);
    }

    /**
     * Builds the method information string in the format (returnType className.methodName(parameterType1 parameterName1, ...)).
     *
     * @param joinPoint the join point representing the method invocation
     * @return the method information string
     */
    private String getMethodInfo(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String returnType = methodSignature.getReturnType().getSimpleName();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        Object[] args = joinPoint.getArgs();
        StringBuilder parameterBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            String parameterType = methodSignature.getParameterTypes()[i].getSimpleName();
            String parameterName = methodSignature.getParameterNames()[i];
            parameterBuilder.append(parameterType).append(" ").append(parameterName);
            if (i < args.length - 1) {
                parameterBuilder.append(", ");
            }
        }

        return String.format("%s %s.%s(%s)", returnType, className, methodName, parameterBuilder.toString());
    }
}