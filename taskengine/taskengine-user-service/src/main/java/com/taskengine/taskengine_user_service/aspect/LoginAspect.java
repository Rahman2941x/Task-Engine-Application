package com.taskengine.taskengine_user_service.aspect;

import com.taskengine.taskengine_user_service.dto.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class LoginAspect {

    private static final Logger log = LoggerFactory.getLogger(LoginAspect.class);

    @Pointcut("@annotation(com.taskengine.taskengine_user_service.annotation.AuditLog)")
    public void LoggerPointCut(){}


    @AfterThrowing(
            pointcut = "LoggerPointCut()",
            throwing = "ex"
    )
    public void auditException(JoinPoint jp, Exception ex) throws Throwable {

        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request= attribute.getRequest();

        log.info("""
                Audit Exception
                Endpoint : {}
                Method : {}
                Agrs : {}
                Response : {}
                """,
                request.getRequestURI(),
                request.getMethod(),
                jp.getArgs(),
                ex.getMessage());
    }
}
