package mx.lania.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mx.lania.g4d.domain.Permiso;
import mx.lania.g4d.repository.PermisoRepository;
import mx.lania.g4d.service.PermisoQueryService;
import mx.lania.g4d.service.PermisoService;
import mx.lania.g4d.service.criteria.PermisoCriteria;
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
 * REST controller for managing {@link mx.lania.g4d.domain.Permiso}.
 */
@RestController
@RequestMapping("/api")
public class PermisoResource {

    private final Logger log = LoggerFactory.getLogger(PermisoResource.class);

    private static final String ENTITY_NAME = "permiso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PermisoService permisoService;

    private final PermisoRepository permisoRepository;

    private final PermisoQueryService permisoQueryService;

    public PermisoResource(PermisoService permisoService, PermisoRepository permisoRepository, PermisoQueryService permisoQueryService) {
        this.permisoService = permisoService;
        this.permisoRepository = permisoRepository;
        this.permisoQueryService = permisoQueryService;
    }

    /**
     * {@code POST  /permisos} : Create a new permiso.
     *
     * @param permiso the permiso to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new permiso, or with status {@code 400 (Bad Request)} if the permiso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/permisos")
    public ResponseEntity<Permiso> createPermiso(@RequestBody Permiso permiso) throws URISyntaxException {
        log.debug("REST request to save Permiso : {}", permiso);
        if (permiso.getId() != null) {
            throw new BadRequestAlertException("A new permiso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Permiso result = permisoService.save(permiso);
        return ResponseEntity
            .created(new URI("/api/permisos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /permisos/:id} : Updates an existing permiso.
     *
     * @param id the id of the permiso to save.
     * @param permiso the permiso to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permiso,
     * or with status {@code 400 (Bad Request)} if the permiso is not valid,
     * or with status {@code 500 (Internal Server Error)} if the permiso couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/permisos/{id}")
    public ResponseEntity<Permiso> updatePermiso(@PathVariable(value = "id", required = false) final Long id, @RequestBody Permiso permiso)
        throws URISyntaxException {
        log.debug("REST request to update Permiso : {}, {}", id, permiso);
        if (permiso.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permiso.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permisoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Permiso result = permisoService.update(permiso);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, permiso.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /permisos/:id} : Partial updates given fields of an existing permiso, field will ignore if it is null
     *
     * @param id the id of the permiso to save.
     * @param permiso the permiso to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permiso,
     * or with status {@code 400 (Bad Request)} if the permiso is not valid,
     * or with status {@code 404 (Not Found)} if the permiso is not found,
     * or with status {@code 500 (Internal Server Error)} if the permiso couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/permisos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Permiso> partialUpdatePermiso(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Permiso permiso
    ) throws URISyntaxException {
        log.debug("REST request to partial update Permiso partially : {}, {}", id, permiso);
        if (permiso.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permiso.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permisoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Permiso> result = permisoService.partialUpdate(permiso);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, permiso.getId().toString())
        );
    }

    /**
     * {@code GET  /permisos} : get all the permisos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permisos in body.
     */
    @GetMapping("/permisos")
    public ResponseEntity<List<Permiso>> getAllPermisos(
        PermisoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Permisos by criteria: {}", criteria);
        Page<Permiso> page = permisoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /permisos/count} : count all the permisos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/permisos/count")
    public ResponseEntity<Long> countPermisos(PermisoCriteria criteria) {
        log.debug("REST request to count Permisos by criteria: {}", criteria);
        return ResponseEntity.ok().body(permisoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /permisos/:id} : get the "id" permiso.
     *
     * @param id the id of the permiso to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permiso, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/permisos/{id}")
    public ResponseEntity<Permiso> getPermiso(@PathVariable Long id) {
        log.debug("REST request to get Permiso : {}", id);
        Optional<Permiso> permiso = permisoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(permiso);
    }

    /**
     * {@code DELETE  /permisos/:id} : delete the "id" permiso.
     *
     * @param id the id of the permiso to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/permisos/{id}")
    public ResponseEntity<Void> deletePermiso(@PathVariable Long id) {
        log.debug("REST request to delete Permiso : {}", id);
        permisoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
