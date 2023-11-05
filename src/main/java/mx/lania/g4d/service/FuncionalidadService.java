package mx.lania.g4d.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import mx.lania.g4d.domain.Bitacora;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.domain.Proyecto;
import mx.lania.g4d.domain.User;
import mx.lania.g4d.domain.enumeration.AccionBitacora;
import mx.lania.g4d.repository.FuncionalidadRepository;
import mx.lania.g4d.repository.UserRepository;
import mx.lania.g4d.service.mapper.Issue;
import mx.lania.g4d.service.utils.GitLabService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Funcionalidad}.
 */
@Service
@Transactional
public class FuncionalidadService {

    private final Logger log = LoggerFactory.getLogger(FuncionalidadService.class);

    private final FuncionalidadRepository funcionalidadRepository;
    private final UserRepository userRepository;
    private final BitacoraService bitacoraService;
    private final GitLabService gitLabService;

    public FuncionalidadService(
        FuncionalidadRepository funcionalidadRepository,
        UserRepository userRepository,
        BitacoraService bitacoraService,
        GitLabService gitLabService
    ) {
        this.funcionalidadRepository = funcionalidadRepository;
        this.userRepository = userRepository;
        this.bitacoraService = bitacoraService;
        this.gitLabService = gitLabService;
    }

    /**
     * Save a funcionalidad.
     *
     * @param funcionalidad the entity to save.
     * @return the persisted entity.
     */
    public Funcionalidad save(Funcionalidad funcionalidad) {
        log.debug("Request to save Funcionalidad : {}", funcionalidad);
        if (funcionalidad.getUrlGitLab() == null || funcionalidad.getUrlGitLab().isEmpty() || funcionalidad.getUrlGitLab().isBlank()) {
            Map<String, String> funcionalidadIdGitLab = gitLabService.createIssue(
                funcionalidad.getNombre(),
                new String[] {},
                funcionalidad.getDescripcion(),
                funcionalidad.getIteracion().getIdGitLab(),
                funcionalidad.getIteracion().getProyecto().getIdProyectoGitLab()
            );
            funcionalidad.setUrlGitLab(funcionalidadIdGitLab.get("iid"));
            funcionalidad.setEnlaceGitLab(funcionalidadIdGitLab.get("web_url"));

            StringBuilder labels = new StringBuilder();

            labels
                .append("g4d : ")
                .append(funcionalidad.getEstatusFuncionalidad())
                .append(",")
                .append("Prioridad : ")
                .append(funcionalidad.getPrioridad());

            gitLabService.updateIssieLabels(
                labels.toString(),
                funcionalidad.getIteracion().getProyecto().getIdProyectoGitLab(),
                funcionalidad.getUrlGitLab()
            );
        }

        Funcionalidad result = funcionalidadRepository.save(funcionalidad);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        User user = null;
        Optional<User> optionalUser = userRepository.findOneByLogin(login);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            Bitacora bitacora = new Bitacora();
            bitacora.setFuncionalidad(result);
            bitacora.setAccion(String.valueOf(AccionBitacora.ALTA));
            bitacora.setUser(user);
            bitacora.setCreado(Instant.now());
            bitacoraService.save(bitacora);
        }

        return result;
    }

    /**
     * Update a funcionalidad.
     *
     * @param funcionalidad the entity to save.
     * @return the persisted entity.
     */
    public Funcionalidad update(Funcionalidad funcionalidad) {
        log.debug("Request to update Funcionalidad : {}", funcionalidad);
        return funcionalidadRepository.save(funcionalidad);
    }

    /**
     * Partially update a funcionalidad.
     *
     * @param funcionalidad the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Funcionalidad> partialUpdate(Funcionalidad funcionalidad) {
        log.debug("Request to partially update Funcionalidad : {}", funcionalidad);

        return funcionalidadRepository
            .findById(funcionalidad.getId())
            .map(existingFuncionalidad -> {
                if (funcionalidad.getNombre() != null) {
                    existingFuncionalidad.setNombre(funcionalidad.getNombre());
                }
                if (funcionalidad.getDescripcion() != null) {
                    existingFuncionalidad.setDescripcion(funcionalidad.getDescripcion());
                }
                if (funcionalidad.getUrlGitLab() != null) {
                    existingFuncionalidad.setUrlGitLab(funcionalidad.getUrlGitLab());
                }
                if (funcionalidad.getCreado() != null) {
                    existingFuncionalidad.setCreado(funcionalidad.getCreado());
                }
                if (funcionalidad.getModificado() != null) {
                    existingFuncionalidad.setModificado(funcionalidad.getModificado());
                }
                if (funcionalidad.getEstatusFuncionalidad() != null) {
                    existingFuncionalidad.setEstatusFuncionalidad(funcionalidad.getEstatusFuncionalidad());
                }
                if (funcionalidad.getPrioridad() != null) {
                    existingFuncionalidad.setPrioridad(funcionalidad.getPrioridad());
                }

                return existingFuncionalidad;
            })
            .map(funcionalidadRepository::save);
    }

    /**
     * Get all the funcionalidads.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Funcionalidad> findAll() {
        log.debug("Request to get all Funcionalidads");
        return funcionalidadRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Funcionalidad> findAllByIteracionId(Long iteracionId) {
        log.debug("Request to get all Funcionalidads by Iteración id");
        return funcionalidadRepository.findAllWithEagerRelationshipsByIteracionId(iteracionId);
    }

    @Transactional(readOnly = true)
    public List<Funcionalidad> findAllByProyectoId(Long proyectoId) {
        log.debug("Request to get all Funcionalidads by proyecto Id");
        return funcionalidadRepository.findAllWithEagerRelationshipsByProyectoId(proyectoId);
    }

    /**
     * Get all the funcionalidads with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Funcionalidad> findAllWithEagerRelationships(Pageable pageable) {
        return funcionalidadRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one funcionalidad by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Funcionalidad> findOne(Long id) {
        log.debug("Request to get Funcionalidad : {}", id);
        return funcionalidadRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the funcionalidad by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Funcionalidad : {}", id);
        funcionalidadRepository.deleteById(id);
    }

    @Transactional
    public List<Funcionalidad> updateFuncionalidadsWithGitLab(Long proyectoId) {
        List<Funcionalidad> funcionalidads = findAllByProyectoId(proyectoId);
        Proyecto proyecto = funcionalidads.get(0).getIteracion().getProyecto();
        List<Funcionalidad> funcionalidadsUpdated = new ArrayList<>();

        List<Issue> issues = gitLabService.getAllIssuesByProyectoId(String.valueOf(proyecto.getIdProyectoGitLab()));
        for (Funcionalidad funcionalidad : funcionalidads) {
            if (funcionalidad.getUrlGitLab() == null || funcionalidad.getUrlGitLab().isBlank() || funcionalidad.getUrlGitLab().isEmpty()) {
                continue;
            }
            long gitlabIid = Long.parseLong(funcionalidad.getUrlGitLab());
            issues.forEach(issue -> {
                if (issue.getIid() == gitlabIid && issue.getState().equalsIgnoreCase("closed")) {
                    funcionalidad.setEstatusFuncionalidad("GitLab closed");
                } else {
                    List<String> labels = issue.getLabels();
                    for (String label : labels) {
                        if (label.contains("g4d :")) {
                            if (!label.equalsIgnoreCase(funcionalidad.getEstatusFuncionalidad())) {
                                funcionalidad.setEstatusFuncionalidad(label);
                                funcionalidadsUpdated.add(update(funcionalidad));
                            }
                        }
                    }
                }
            });
        }

        return funcionalidadsUpdated;
    }
}
