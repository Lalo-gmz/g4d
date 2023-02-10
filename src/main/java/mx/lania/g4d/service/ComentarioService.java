package mx.lania.g4d.service;

import java.util.Optional;
import mx.lania.g4d.domain.Comentario;
import mx.lania.g4d.repository.ComentarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Comentario}.
 */
@Service
@Transactional
public class ComentarioService {

    private final Logger log = LoggerFactory.getLogger(ComentarioService.class);

    private final ComentarioRepository comentarioRepository;

    public ComentarioService(ComentarioRepository comentarioRepository) {
        this.comentarioRepository = comentarioRepository;
    }

    /**
     * Save a comentario.
     *
     * @param comentario the entity to save.
     * @return the persisted entity.
     */
    public Comentario save(Comentario comentario) {
        log.debug("Request to save Comentario : {}", comentario);
        return comentarioRepository.save(comentario);
    }

    /**
     * Update a comentario.
     *
     * @param comentario the entity to save.
     * @return the persisted entity.
     */
    public Comentario update(Comentario comentario) {
        log.debug("Request to update Comentario : {}", comentario);
        return comentarioRepository.save(comentario);
    }

    /**
     * Partially update a comentario.
     *
     * @param comentario the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Comentario> partialUpdate(Comentario comentario) {
        log.debug("Request to partially update Comentario : {}", comentario);

        return comentarioRepository
            .findById(comentario.getId())
            .map(existingComentario -> {
                if (comentario.getMensaje() != null) {
                    existingComentario.setMensaje(comentario.getMensaje());
                }
                if (comentario.getCreado() != null) {
                    existingComentario.setCreado(comentario.getCreado());
                }
                if (comentario.getModificado() != null) {
                    existingComentario.setModificado(comentario.getModificado());
                }

                return existingComentario;
            })
            .map(comentarioRepository::save);
    }

    /**
     * Get all the comentarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Comentario> findAll(Pageable pageable) {
        log.debug("Request to get all Comentarios");
        return comentarioRepository.findAll(pageable);
    }

    /**
     * Get one comentario by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Comentario> findOne(Long id) {
        log.debug("Request to get Comentario : {}", id);
        return comentarioRepository.findById(id);
    }

    /**
     * Delete the comentario by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Comentario : {}", id);
        comentarioRepository.deleteById(id);
    }
}
