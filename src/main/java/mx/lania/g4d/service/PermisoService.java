package mx.lania.g4d.service;

import java.util.Optional;
import mx.lania.g4d.domain.Permiso;
import mx.lania.g4d.repository.PermisoRepository;
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

    public PermisoService(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    /**
     * Save a permiso.
     *
     * @param permiso the entity to save.
     * @return the persisted entity.
     */
    public Permiso save(Permiso permiso) {
        log.debug("Request to save Permiso : {}", permiso);
        return permisoRepository.save(permiso);
    }

    /**
     * Update a permiso.
     *
     * @param permiso the entity to save.
     * @return the persisted entity.
     */
    public Permiso update(Permiso permiso) {
        log.debug("Request to update Permiso : {}", permiso);
        return permisoRepository.save(permiso);
    }

    /**
     * Partially update a permiso.
     *
     * @param permiso the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Permiso> partialUpdate(Permiso permiso) {
        log.debug("Request to partially update Permiso : {}", permiso);

        return permisoRepository
            .findById(permiso.getId())
            .map(existingPermiso -> {
                if (permiso.getNombre() != null) {
                    existingPermiso.setNombre(permiso.getNombre());
                }

                return existingPermiso;
            })
            .map(permisoRepository::save);
    }

    /**
     * Get all the permisos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Permiso> findAll(Pageable pageable) {
        log.debug("Request to get all Permisos");
        return permisoRepository.findAll(pageable);
    }

    /**
     * Get one permiso by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Permiso> findOne(Long id) {
        log.debug("Request to get Permiso : {}", id);
        return permisoRepository.findById(id);
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
