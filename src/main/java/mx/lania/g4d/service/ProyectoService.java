package mx.lania.g4d.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import mx.lania.g4d.domain.ParticipacionProyecto;
import mx.lania.g4d.domain.Proyecto;
import mx.lania.g4d.domain.User;
import mx.lania.g4d.repository.ProyectoRepository;
import mx.lania.g4d.repository.UserRepository;
import mx.lania.g4d.service.utils.GitLabService;
import mx.lania.g4d.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final ParticipacionProyectoService participacionProyectoService;
    private final UserRepository userRepository;

    private final GitLabService gitLabService;

    public ProyectoService(
        ProyectoRepository proyectoRepository,
        ParticipacionProyectoService participacionProyectoService,
        UserRepository userRepository,
        GitLabService gitLabService
    ) {
        this.proyectoRepository = proyectoRepository;
        this.participacionProyectoService = participacionProyectoService;
        this.userRepository = userRepository;
        this.gitLabService = gitLabService;
    }

    /**
     * Save a proyecto.
     *
     * @param proyecto the entity to save.
     * @return the persisted entity.
     */
    public Proyecto save(Proyecto proyecto) {
        log.debug("Request to save Proyecto : {}", proyecto);
        Proyecto res = new Proyecto();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        if (proyecto.getIdProyectoGitLab().equalsIgnoreCase("NUEVO")) {
            Map<String, String> idGitLabProject = gitLabService.saveProjecto(proyecto.getNombre());
            Optional<Map.Entry<String, String>> result = idGitLabProject
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals("error"))
                .findFirst();

            if (result.isPresent()) {
                return new Proyecto();
            }
            proyecto.setIdProyectoGitLab(idGitLabProject.get("id"));
            proyecto.setEnlaceGitLab(idGitLabProject.get("web_url"));
        }

        Optional<User> optionalUser = userRepository.findOneByLogin(login);
        res = proyectoRepository.save(proyecto);
        ParticipacionProyecto participacionProyecto = new ParticipacionProyecto();
        participacionProyecto.setProyecto(res);
        participacionProyecto.setEsAdmin(true);
        optionalUser.ifPresent(participacionProyecto::setUsuario);

        participacionProyectoService.save(participacionProyecto);

        return res;
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
                if (proyecto.getCreado() != null) {
                    existingProyecto.setCreado(proyecto.getCreado());
                }
                if (proyecto.getModificado() != null) {
                    existingProyecto.setModificado(proyecto.getModificado());
                }
                if (proyecto.getEnlaceGitLab() != null) {
                    existingProyecto.setEnlaceGitLab(proyecto.getEnlaceGitLab());
                }

                return existingProyecto;
            })
            .map(proyectoRepository::save);
    }

    /**
     * Get all the proyectos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Proyecto> findAll() {
        log.debug("Request to get all Proyectos");
        return proyectoRepository.findAll();
    }

    /**
     * Get all the proyectos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Proyecto> findAllWithEagerRelationships(Pageable pageable) {
        return proyectoRepository.findAllWithEagerRelationships(pageable);
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
        return proyectoRepository.findOneWithEagerRelationships(id);
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
