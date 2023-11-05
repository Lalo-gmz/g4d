package mx.lania.g4d.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodWrapperLogger {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String textExiting = "Exiting {}";

    @Around("target(org.springframework.data.jpa.repository.JpaRepository)")
    public Object logRepositoryMethod(ProceedingJoinPoint jointPoint) throws Throwable {
        logger.debug("Entering repository {}", getName(jointPoint));

        Object o = jointPoint.proceed();
        logger.debug(textExiting, getName(jointPoint));

        return o;
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logControllerMethod(ProceedingJoinPoint jointPoint) throws Throwable {
        logger.debug("Dispatching {}", getName(jointPoint));

        Object o = jointPoint.proceed();

        logger.debug(textExiting, getName(jointPoint));

        return o;
    }

    @Around("@within(org.springframework.stereotype.Service)")
    public Object logServiceMethod(ProceedingJoinPoint jointPoint) throws Throwable {
        logger.debug("Entering service {}", getName(jointPoint));

        Object o = jointPoint.proceed();

        logger.debug(textExiting, getName(jointPoint));

        return o;
    }

    private String getName(ProceedingJoinPoint jp) {
        return jp.getSignature().toShortString();
    }
}
