package mx.lania.g4d.service;

import java.util.Optional;
import mx.lania.g4d.domain.Etiqueta;
import mx.lania.g4d.repository.EtiquetaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Etiqueta}.
 */
@Service
@Transactional
public class EtiquetaService {

    private final Logger log = LoggerFactory.getLogger(EtiquetaService.class);

    private final EtiquetaRepository etiquetaRepository;

    public EtiquetaService(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    /**
     * Save a etiqueta.
     *
     * @param etiqueta the entity to save.
     * @return the persisted entity.
     */
    public Etiqueta save(Etiqueta etiqueta) {
        log.debug("Request to save Etiqueta : {}", etiqueta);
        return etiquetaRepository.save(etiqueta);
    }

    /**
     * Update a etiqueta.
     *
     * @param etiqueta the entity to save.
     * @return the persisted entity.
     */
    public Etiqueta update(Etiqueta etiqueta) {
        log.debug("Request to update Etiqueta : {}", etiqueta);
        return etiquetaRepository.save(etiqueta);
    }

    /**
     * Partially update a etiqueta.
     *
     * @param etiqueta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Etiqueta> partialUpdate(Etiqueta etiqueta) {
        log.debug("Request to partially update Etiqueta : {}", etiqueta);

        return etiquetaRepository
            .findById(etiqueta.getId())
            .map(existingEtiqueta -> {
                if (etiqueta.getNombre() != null) {
                    existingEtiqueta.setNombre(etiqueta.getNombre());
                }
                if (etiqueta.getPrioridad() != null) {
                    existingEtiqueta.setPrioridad(etiqueta.getPrioridad());
                }

                return existingEtiqueta;
            })
            .map(etiquetaRepository::save);
    }

    /**
     * Get all the etiquetas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Etiqueta> findAll(Pageable pageable) {
        log.debug("Request to get all Etiquetas");
        return etiquetaRepository.findAll(pageable);
    }

    /**
     * Get one etiqueta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Etiqueta> findOne(Long id) {
        log.debug("Request to get Etiqueta : {}", id);
        return etiquetaRepository.findById(id);
    }

    /**
     * Delete the etiqueta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Etiqueta : {}", id);
        etiquetaRepository.deleteById(id);
    }
}
