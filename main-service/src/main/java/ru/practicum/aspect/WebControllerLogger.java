package ru.practicum.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Aspect
@Slf4j
public class WebControllerLogger {

    @Pointcut("within(ru.practicum.web..*)")
    public void serviceMethod() { }

    @Before("serviceMethod()")
    public void callLogService(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        Object[] methodArgs = joinPoint.getArgs();
        log.info("");
        log.info("-------------------------------------------------------------------------");
        log.info("");
        log.info("Calling class " + className + ", method " + methodName + " with args:");
        Arrays.stream(methodArgs).forEach(e -> log.info("ARGUMENT: " + e));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        log.info("Current user roles: " + roles);
    }

}
