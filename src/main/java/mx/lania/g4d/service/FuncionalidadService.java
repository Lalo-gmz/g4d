package mx.lania.g4d.service;

import java.util.Optional;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.repository.FuncionalidadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public FuncionalidadService(FuncionalidadRepository funcionalidadRepository) {
        this.funcionalidadRepository = funcionalidadRepository;
    }

    /**
     * Save a funcionalidad.
     *
     * @param funcionalidad the entity to save.
     * @return the persisted entity.
     */
    public Funcionalidad save(Funcionalidad funcionalidad) {
        log.debug("Request to save Funcionalidad : {}", funcionalidad);
        return funcionalidadRepository.save(funcionalidad);
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
                if (funcionalidad.getFechaEntrega() != null) {
                    existingFuncionalidad.setFechaEntrega(funcionalidad.getFechaEntrega());
                }
                if (funcionalidad.getCreado() != null) {
                    existingFuncionalidad.setCreado(funcionalidad.getCreado());
                }
                if (funcionalidad.getModificado() != null) {
                    existingFuncionalidad.setModificado(funcionalidad.getModificado());
                }

                return existingFuncionalidad;
            })
            .map(funcionalidadRepository::save);
    }

    /**
     * Get all the funcionalidads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Funcionalidad> findAll(Pageable pageable) {
        log.debug("Request to get all Funcionalidads");
        return funcionalidadRepository.findAll(pageable);
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
        return funcionalidadRepository.findById(id);
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
