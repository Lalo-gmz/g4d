package mx.lania.g4d.service;

import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.Captura;
import mx.lania.g4d.repository.CapturaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Captura}.
 */
@Service
@Transactional
public class CapturaService {

    private final Logger log = LoggerFactory.getLogger(CapturaService.class);

    private final CapturaRepository capturaRepository;

    public CapturaService(CapturaRepository capturaRepository) {
        this.capturaRepository = capturaRepository;
    }

    /**
     * Save a captura.
     *
     * @param captura the entity to save.
     * @return the persisted entity.
     */
    public Captura save(Captura captura) {
        log.debug("Request to save Captura : {}", captura);
        return capturaRepository.save(captura);
    }

    /**
     * Update a captura.
     *
     * @param captura the entity to save.
     * @return the persisted entity.
     */
    public Captura update(Captura captura) {
        log.debug("Request to update Captura : {}", captura);
        return capturaRepository.save(captura);
    }

    /**
     * Partially update a captura.
     *
     * @param captura the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Captura> partialUpdate(Captura captura) {
        log.debug("Request to partially update Captura : {}", captura);

        return capturaRepository
            .findById(captura.getId())
            .map(existingCaptura -> {
                if (captura.getFuncionalidades() != null) {
                    existingCaptura.setFuncionalidades(captura.getFuncionalidades());
                }
                if (captura.getEstatus() != null) {
                    existingCaptura.setEstatus(captura.getEstatus());
                }
                if (captura.getFecha() != null) {
                    existingCaptura.setFecha(captura.getFecha());
                }

                return existingCaptura;
            })
            .map(capturaRepository::save);
    }

    /**
     * Get all the capturas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Captura> findAll() {
        log.debug("Request to get all Capturas");
        return capturaRepository.findAll();
    }

    /**
     * Get one captura by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Captura> findOne(Long id) {
        log.debug("Request to get Captura : {}", id);
        return capturaRepository.findById(id);
    }

    /**
     * Delete the captura by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Captura : {}", id);
        capturaRepository.deleteById(id);
    }
}
