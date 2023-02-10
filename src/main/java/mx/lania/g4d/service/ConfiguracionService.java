package mx.lania.g4d.service;

import java.util.Optional;
import mx.lania.g4d.domain.Configuracion;
import mx.lania.g4d.repository.ConfiguracionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Configuracion}.
 */
@Service
@Transactional
public class ConfiguracionService {

    private final Logger log = LoggerFactory.getLogger(ConfiguracionService.class);

    private final ConfiguracionRepository configuracionRepository;

    public ConfiguracionService(ConfiguracionRepository configuracionRepository) {
        this.configuracionRepository = configuracionRepository;
    }

    /**
     * Save a configuracion.
     *
     * @param configuracion the entity to save.
     * @return the persisted entity.
     */
    public Configuracion save(Configuracion configuracion) {
        log.debug("Request to save Configuracion : {}", configuracion);
        return configuracionRepository.save(configuracion);
    }

    /**
     * Update a configuracion.
     *
     * @param configuracion the entity to save.
     * @return the persisted entity.
     */
    public Configuracion update(Configuracion configuracion) {
        log.debug("Request to update Configuracion : {}", configuracion);
        return configuracionRepository.save(configuracion);
    }

    /**
     * Partially update a configuracion.
     *
     * @param configuracion the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Configuracion> partialUpdate(Configuracion configuracion) {
        log.debug("Request to partially update Configuracion : {}", configuracion);

        return configuracionRepository
            .findById(configuracion.getId())
            .map(existingConfiguracion -> {
                if (configuracion.getClave() != null) {
                    existingConfiguracion.setClave(configuracion.getClave());
                }
                if (configuracion.getValor() != null) {
                    existingConfiguracion.setValor(configuracion.getValor());
                }

                return existingConfiguracion;
            })
            .map(configuracionRepository::save);
    }

    /**
     * Get all the configuracions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Configuracion> findAll(Pageable pageable) {
        log.debug("Request to get all Configuracions");
        return configuracionRepository.findAll(pageable);
    }

    /**
     * Get one configuracion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Configuracion> findOne(Long id) {
        log.debug("Request to get Configuracion : {}", id);
        return configuracionRepository.findById(id);
    }

    /**
     * Delete the configuracion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Configuracion : {}", id);
        configuracionRepository.deleteById(id);
    }
}
