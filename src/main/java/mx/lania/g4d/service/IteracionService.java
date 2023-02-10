package mx.lania.g4d.service;

import java.util.Optional;
import mx.lania.g4d.domain.Iteracion;
import mx.lania.g4d.repository.IteracionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Iteracion}.
 */
@Service
@Transactional
public class IteracionService {

    private final Logger log = LoggerFactory.getLogger(IteracionService.class);

    private final IteracionRepository iteracionRepository;

    public IteracionService(IteracionRepository iteracionRepository) {
        this.iteracionRepository = iteracionRepository;
    }

    /**
     * Save a iteracion.
     *
     * @param iteracion the entity to save.
     * @return the persisted entity.
     */
    public Iteracion save(Iteracion iteracion) {
        log.debug("Request to save Iteracion : {}", iteracion);
        return iteracionRepository.save(iteracion);
    }

    /**
     * Update a iteracion.
     *
     * @param iteracion the entity to save.
     * @return the persisted entity.
     */
    public Iteracion update(Iteracion iteracion) {
        log.debug("Request to update Iteracion : {}", iteracion);
        return iteracionRepository.save(iteracion);
    }

    /**
     * Partially update a iteracion.
     *
     * @param iteracion the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Iteracion> partialUpdate(Iteracion iteracion) {
        log.debug("Request to partially update Iteracion : {}", iteracion);

        return iteracionRepository
            .findById(iteracion.getId())
            .map(existingIteracion -> {
                if (iteracion.getNombre() != null) {
                    existingIteracion.setNombre(iteracion.getNombre());
                }
                if (iteracion.getInicio() != null) {
                    existingIteracion.setInicio(iteracion.getInicio());
                }
                if (iteracion.getFin() != null) {
                    existingIteracion.setFin(iteracion.getFin());
                }

                return existingIteracion;
            })
            .map(iteracionRepository::save);
    }

    /**
     * Get all the iteracions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Iteracion> findAll(Pageable pageable) {
        log.debug("Request to get all Iteracions");
        return iteracionRepository.findAll(pageable);
    }

    /**
     * Get one iteracion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Iteracion> findOne(Long id) {
        log.debug("Request to get Iteracion : {}", id);
        return iteracionRepository.findById(id);
    }

    /**
     * Delete the iteracion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Iteracion : {}", id);
        iteracionRepository.deleteById(id);
    }
}
