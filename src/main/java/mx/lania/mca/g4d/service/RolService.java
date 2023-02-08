package mx.lania.mca.g4d.service;

import java.util.Optional;
import mx.lania.mca.g4d.domain.Rol;
import mx.lania.mca.g4d.repository.RolRepository;
import mx.lania.mca.g4d.service.dto.RolDTO;
import mx.lania.mca.g4d.service.mapper.RolMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Rol}.
 */
@Service
@Transactional
public class RolService {

    private final Logger log = LoggerFactory.getLogger(RolService.class);

    private final RolRepository rolRepository;

    private final RolMapper rolMapper;

    public RolService(RolRepository rolRepository, RolMapper rolMapper) {
        this.rolRepository = rolRepository;
        this.rolMapper = rolMapper;
    }

    /**
     * Save a rol.
     *
     * @param rolDTO the entity to save.
     * @return the persisted entity.
     */
    public RolDTO save(RolDTO rolDTO) {
        log.debug("Request to save Rol : {}", rolDTO);
        Rol rol = rolMapper.toEntity(rolDTO);
        rol = rolRepository.save(rol);
        return rolMapper.toDto(rol);
    }

    /**
     * Update a rol.
     *
     * @param rolDTO the entity to save.
     * @return the persisted entity.
     */
    public RolDTO update(RolDTO rolDTO) {
        log.debug("Request to update Rol : {}", rolDTO);
        Rol rol = rolMapper.toEntity(rolDTO);
        rol = rolRepository.save(rol);
        return rolMapper.toDto(rol);
    }

    /**
     * Partially update a rol.
     *
     * @param rolDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RolDTO> partialUpdate(RolDTO rolDTO) {
        log.debug("Request to partially update Rol : {}", rolDTO);

        return rolRepository
            .findById(rolDTO.getId())
            .map(existingRol -> {
                rolMapper.partialUpdate(existingRol, rolDTO);

                return existingRol;
            })
            .map(rolRepository::save)
            .map(rolMapper::toDto);
    }

    /**
     * Get all the rols.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RolDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rols");
        return rolRepository.findAll(pageable).map(rolMapper::toDto);
    }

    /**
     * Get one rol by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RolDTO> findOne(Long id) {
        log.debug("Request to get Rol : {}", id);
        return rolRepository.findById(id).map(rolMapper::toDto);
    }

    /**
     * Delete the rol by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Rol : {}", id);
        rolRepository.deleteById(id);
    }
}
