package mx.lania.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mx.lania.g4d.domain.EstatusFuncionalidad;
import mx.lania.g4d.repository.EstatusFuncionalidadRepository;
import mx.lania.g4d.service.EstatusFuncionalidadService;
import mx.lania.g4d.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link mx.lania.g4d.domain.EstatusFuncionalidad}.
 */
@RestController
@RequestMapping("/api")
public class EstatusFuncionalidadResource {

    private final Logger log = LoggerFactory.getLogger(EstatusFuncionalidadResource.class);

    private static final String ENTITY_NAME = "estatusFuncionalidad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EstatusFuncionalidadService estatusFuncionalidadService;

    private final EstatusFuncionalidadRepository estatusFuncionalidadRepository;

    public EstatusFuncionalidadResource(
        EstatusFuncionalidadService estatusFuncionalidadService,
        EstatusFuncionalidadRepository estatusFuncionalidadRepository
    ) {
        this.estatusFuncionalidadService = estatusFuncionalidadService;
        this.estatusFuncionalidadRepository = estatusFuncionalidadRepository;
    }

    /**
     * {@code POST  /estatus-funcionalidads} : Create a new estatusFuncionalidad.
     *
     * @param estatusFuncionalidad the estatusFuncionalidad to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new estatusFuncionalidad, or with status {@code 400 (Bad Request)} if the estatusFuncionalidad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/estatus-funcionalidads")
    public ResponseEntity<EstatusFuncionalidad> createEstatusFuncionalidad(@RequestBody EstatusFuncionalidad estatusFuncionalidad)
        throws URISyntaxException {
        log.debug("REST request to save EstatusFuncionalidad : {}", estatusFuncionalidad);
        if (estatusFuncionalidad.getId() != null) {
            throw new BadRequestAlertException("A new estatusFuncionalidad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EstatusFuncionalidad result = estatusFuncionalidadService.save(estatusFuncionalidad);
        return ResponseEntity
            .created(new URI("/api/estatus-funcionalidads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /estatus-funcionalidads/:id} : Updates an existing estatusFuncionalidad.
     *
     * @param id the id of the estatusFuncionalidad to save.
     * @param estatusFuncionalidad the estatusFuncionalidad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estatusFuncionalidad,
     * or with status {@code 400 (Bad Request)} if the estatusFuncionalidad is not valid,
     * or with status {@code 500 (Internal Server Error)} if the estatusFuncionalidad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/estatus-funcionalidads/{id}")
    public ResponseEntity<EstatusFuncionalidad> updateEstatusFuncionalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EstatusFuncionalidad estatusFuncionalidad
    ) throws URISyntaxException {
        log.debug("REST request to update EstatusFuncionalidad : {}, {}", id, estatusFuncionalidad);
        if (estatusFuncionalidad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estatusFuncionalidad.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estatusFuncionalidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EstatusFuncionalidad result = estatusFuncionalidadService.update(estatusFuncionalidad);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estatusFuncionalidad.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /estatus-funcionalidads/:id} : Partial updates given fields of an existing estatusFuncionalidad, field will ignore if it is null
     *
     * @param id the id of the estatusFuncionalidad to save.
     * @param estatusFuncionalidad the estatusFuncionalidad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estatusFuncionalidad,
     * or with status {@code 400 (Bad Request)} if the estatusFuncionalidad is not valid,
     * or with status {@code 404 (Not Found)} if the estatusFuncionalidad is not found,
     * or with status {@code 500 (Internal Server Error)} if the estatusFuncionalidad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/estatus-funcionalidads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EstatusFuncionalidad> partialUpdateEstatusFuncionalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EstatusFuncionalidad estatusFuncionalidad
    ) throws URISyntaxException {
        log.debug("REST request to partial update EstatusFuncionalidad partially : {}, {}", id, estatusFuncionalidad);
        if (estatusFuncionalidad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estatusFuncionalidad.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estatusFuncionalidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EstatusFuncionalidad> result = estatusFuncionalidadService.partialUpdate(estatusFuncionalidad);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estatusFuncionalidad.getId().toString())
        );
    }

    /**
     * {@code GET  /estatus-funcionalidads} : get all the estatusFuncionalidads.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of estatusFuncionalidads in body.
     */
    @GetMapping("/estatus-funcionalidads")
    public List<EstatusFuncionalidad> getAllEstatusFuncionalidads() {
        log.debug("REST request to get all EstatusFuncionalidads");
        return estatusFuncionalidadService.findAll();
    }

    /**
     * {@code GET  /estatus-funcionalidads/:id} : get the "id" estatusFuncionalidad.
     *
     * @param id the id of the estatusFuncionalidad to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estatusFuncionalidad, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/estatus-funcionalidads/{id}")
    public ResponseEntity<EstatusFuncionalidad> getEstatusFuncionalidad(@PathVariable Long id) {
        log.debug("REST request to get EstatusFuncionalidad : {}", id);
        Optional<EstatusFuncionalidad> estatusFuncionalidad = estatusFuncionalidadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(estatusFuncionalidad);
    }

    /**
     * {@code DELETE  /estatus-funcionalidads/:id} : delete the "id" estatusFuncionalidad.
     *
     * @param id the id of the estatusFuncionalidad to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/estatus-funcionalidads/{id}")
    public ResponseEntity<Void> deleteEstatusFuncionalidad(@PathVariable Long id) {
        log.debug("REST request to delete EstatusFuncionalidad : {}", id);
        estatusFuncionalidadService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
