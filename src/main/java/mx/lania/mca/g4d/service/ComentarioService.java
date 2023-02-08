package mx.lania.mca.g4d.service;

import java.util.Optional;
import mx.lania.mca.g4d.domain.Comentario;
import mx.lania.mca.g4d.repository.ComentarioRepository;
import mx.lania.mca.g4d.service.dto.ComentarioDTO;
import mx.lania.mca.g4d.service.mapper.ComentarioMapper;
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

    private final ComentarioMapper comentarioMapper;

    public ComentarioService(ComentarioRepository comentarioRepository, ComentarioMapper comentarioMapper) {
        this.comentarioRepository = comentarioRepository;
        this.comentarioMapper = comentarioMapper;
    }

    /**
     * Save a comentario.
     *
     * @param comentarioDTO the entity to save.
     * @return the persisted entity.
     */
    public ComentarioDTO save(ComentarioDTO comentarioDTO) {
        log.debug("Request to save Comentario : {}", comentarioDTO);
        Comentario comentario = comentarioMapper.toEntity(comentarioDTO);
        comentario = comentarioRepository.save(comentario);
        return comentarioMapper.toDto(comentario);
    }

    /**
     * Update a comentario.
     *
     * @param comentarioDTO the entity to save.
     * @return the persisted entity.
     */
    public ComentarioDTO update(ComentarioDTO comentarioDTO) {
        log.debug("Request to update Comentario : {}", comentarioDTO);
        Comentario comentario = comentarioMapper.toEntity(comentarioDTO);
        comentario = comentarioRepository.save(comentario);
        return comentarioMapper.toDto(comentario);
    }

    /**
     * Partially update a comentario.
     *
     * @param comentarioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ComentarioDTO> partialUpdate(ComentarioDTO comentarioDTO) {
        log.debug("Request to partially update Comentario : {}", comentarioDTO);

        return comentarioRepository
            .findById(comentarioDTO.getId())
            .map(existingComentario -> {
                comentarioMapper.partialUpdate(existingComentario, comentarioDTO);

                return existingComentario;
            })
            .map(comentarioRepository::save)
            .map(comentarioMapper::toDto);
    }

    /**
     * Get all the comentarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ComentarioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Comentarios");
        return comentarioRepository.findAll(pageable).map(comentarioMapper::toDto);
    }

    /**
     * Get one comentario by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ComentarioDTO> findOne(Long id) {
        log.debug("Request to get Comentario : {}", id);
        return comentarioRepository.findById(id).map(comentarioMapper::toDto);
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
