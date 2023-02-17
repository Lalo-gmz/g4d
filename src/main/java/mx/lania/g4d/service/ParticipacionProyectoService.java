package mx.lania.g4d.service;

import java.util.Optional;
import mx.lania.g4d.domain.ParticipacionProyecto;
import mx.lania.g4d.repository.ParticipacionProyectoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ParticipacionProyecto}.
 */
@Service
@Transactional
public class ParticipacionProyectoService {

    private final Logger log = LoggerFactory.getLogger(ParticipacionProyectoService.class);

    private final ParticipacionProyectoRepository participacionProyectoRepository;

    public ParticipacionProyectoService(ParticipacionProyectoRepository participacionProyectoRepository) {
        this.participacionProyectoRepository = participacionProyectoRepository;
    }

    /**
     * Save a participacionProyecto.
     *
     * @param participacionProyecto the entity to save.
     * @return the persisted entity.
     */
    public ParticipacionProyecto save(ParticipacionProyecto participacionProyecto) {
        log.debug("Request to save ParticipacionProyecto : {}", participacionProyecto);
        return participacionProyectoRepository.save(participacionProyecto);
    }

    /**
     * Update a participacionProyecto.
     *
     * @param participacionProyecto the entity to save.
     * @return the persisted entity.
     */
    public ParticipacionProyecto update(ParticipacionProyecto participacionProyecto) {
        log.debug("Request to update ParticipacionProyecto : {}", participacionProyecto);
        return participacionProyectoRepository.save(participacionProyecto);
    }

    /**
     * Partially update a participacionProyecto.
     *
     * @param participacionProyecto the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParticipacionProyecto> partialUpdate(ParticipacionProyecto participacionProyecto) {
        log.debug("Request to partially update ParticipacionProyecto : {}", participacionProyecto);

        return participacionProyectoRepository
            .findById(participacionProyecto.getId())
            .map(existingParticipacionProyecto -> {
                return existingParticipacionProyecto;
            })
            .map(participacionProyectoRepository::save);
    }

    /**
     * Get all the participacionProyectos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ParticipacionProyecto> findAll(Pageable pageable) {
        log.debug("Request to get all ParticipacionProyectos");
        return participacionProyectoRepository.findAll(pageable);
    }

    /**
     * Get one participacionProyecto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParticipacionProyecto> findOne(Long id) {
        log.debug("Request to get ParticipacionProyecto : {}", id);
        return participacionProyectoRepository.findById(id);
    }

    /**
     * Delete the participacionProyecto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ParticipacionProyecto : {}", id);
        participacionProyectoRepository.deleteById(id);
    }
}
