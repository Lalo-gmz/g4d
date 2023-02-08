package mx.lania.mca.g4d.service;

import java.util.Optional;
import mx.lania.mca.g4d.domain.Configuracion;
import mx.lania.mca.g4d.repository.ConfiguracionRepository;
import mx.lania.mca.g4d.service.dto.ConfiguracionDTO;
import mx.lania.mca.g4d.service.mapper.ConfiguracionMapper;
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

    private final ConfiguracionMapper configuracionMapper;

    public ConfiguracionService(ConfiguracionRepository configuracionRepository, ConfiguracionMapper configuracionMapper) {
        this.configuracionRepository = configuracionRepository;
        this.configuracionMapper = configuracionMapper;
    }

    /**
     * Save a configuracion.
     *
     * @param configuracionDTO the entity to save.
     * @return the persisted entity.
     */
    public ConfiguracionDTO save(ConfiguracionDTO configuracionDTO) {
        log.debug("Request to save Configuracion : {}", configuracionDTO);
        Configuracion configuracion = configuracionMapper.toEntity(configuracionDTO);
        configuracion = configuracionRepository.save(configuracion);
        return configuracionMapper.toDto(configuracion);
    }

    /**
     * Update a configuracion.
     *
     * @param configuracionDTO the entity to save.
     * @return the persisted entity.
     */
    public ConfiguracionDTO update(ConfiguracionDTO configuracionDTO) {
        log.debug("Request to update Configuracion : {}", configuracionDTO);
        Configuracion configuracion = configuracionMapper.toEntity(configuracionDTO);
        configuracion = configuracionRepository.save(configuracion);
        return configuracionMapper.toDto(configuracion);
    }

    /**
     * Partially update a configuracion.
     *
     * @param configuracionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConfiguracionDTO> partialUpdate(ConfiguracionDTO configuracionDTO) {
        log.debug("Request to partially update Configuracion : {}", configuracionDTO);

        return configuracionRepository
            .findById(configuracionDTO.getId())
            .map(existingConfiguracion -> {
                configuracionMapper.partialUpdate(existingConfiguracion, configuracionDTO);

                return existingConfiguracion;
            })
            .map(configuracionRepository::save)
            .map(configuracionMapper::toDto);
    }

    /**
     * Get all the configuracions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfiguracionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Configuracions");
        return configuracionRepository.findAll(pageable).map(configuracionMapper::toDto);
    }

    /**
     * Get one configuracion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConfiguracionDTO> findOne(Long id) {
        log.debug("Request to get Configuracion : {}", id);
        return configuracionRepository.findById(id).map(configuracionMapper::toDto);
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
