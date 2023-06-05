package mx.lania.g4d.aspect;

import java.util.Optional;
import mx.lania.g4d.domain.Bitacora;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.domain.User;
import mx.lania.g4d.domain.enumeration.AccionBitacora;
import mx.lania.g4d.service.BitacoraService;
import mx.lania.g4d.service.FuncionalidadService;
import mx.lania.g4d.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class MethodWrapperLogger {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BitacoraService bitacoraService;

    private final UserService userService;

    private final FuncionalidadService funcionalidadService;

    public MethodWrapperLogger(BitacoraService bitacoraService, UserService userService, FuncionalidadService funcionalidadService) {
        this.bitacoraService = bitacoraService;
        this.userService = userService;
        this.funcionalidadService = funcionalidadService;
    }

    @Around("target(org.springframework.data.jpa.repository.JpaRepository)")
    public Object logRepositoryMethod(ProceedingJoinPoint jointPoint) throws Throwable {
        logger.debug("Entering repository {}", getName(jointPoint));

        /*



        Object[] arguments = jointPoint.getArgs();
        // Verificar si hay argumentos y si el primer argumento es el objeto que deseas obtener
        if (arguments.length > 0) {
            Object firstArgument = arguments[0];
    // execution(Object org.springframework.data.repository.CrudRepository.save(Object))
            // Verificar si el método es el de "Editar" y el objeto es el que necesitas
            if (getName(jointPoint).equals("CrudRepository.save(..)") && firstArgument instanceof Funcionalidad) {
                Funcionalidad objeto = (Funcionalidad) firstArgument;

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String login = authentication.getName();

                Optional<User> optionalUser = userService.findByLogin(login);
                if (optionalUser.isPresent()){
                    User user = optionalUser.get();

                    Optional<Funcionalidad> optionalfuncionalidad = funcionalidadService.findOne(objeto.getId());
                    if (optionalfuncionalidad.isPresent()){
                        Funcionalidad funcionalidad = optionalfuncionalidad.get();

                        registrarBitacora(funcionalidad, AccionBitacora.ACTUALIZACION.toString(), user);
                    }

                }


            }
        }






        switch (getName(jointPoint)) {
            case "FuncionalidadRepository.save(..)":
                System.out.println("Executing special method: findAtributoFuncionalidadByFuncionalidadId, by: ");
                break;
            default:
                //System.out.println("Executing :"+ getName(jointPoint));
                break;
        }
         */

        Object o = jointPoint.proceed();
        logger.debug("Exiting {}", getName(jointPoint));

        return o;
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logControllerMethod(ProceedingJoinPoint jointPoint) throws Throwable {
        logger.debug("Dispatching {}", getName(jointPoint));

        Object o = jointPoint.proceed();

        logger.debug("Exiting {}", getName(jointPoint));

        return o;
    }

    @Around("@within(org.springframework.stereotype.Service)")
    public Object logServiceMethod(ProceedingJoinPoint jointPoint) throws Throwable {
        logger.debug("Entering service {}", getName(jointPoint));

        Object o = jointPoint.proceed();

        logger.debug("Exiting {}", getName(jointPoint));

        return o;
    }

    private String getName(ProceedingJoinPoint jp) {
        return jp.getSignature().toShortString();
    }

    private void registrarBitacora(Funcionalidad funcionalidad, String accion, User usuario) {
        System.out.println(funcionalidad + " " + accion + " " + usuario);
        Bitacora b = new Bitacora();
        b.setUser(usuario);
        b.setAccion(accion);
        bitacoraService.save(b);
    }
}
