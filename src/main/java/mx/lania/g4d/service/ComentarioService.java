package mx.lania.g4d.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.Comentario;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.domain.User;
import mx.lania.g4d.repository.ComentarioRepository;
import mx.lania.g4d.repository.FuncionalidadRepository;
import mx.lania.g4d.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Comentario}.
 */
@Service
@Transactional
public class ComentarioService {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    private final Logger log = LoggerFactory.getLogger(ComentarioService.class);

    private final ComentarioRepository comentarioRepository;
    private final UserRepository userRepository;
    private final FuncionalidadRepository funcionalidadRepository;

    public ComentarioService(
        ComentarioRepository comentarioRepository,
        UserRepository userRepository,
        FuncionalidadRepository funcionalidadRepository
    ) {
        this.comentarioRepository = comentarioRepository;
        this.userRepository = userRepository;
        this.funcionalidadRepository = funcionalidadRepository;
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

    public Comentario saveByFuncionalidad(String login, Comentario comentario) {
        log.debug("Request to save Comentario : {}", comentario);

        Optional<User> user = userRepository.findOneByLogin(login);
        if (user.isPresent()) {
            comentario.setUser(user.get());
        }

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
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Comentario> findAll() {
        log.debug("Request to get all Comentarios");
        return comentarioRepository.findAll();
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

    @Transactional(readOnly = true)
    public Optional<List<Comentario>> findAllByFuncId(Long id) {
        log.debug("Request to get All Comentario bu funcionalidad id: {}", id);
        return comentarioRepository.findAllByFuncionalidadId(id);
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
