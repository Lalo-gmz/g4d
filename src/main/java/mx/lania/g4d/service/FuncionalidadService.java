package mx.lania.g4d.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.Bitacora;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.domain.User;
import mx.lania.g4d.domain.enumeration.AccionBitacora;
import mx.lania.g4d.repository.BitacoraRepository;
import mx.lania.g4d.repository.FuncionalidadRepository;
import mx.lania.g4d.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Funcionalidad}.
 */
@Service
@Transactional
public class FuncionalidadService {

    private final Logger log = LoggerFactory.getLogger(FuncionalidadService.class);

    private final FuncionalidadRepository funcionalidadRepository;
    private final BitacoraRepository bitacoraRepository;
    private final UserRepository userRepository;
    private final BitacoraService bitacoraService;

    public FuncionalidadService(
        FuncionalidadRepository funcionalidadRepository,
        BitacoraRepository bitacoraRepository,
        UserRepository userRepository,
        BitacoraService bitacoraService
    ) {
        this.funcionalidadRepository = funcionalidadRepository;
        this.bitacoraRepository = bitacoraRepository;
        this.userRepository = userRepository;
        this.bitacoraService = bitacoraService;
    }

    /**
     * Save a funcionalidad.
     *
     * @param funcionalidad the entity to save.
     * @return the persisted entity.
     */
    public Funcionalidad save(Funcionalidad funcionalidad) {
        log.debug("Request to save Funcionalidad : {}", funcionalidad);
        // guardar accion en bitacora
        // guardar bitacora

        Funcionalidad result = funcionalidadRepository.save(funcionalidad);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        User user = null;
        Optional<User> optionalUser = userRepository.findOneByLogin(login);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            Bitacora bitacora = new Bitacora();
            bitacora.setFuncionalidad(result);
            bitacora.setAccion(String.valueOf(AccionBitacora.ALTA));
            bitacora.setUser(user);
            bitacora.setCreado(Instant.now());
            bitacoraService.save(bitacora);
        }

        return result;
    }

    public List<Funcionalidad> saveAll(List<Funcionalidad> funcionalidades) {
        return funcionalidadRepository.saveAll(funcionalidades);
    }

    /**
     * Update a funcionalidad.
     *
     * @param funcionalidad the entity to save.
     * @return the persisted entity.
     */
    public Funcionalidad update(Funcionalidad funcionalidad) {
        log.debug("Request to update Funcionalidad : {}", funcionalidad);
        return funcionalidadRepository.save(funcionalidad);
    }

    /**
     * Partially update a funcionalidad.
     *
     * @param funcionalidad the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Funcionalidad> partialUpdate(Funcionalidad funcionalidad) {
        log.debug("Request to partially update Funcionalidad : {}", funcionalidad);

        return funcionalidadRepository
            .findById(funcionalidad.getId())
            .map(existingFuncionalidad -> {
                if (funcionalidad.getNombre() != null) {
                    existingFuncionalidad.setNombre(funcionalidad.getNombre());
                }
                if (funcionalidad.getDescripcion() != null) {
                    existingFuncionalidad.setDescripcion(funcionalidad.getDescripcion());
                }
                if (funcionalidad.getUrlGitLab() != null) {
                    existingFuncionalidad.setUrlGitLab(funcionalidad.getUrlGitLab());
                }
                if (funcionalidad.getCreado() != null) {
                    existingFuncionalidad.setCreado(funcionalidad.getCreado());
                }
                if (funcionalidad.getModificado() != null) {
                    existingFuncionalidad.setModificado(funcionalidad.getModificado());
                }
                if (funcionalidad.getEstatusFuncionalidad() != null) {
                    existingFuncionalidad.setEstatusFuncionalidad(funcionalidad.getEstatusFuncionalidad());
                }
                if (funcionalidad.getPrioridad() != null) {
                    existingFuncionalidad.setPrioridad(funcionalidad.getPrioridad());
                }

                return existingFuncionalidad;
            })
            .map(funcionalidadRepository::save);
    }

    /**
     * Get all the funcionalidads.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Funcionalidad> findAll() {
        log.debug("Request to get all Funcionalidads");
        return funcionalidadRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Funcionalidad> findAllByIteracionId(Long iteracionId) {
        log.debug("Request to get all Funcionalidads");
        return funcionalidadRepository.findAllWithEagerRelationshipsByIteracionId(iteracionId);
    }

    /**
     * Get all the funcionalidads with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Funcionalidad> findAllWithEagerRelationships(Pageable pageable) {
        return funcionalidadRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one funcionalidad by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Funcionalidad> findOne(Long id) {
        log.debug("Request to get Funcionalidad : {}", id);
        return funcionalidadRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the funcionalidad by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Funcionalidad : {}", id);
        funcionalidadRepository.deleteById(id);
    }
}
