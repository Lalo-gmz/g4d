package mx.lania.g4d.service;

import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.Etiqueta;
import mx.lania.g4d.repository.EtiquetaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
                if (etiqueta.getColor() != null) {
                    existingEtiqueta.setColor(etiqueta.getColor());
                }

                return existingEtiqueta;
            })
            .map(etiquetaRepository::save);
    }

    /**
     * Get all the etiquetas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Etiqueta> findAll() {
        log.debug("Request to get all Etiquetas");
        return etiquetaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<List<Etiqueta>> findAllByFuncionalidadId(Long id) {
        log.debug("Request to get all Etiquetas by funcionalidad id");
        return etiquetaRepository.findAllByFuncionalidadId(id);
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
