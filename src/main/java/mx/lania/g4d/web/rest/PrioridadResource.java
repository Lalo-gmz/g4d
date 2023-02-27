package mx.lania.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mx.lania.g4d.domain.Prioridad;
import mx.lania.g4d.repository.PrioridadRepository;
import mx.lania.g4d.service.PrioridadService;
import mx.lania.g4d.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link mx.lania.g4d.domain.Prioridad}.
 */
@RestController
@RequestMapping("/api")
public class PrioridadResource {

    private final Logger log = LoggerFactory.getLogger(PrioridadResource.class);

    private static final String ENTITY_NAME = "prioridad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrioridadService prioridadService;

    private final PrioridadRepository prioridadRepository;

    public PrioridadResource(PrioridadService prioridadService, PrioridadRepository prioridadRepository) {
        this.prioridadService = prioridadService;
        this.prioridadRepository = prioridadRepository;
    }

    /**
     * {@code POST  /prioridads} : Create a new prioridad.
     *
     * @param prioridad the prioridad to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prioridad, or with status {@code 400 (Bad Request)} if the prioridad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prioridads")
    public ResponseEntity<Prioridad> createPrioridad(@RequestBody Prioridad prioridad) throws URISyntaxException {
        log.debug("REST request to save Prioridad : {}", prioridad);
        if (prioridad.getId() != null) {
            throw new BadRequestAlertException("A new prioridad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Prioridad result = prioridadService.save(prioridad);
        return ResponseEntity
            .created(new URI("/api/prioridads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prioridads/:id} : Updates an existing prioridad.
     *
     * @param id the id of the prioridad to save.
     * @param prioridad the prioridad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prioridad,
     * or with status {@code 400 (Bad Request)} if the prioridad is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prioridad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prioridads/{id}")
    public ResponseEntity<Prioridad> updatePrioridad(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Prioridad prioridad
    ) throws URISyntaxException {
        log.debug("REST request to update Prioridad : {}, {}", id, prioridad);
        if (prioridad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prioridad.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prioridadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Prioridad result = prioridadService.update(prioridad);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prioridad.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prioridads/:id} : Partial updates given fields of an existing prioridad, field will ignore if it is null
     *
     * @param id the id of the prioridad to save.
     * @param prioridad the prioridad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prioridad,
     * or with status {@code 400 (Bad Request)} if the prioridad is not valid,
     * or with status {@code 404 (Not Found)} if the prioridad is not found,
     * or with status {@code 500 (Internal Server Error)} if the prioridad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prioridads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Prioridad> partialUpdatePrioridad(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Prioridad prioridad
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prioridad partially : {}, {}", id, prioridad);
        if (prioridad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prioridad.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prioridadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Prioridad> result = prioridadService.partialUpdate(prioridad);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prioridad.getId().toString())
        );
    }

    /**
     * {@code GET  /prioridads} : get all the prioridads.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prioridads in body.
     */
    @GetMapping("/prioridads")
    public List<Prioridad> getAllPrioridads() {
        log.debug("REST request to get all Prioridads");
        return prioridadService.findAll();
    }

    /**
     * {@code GET  /prioridads/:id} : get the "id" prioridad.
     *
     * @param id the id of the prioridad to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prioridad, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prioridads/{id}")
    public ResponseEntity<Prioridad> getPrioridad(@PathVariable Long id) {
        log.debug("REST request to get Prioridad : {}", id);
        Optional<Prioridad> prioridad = prioridadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prioridad);
    }

    /**
     * {@code DELETE  /prioridads/:id} : delete the "id" prioridad.
     *
     * @param id the id of the prioridad to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prioridads/{id}")
    public ResponseEntity<Void> deletePrioridad(@PathVariable Long id) {
        log.debug("REST request to delete Prioridad : {}", id);
        prioridadService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
