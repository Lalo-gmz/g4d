package mx.lania.g4d.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import mx.lania.g4d.domain.*;
import mx.lania.g4d.repository.ProyectoRepository;
import mx.lania.g4d.repository.UserRepository;
import mx.lania.g4d.service.Utils.GitLabService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    private final FuncionalidadService funcionalidadService;
    private final AtributoFuncionalidadService atributoFuncionalidadService;

    public ProyectoService(
        ProyectoRepository proyectoRepository,
        ParticipacionProyectoService participacionProyectoService,
        UserRepository userRepository,
        GitLabService gitLabService,
        FuncionalidadService funcionalidadService,
        AtributoFuncionalidadService atributoFuncionalidadService
    ) {
        this.proyectoRepository = proyectoRepository;
        this.participacionProyectoService = participacionProyectoService;
        this.userRepository = userRepository;
        this.gitLabService = gitLabService;
        this.funcionalidadService = funcionalidadService;
        this.atributoFuncionalidadService = atributoFuncionalidadService;
    }

    /**
     * Save a proyecto.
     *
     * @param proyecto the entity to save.
     * @return the persisted entity.
     */
    public Proyecto save(Proyecto proyecto) {
        log.debug("Request to save Proyecto : {}", proyecto);
        Proyecto res = proyectoRepository.save(proyecto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        Optional<User> optionalUser = userRepository.findOneByLogin(login);

        ParticipacionProyecto participacionProyecto = new ParticipacionProyecto();
        participacionProyecto.setProyecto(res);
        participacionProyecto.setEsAdmin(true);
        optionalUser.ifPresent(participacionProyecto::setUsuario);
        participacionProyectoService.save(participacionProyecto);
        if (res.getIdProyectoGitLab().equalsIgnoreCase("NUEVO")) {
            Map<String, String> idGitLabProyect = gitLabService.SaveProjecto(res.getNombre());
            res.setIdProyectoGitLab(idGitLabProyect.get("id"));
            res.setEnlaceGitLab(idGitLabProyect.get("web_url"));
            Optional<Proyecto> proyectoConGitLab = partialUpdate(res);
            if (proyectoConGitLab.isPresent()) {
                res = proyectoConGitLab.get();
            }
        }

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
