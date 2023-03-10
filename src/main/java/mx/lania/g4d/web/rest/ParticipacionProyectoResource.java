package mx.lania.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mx.lania.g4d.domain.ParticipacionProyecto;
import mx.lania.g4d.repository.ParticipacionProyectoRepository;
import mx.lania.g4d.service.ParticipacionProyectoService;
import mx.lania.g4d.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link mx.lania.g4d.domain.ParticipacionProyecto}.
 */
@RestController
@RequestMapping("/api")
public class ParticipacionProyectoResource {

    private final Logger log = LoggerFactory.getLogger(ParticipacionProyectoResource.class);

    private static final String ENTITY_NAME = "participacionProyecto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParticipacionProyectoService participacionProyectoService;

    private final ParticipacionProyectoRepository participacionProyectoRepository;

    public ParticipacionProyectoResource(
        ParticipacionProyectoService participacionProyectoService,
        ParticipacionProyectoRepository participacionProyectoRepository
    ) {
        this.participacionProyectoService = participacionProyectoService;
        this.participacionProyectoRepository = participacionProyectoRepository;
    }

    /**
     * {@code POST  /participacion-proyectos} : Create a new participacionProyecto.
     *
     * @param participacionProyecto the participacionProyecto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new participacionProyecto, or with status {@code 400 (Bad Request)} if the participacionProyecto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/participacion-proyectos")
    public ResponseEntity<ParticipacionProyecto> createParticipacionProyecto(@RequestBody ParticipacionProyecto participacionProyecto)
        throws URISyntaxException {
        log.debug("REST request to save ParticipacionProyecto : {}", participacionProyecto);
        if (participacionProyecto.getId() != null) {
            throw new BadRequestAlertException("A new participacionProyecto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParticipacionProyecto result = participacionProyectoService.save(participacionProyecto);
        return ResponseEntity
            .created(new URI("/api/participacion-proyectos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /participacion-proyectos/:id} : Updates an existing participacionProyecto.
     *
     * @param id the id of the participacionProyecto to save.
     * @param participacionProyecto the participacionProyecto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated participacionProyecto,
     * or with status {@code 400 (Bad Request)} if the participacionProyecto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the participacionProyecto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/participacion-proyectos/{id}")
    public ResponseEntity<ParticipacionProyecto> updateParticipacionProyecto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ParticipacionProyecto participacionProyecto
    ) throws URISyntaxException {
        log.debug("REST request to update ParticipacionProyecto : {}, {}", id, participacionProyecto);
        if (participacionProyecto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, participacionProyecto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!participacionProyectoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParticipacionProyecto result = participacionProyectoService.update(participacionProyecto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, participacionProyecto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /participacion-proyectos/:id} : Partial updates given fields of an existing participacionProyecto, field will ignore if it is null
     *
     * @param id the id of the participacionProyecto to save.
     * @param participacionProyecto the participacionProyecto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated participacionProyecto,
     * or with status {@code 400 (Bad Request)} if the participacionProyecto is not valid,
     * or with status {@code 404 (Not Found)} if the participacionProyecto is not found,
     * or with status {@code 500 (Internal Server Error)} if the participacionProyecto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/participacion-proyectos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ParticipacionProyecto> partialUpdateParticipacionProyecto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ParticipacionProyecto participacionProyecto
    ) throws URISyntaxException {
        log.debug("REST request to partial update ParticipacionProyecto partially : {}, {}", id, participacionProyecto);
        if (participacionProyecto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, participacionProyecto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!participacionProyectoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParticipacionProyecto> result = participacionProyectoService.partialUpdate(participacionProyecto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, participacionProyecto.getId().toString())
        );
    }

    /**
     * {@code GET  /participacion-proyectos} : get all the participacionProyectos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of participacionProyectos in body.
     */
    @GetMapping("/participacion-proyectos")
    public List<ParticipacionProyecto> getAllParticipacionProyectos() {
        log.debug("REST request to get all ParticipacionProyectos");
        return participacionProyectoService.findAll();
    }

    @GetMapping("/participacion-proyectosByUser")
    public List<ParticipacionProyecto> getAllParticipacionProyectosByUser() {
        log.debug("REST request to get all ParticipacionProyectos");
        return participacionProyectoService.findAllByUser();
    }

    /**
     * {@code GET  /participacion-proyectos/:id} : get the "id" participacionProyecto.
     *
     * @param id the id of the participacionProyecto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the participacionProyecto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/participacion-proyectos/{id}")
    public ResponseEntity<ParticipacionProyecto> getParticipacionProyecto(@PathVariable Long id) {
        log.debug("REST request to get ParticipacionProyecto : {}", id);
        Optional<ParticipacionProyecto> participacionProyecto = participacionProyectoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(participacionProyecto);
    }

    /**
     * {@code DELETE  /participacion-proyectos/:id} : delete the "id" participacionProyecto.
     *
     * @param id the id of the participacionProyecto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/participacion-proyectos/{id}")
    public ResponseEntity<Void> deleteParticipacionProyecto(@PathVariable Long id) {
        log.debug("REST request to delete ParticipacionProyecto : {}", id);
        participacionProyectoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
