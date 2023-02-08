package mx.lania.mca.g4d.service;

import java.util.Optional;
import mx.lania.mca.g4d.domain.EstatusFuncionalidad;
import mx.lania.mca.g4d.repository.EstatusFuncionalidadRepository;
import mx.lania.mca.g4d.service.dto.EstatusFuncionalidadDTO;
import mx.lania.mca.g4d.service.mapper.EstatusFuncionalidadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final EstatusFuncionalidadMapper estatusFuncionalidadMapper;

    public EstatusFuncionalidadService(
        EstatusFuncionalidadRepository estatusFuncionalidadRepository,
        EstatusFuncionalidadMapper estatusFuncionalidadMapper
    ) {
        this.estatusFuncionalidadRepository = estatusFuncionalidadRepository;
        this.estatusFuncionalidadMapper = estatusFuncionalidadMapper;
    }

    /**
     * Save a estatusFuncionalidad.
     *
     * @param estatusFuncionalidadDTO the entity to save.
     * @return the persisted entity.
     */
    public EstatusFuncionalidadDTO save(EstatusFuncionalidadDTO estatusFuncionalidadDTO) {
        log.debug("Request to save EstatusFuncionalidad : {}", estatusFuncionalidadDTO);
        EstatusFuncionalidad estatusFuncionalidad = estatusFuncionalidadMapper.toEntity(estatusFuncionalidadDTO);
        estatusFuncionalidad = estatusFuncionalidadRepository.save(estatusFuncionalidad);
        return estatusFuncionalidadMapper.toDto(estatusFuncionalidad);
    }

    /**
     * Update a estatusFuncionalidad.
     *
     * @param estatusFuncionalidadDTO the entity to save.
     * @return the persisted entity.
     */
    public EstatusFuncionalidadDTO update(EstatusFuncionalidadDTO estatusFuncionalidadDTO) {
        log.debug("Request to update EstatusFuncionalidad : {}", estatusFuncionalidadDTO);
        EstatusFuncionalidad estatusFuncionalidad = estatusFuncionalidadMapper.toEntity(estatusFuncionalidadDTO);
        estatusFuncionalidad = estatusFuncionalidadRepository.save(estatusFuncionalidad);
        return estatusFuncionalidadMapper.toDto(estatusFuncionalidad);
    }

    /**
     * Partially update a estatusFuncionalidad.
     *
     * @param estatusFuncionalidadDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EstatusFuncionalidadDTO> partialUpdate(EstatusFuncionalidadDTO estatusFuncionalidadDTO) {
        log.debug("Request to partially update EstatusFuncionalidad : {}", estatusFuncionalidadDTO);

        return estatusFuncionalidadRepository
            .findById(estatusFuncionalidadDTO.getId())
            .map(existingEstatusFuncionalidad -> {
                estatusFuncionalidadMapper.partialUpdate(existingEstatusFuncionalidad, estatusFuncionalidadDTO);

                return existingEstatusFuncionalidad;
            })
            .map(estatusFuncionalidadRepository::save)
            .map(estatusFuncionalidadMapper::toDto);
    }

    /**
     * Get all the estatusFuncionalidads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EstatusFuncionalidadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EstatusFuncionalidads");
        return estatusFuncionalidadRepository.findAll(pageable).map(estatusFuncionalidadMapper::toDto);
    }

    /**
     * Get one estatusFuncionalidad by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EstatusFuncionalidadDTO> findOne(Long id) {
        log.debug("Request to get EstatusFuncionalidad : {}", id);
        return estatusFuncionalidadRepository.findById(id).map(estatusFuncionalidadMapper::toDto);
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
