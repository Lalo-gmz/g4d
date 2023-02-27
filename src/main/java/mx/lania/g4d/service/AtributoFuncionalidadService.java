package mx.lania.g4d.service;

import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.AtributoFuncionalidad;
import mx.lania.g4d.repository.AtributoFuncionalidadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AtributoFuncionalidad}.
 */
@Service
@Transactional
public class AtributoFuncionalidadService {

    private final Logger log = LoggerFactory.getLogger(AtributoFuncionalidadService.class);

    private final AtributoFuncionalidadRepository atributoFuncionalidadRepository;

    public AtributoFuncionalidadService(AtributoFuncionalidadRepository atributoFuncionalidadRepository) {
        this.atributoFuncionalidadRepository = atributoFuncionalidadRepository;
    }

    /**
     * Save a atributoFuncionalidad.
     *
     * @param atributoFuncionalidad the entity to save.
     * @return the persisted entity.
     */
    public AtributoFuncionalidad save(AtributoFuncionalidad atributoFuncionalidad) {
        log.debug("Request to save AtributoFuncionalidad : {}", atributoFuncionalidad);
        return atributoFuncionalidadRepository.save(atributoFuncionalidad);
    }

    /**
     * Update a atributoFuncionalidad.
     *
     * @param atributoFuncionalidad the entity to save.
     * @return the persisted entity.
     */
    public AtributoFuncionalidad update(AtributoFuncionalidad atributoFuncionalidad) {
        log.debug("Request to update AtributoFuncionalidad : {}", atributoFuncionalidad);
        return atributoFuncionalidadRepository.save(atributoFuncionalidad);
    }

    /**
     * Partially update a atributoFuncionalidad.
     *
     * @param atributoFuncionalidad the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AtributoFuncionalidad> partialUpdate(AtributoFuncionalidad atributoFuncionalidad) {
        log.debug("Request to partially update AtributoFuncionalidad : {}", atributoFuncionalidad);

        return atributoFuncionalidadRepository
            .findById(atributoFuncionalidad.getId())
            .map(existingAtributoFuncionalidad -> {
                if (atributoFuncionalidad.getMarcado() != null) {
                    existingAtributoFuncionalidad.setMarcado(atributoFuncionalidad.getMarcado());
                }
                if (atributoFuncionalidad.getValor() != null) {
                    existingAtributoFuncionalidad.setValor(atributoFuncionalidad.getValor());
                }

                return existingAtributoFuncionalidad;
            })
            .map(atributoFuncionalidadRepository::save);
    }

    /**
     * Get all the atributoFuncionalidads.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AtributoFuncionalidad> findAll() {
        log.debug("Request to get all AtributoFuncionalidads");
        return atributoFuncionalidadRepository.findAll();
    }

    /**
     * Get one atributoFuncionalidad by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AtributoFuncionalidad> findOne(Long id) {
        log.debug("Request to get AtributoFuncionalidad : {}", id);
        return atributoFuncionalidadRepository.findById(id);
    }

    //findAtributoFuncionalidadByFuncionalidadId
    @Transactional(readOnly = true)
    public Optional<List<AtributoFuncionalidad>> findAtributoFuncionalidadByFuncionalidadId(Long id) {
        log.debug("Request to get AtributoFuncionalidad by funcionalidad id: {}", id);
        return atributoFuncionalidadRepository.findAtributoFuncionalidadByFuncionalidadId(id);
    }

    /**
     * Delete the atributoFuncionalidad by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AtributoFuncionalidad : {}", id);
        atributoFuncionalidadRepository.deleteById(id);
    }
}
