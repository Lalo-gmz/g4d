package mx.lania.g4d.service;

import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.EstatusFuncionalidad;
import mx.lania.g4d.repository.EstatusFuncionalidadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EstatusFuncionalidad}.
 */
@Service
@Transactional
public class EstatusFuncionalidadService {

    private final Logger log = LoggerFactory.getLogger(EstatusFuncionalidadService.class);

    private final EstatusFuncionalidadRepository estatusFuncionalidadRepository;

    public EstatusFuncionalidadService(EstatusFuncionalidadRepository estatusFuncionalidadRepository) {
        this.estatusFuncionalidadRepository = estatusFuncionalidadRepository;
    }

    /**
     * Save a estatusFuncionalidad.
     *
     * @param estatusFuncionalidad the entity to save.
     * @return the persisted entity.
     */
    public EstatusFuncionalidad save(EstatusFuncionalidad estatusFuncionalidad) {
        log.debug("Request to save EstatusFuncionalidad : {}", estatusFuncionalidad);
        return estatusFuncionalidadRepository.save(estatusFuncionalidad);
    }

    /**
     * Update a estatusFuncionalidad.
     *
     * @param estatusFuncionalidad the entity to save.
     * @return the persisted entity.
     */
    public EstatusFuncionalidad update(EstatusFuncionalidad estatusFuncionalidad) {
        log.debug("Request to update EstatusFuncionalidad : {}", estatusFuncionalidad);
        return estatusFuncionalidadRepository.save(estatusFuncionalidad);
    }

    /**
     * Partially update a estatusFuncionalidad.
     *
     * @param estatusFuncionalidad the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EstatusFuncionalidad> partialUpdate(EstatusFuncionalidad estatusFuncionalidad) {
        log.debug("Request to partially update EstatusFuncionalidad : {}", estatusFuncionalidad);

        return estatusFuncionalidadRepository
            .findById(estatusFuncionalidad.getId())
            .map(existingEstatusFuncionalidad -> {
                if (estatusFuncionalidad.getNombre() != null) {
                    existingEstatusFuncionalidad.setNombre(estatusFuncionalidad.getNombre());
                }

                return existingEstatusFuncionalidad;
            })
            .map(estatusFuncionalidadRepository::save);
    }

    /**
     * Get all the estatusFuncionalidads.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EstatusFuncionalidad> findAll() {
        log.debug("Request to get all EstatusFuncionalidads");
        return estatusFuncionalidadRepository.findAll();
    }

    /**
     * Get one estatusFuncionalidad by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EstatusFuncionalidad> findOne(Long id) {
        log.debug("Request to get EstatusFuncionalidad : {}", id);
        return estatusFuncionalidadRepository.findById(id);
    }

    /**
     * Delete the estatusFuncionalidad by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EstatusFuncionalidad : {}", id);
        estatusFuncionalidadRepository.deleteById(id);
    }
}
