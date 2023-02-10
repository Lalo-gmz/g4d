package mx.lania.g4d.service;

import java.util.Optional;
import mx.lania.g4d.domain.Proyecto;
import mx.lania.g4d.repository.ProyectoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Proyecto}.
 */
@Service
@Transactional
public class ProyectoService {

    private final Logger log = LoggerFactory.getLogger(ProyectoService.class);

    private final ProyectoRepository proyectoRepository;

    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    /**
     * Save a proyecto.
     *
     * @param proyecto the entity to save.
     * @return the persisted entity.
     */
    public Proyecto save(Proyecto proyecto) {
        log.debug("Request to save Proyecto : {}", proyecto);
        return proyectoRepository.save(proyecto);
    }

    /**
     * Update a proyecto.
     *
     * @param proyecto the entity to save.
     * @return the persisted entity.
     */
    public Proyecto update(Proyecto proyecto) {
        log.debug("Request to update Proyecto : {}", proyecto);
        return proyectoRepository.save(proyecto);
    }

    /**
     * Partially update a proyecto.
     *
     * @param proyecto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Proyecto> partialUpdate(Proyecto proyecto) {
        log.debug("Request to partially update Proyecto : {}", proyecto);

        return proyectoRepository
            .findById(proyecto.getId())
            .map(existingProyecto -> {
                if (proyecto.getNombre() != null) {
                    existingProyecto.setNombre(proyecto.getNombre());
                }
                if (proyecto.getIdProyectoGitLab() != null) {
                    existingProyecto.setIdProyectoGitLab(proyecto.getIdProyectoGitLab());
                }

                return existingProyecto;
            })
            .map(proyectoRepository::save);
    }

    /**
     * Get all the proyectos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Proyecto> findAll(Pageable pageable) {
        log.debug("Request to get all Proyectos");
        return proyectoRepository.findAll(pageable);
    }

    /**
     * Get one proyecto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Proyecto> findOne(Long id) {
        log.debug("Request to get Proyecto : {}", id);
        return proyectoRepository.findById(id);
    }

    /**
     * Delete the proyecto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Proyecto : {}", id);
        proyectoRepository.deleteById(id);
    }
}
