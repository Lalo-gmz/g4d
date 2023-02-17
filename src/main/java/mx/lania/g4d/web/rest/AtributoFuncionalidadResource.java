package mx.lania.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mx.lania.g4d.domain.AtributoFuncionalidad;
import mx.lania.g4d.repository.AtributoFuncionalidadRepository;
import mx.lania.g4d.service.AtributoFuncionalidadQueryService;
import mx.lania.g4d.service.AtributoFuncionalidadService;
import mx.lania.g4d.service.criteria.AtributoFuncionalidadCriteria;
import mx.lania.g4d.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link mx.lania.g4d.domain.AtributoFuncionalidad}.
 */
@RestController
@RequestMapping("/api")
public class AtributoFuncionalidadResource {

    private final Logger log = LoggerFactory.getLogger(AtributoFuncionalidadResource.class);

    private static final String ENTITY_NAME = "atributoFuncionalidad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AtributoFuncionalidadService atributoFuncionalidadService;

    private final AtributoFuncionalidadRepository atributoFuncionalidadRepository;

    private final AtributoFuncionalidadQueryService atributoFuncionalidadQueryService;

    public AtributoFuncionalidadResource(
        AtributoFuncionalidadService atributoFuncionalidadService,
        AtributoFuncionalidadRepository atributoFuncionalidadRepository,
        AtributoFuncionalidadQueryService atributoFuncionalidadQueryService
    ) {
        this.atributoFuncionalidadService = atributoFuncionalidadService;
        this.atributoFuncionalidadRepository = atributoFuncionalidadRepository;
        this.atributoFuncionalidadQueryService = atributoFuncionalidadQueryService;
    }

    /**
     * {@code POST  /atributo-funcionalidads} : Create a new atributoFuncionalidad.
     *
     * @param atributoFuncionalidad the atributoFuncionalidad to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new atributoFuncionalidad, or with status {@code 400 (Bad Request)} if the atributoFuncionalidad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/atributo-funcionalidads")
    public ResponseEntity<AtributoFuncionalidad> createAtributoFuncionalidad(@RequestBody AtributoFuncionalidad atributoFuncionalidad)
        throws URISyntaxException {
        log.debug("REST request to save AtributoFuncionalidad : {}", atributoFuncionalidad);
        if (atributoFuncionalidad.getId() != null) {
            throw new BadRequestAlertException("A new atributoFuncionalidad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AtributoFuncionalidad result = atributoFuncionalidadService.save(atributoFuncionalidad);
        return ResponseEntity
            .created(new URI("/api/atributo-funcionalidads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /atributo-funcionalidads/:id} : Updates an existing atributoFuncionalidad.
     *
     * @param id the id of the atributoFuncionalidad to save.
     * @param atributoFuncionalidad the atributoFuncionalidad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated atributoFuncionalidad,
     * or with status {@code 400 (Bad Request)} if the atributoFuncionalidad is not valid,
     * or with status {@code 500 (Internal Server Error)} if the atributoFuncionalidad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/atributo-funcionalidads/{id}")
    public ResponseEntity<AtributoFuncionalidad> updateAtributoFuncionalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AtributoFuncionalidad atributoFuncionalidad
    ) throws URISyntaxException {
        log.debug("REST request to update AtributoFuncionalidad : {}, {}", id, atributoFuncionalidad);
        if (atributoFuncionalidad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, atributoFuncionalidad.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!atributoFuncionalidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AtributoFuncionalidad result = atributoFuncionalidadService.update(atributoFuncionalidad);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, atributoFuncionalidad.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /atributo-funcionalidads/:id} : Partial updates given fields of an existing atributoFuncionalidad, field will ignore if it is null
     *
     * @param id the id of the atributoFuncionalidad to save.
     * @param atributoFuncionalidad the atributoFuncionalidad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated atributoFuncionalidad,
     * or with status {@code 400 (Bad Request)} if the atributoFuncionalidad is not valid,
     * or with status {@code 404 (Not Found)} if the atributoFuncionalidad is not found,
     * or with status {@code 500 (Internal Server Error)} if the atributoFuncionalidad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/atributo-funcionalidads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AtributoFuncionalidad> partialUpdateAtributoFuncionalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AtributoFuncionalidad atributoFuncionalidad
    ) throws URISyntaxException {
        log.debug("REST request to partial update AtributoFuncionalidad partially : {}, {}", id, atributoFuncionalidad);
        if (atributoFuncionalidad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, atributoFuncionalidad.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!atributoFuncionalidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AtributoFuncionalidad> result = atributoFuncionalidadService.partialUpdate(atributoFuncionalidad);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, atributoFuncionalidad.getId().toString())
        );
    }

    /**
     * {@code GET  /atributo-funcionalidads} : get all the atributoFuncionalidads.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of atributoFuncionalidads in body.
     */
    @GetMapping("/atributo-funcionalidads")
    public ResponseEntity<List<AtributoFuncionalidad>> getAllAtributoFuncionalidads(
        AtributoFuncionalidadCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AtributoFuncionalidads by criteria: {}", criteria);
        Page<AtributoFuncionalidad> page = atributoFuncionalidadQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /atributo-funcionalidads/count} : count all the atributoFuncionalidads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/atributo-funcionalidads/count")
    public ResponseEntity<Long> countAtributoFuncionalidads(AtributoFuncionalidadCriteria criteria) {
        log.debug("REST request to count AtributoFuncionalidads by criteria: {}", criteria);
        return ResponseEntity.ok().body(atributoFuncionalidadQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /atributo-funcionalidads/:id} : get the "id" atributoFuncionalidad.
     *
     * @param id the id of the atributoFuncionalidad to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the atributoFuncionalidad, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/atributo-funcionalidads/{id}")
    public ResponseEntity<AtributoFuncionalidad> getAtributoFuncionalidad(@PathVariable Long id) {
        log.debug("REST request to get AtributoFuncionalidad : {}", id);
        Optional<AtributoFuncionalidad> atributoFuncionalidad = atributoFuncionalidadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(atributoFuncionalidad);
    }

    /**
     * {@code DELETE  /atributo-funcionalidads/:id} : delete the "id" atributoFuncionalidad.
     *
     * @param id the id of the atributoFuncionalidad to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/atributo-funcionalidads/{id}")
    public ResponseEntity<Void> deleteAtributoFuncionalidad(@PathVariable Long id) {
        log.debug("REST request to delete AtributoFuncionalidad : {}", id);
        atributoFuncionalidadService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
