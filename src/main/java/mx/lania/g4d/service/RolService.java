package mx.lania.g4d.service;

import java.util.Optional;
import mx.lania.g4d.domain.Rol;
import mx.lania.g4d.repository.RolRepository;
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

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    /**
     * Save a rol.
     *
     * @param rol the entity to save.
     * @return the persisted entity.
     */
    public Rol save(Rol rol) {
        log.debug("Request to save Rol : {}", rol);
        return rolRepository.save(rol);
    }

    /**
     * Update a rol.
     *
     * @param rol the entity to save.
     * @return the persisted entity.
     */
    public Rol update(Rol rol) {
        log.debug("Request to update Rol : {}", rol);
        return rolRepository.save(rol);
    }

    /**
     * Partially update a rol.
     *
     * @param rol the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Rol> partialUpdate(Rol rol) {
        log.debug("Request to partially update Rol : {}", rol);

        return rolRepository
            .findById(rol.getId())
            .map(existingRol -> {
                if (rol.getNombre() != null) {
                    existingRol.setNombre(rol.getNombre());
                }

                return existingRol;
            })
            .map(rolRepository::save);
    }

    /**
     * Get all the rols.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Rol> findAll(Pageable pageable) {
        log.debug("Request to get all Rols");
        return rolRepository.findAll(pageable);
    }

    /**
     * Get one rol by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Rol> findOne(Long id) {
        log.debug("Request to get Rol : {}", id);
        return rolRepository.findById(id);
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
