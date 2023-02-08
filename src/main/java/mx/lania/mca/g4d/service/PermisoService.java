package mx.lania.mca.g4d.service;

import java.util.Optional;
import mx.lania.mca.g4d.domain.Permiso;
import mx.lania.mca.g4d.repository.PermisoRepository;
import mx.lania.mca.g4d.service.dto.PermisoDTO;
import mx.lania.mca.g4d.service.mapper.PermisoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Permiso}.
 */
@Service
@Transactional
public class PermisoService {

    private final Logger log = LoggerFactory.getLogger(PermisoService.class);

    private final PermisoRepository permisoRepository;

    private final PermisoMapper permisoMapper;

    public PermisoService(PermisoRepository permisoRepository, PermisoMapper permisoMapper) {
        this.permisoRepository = permisoRepository;
        this.permisoMapper = permisoMapper;
    }

    /**
     * Save a permiso.
     *
     * @param permisoDTO the entity to save.
     * @return the persisted entity.
     */
    public PermisoDTO save(PermisoDTO permisoDTO) {
        log.debug("Request to save Permiso : {}", permisoDTO);
        Permiso permiso = permisoMapper.toEntity(permisoDTO);
        permiso = permisoRepository.save(permiso);
        return permisoMapper.toDto(permiso);
    }

    /**
     * Update a permiso.
     *
     * @param permisoDTO the entity to save.
     * @return the persisted entity.
     */
    public PermisoDTO update(PermisoDTO permisoDTO) {
        log.debug("Request to update Permiso : {}", permisoDTO);
        Permiso permiso = permisoMapper.toEntity(permisoDTO);
        permiso = permisoRepository.save(permiso);
        return permisoMapper.toDto(permiso);
    }

    /**
     * Partially update a permiso.
     *
     * @param permisoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PermisoDTO> partialUpdate(PermisoDTO permisoDTO) {
        log.debug("Request to partially update Permiso : {}", permisoDTO);

        return permisoRepository
            .findById(permisoDTO.getId())
            .map(existingPermiso -> {
                permisoMapper.partialUpdate(existingPermiso, permisoDTO);

                return existingPermiso;
            })
            .map(permisoRepository::save)
            .map(permisoMapper::toDto);
    }

    /**
     * Get all the permisos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PermisoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Permisos");
        return permisoRepository.findAll(pageable).map(permisoMapper::toDto);
    }

    /**
     * Get one permiso by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PermisoDTO> findOne(Long id) {
        log.debug("Request to get Permiso : {}", id);
        return permisoRepository.findById(id).map(permisoMapper::toDto);
    }

    /**
     * Delete the permiso by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Permiso : {}", id);
        permisoRepository.deleteById(id);
    }
}
