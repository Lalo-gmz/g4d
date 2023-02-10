package mx.lania.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mx.lania.g4d.domain.Configuracion;
import mx.lania.g4d.repository.ConfiguracionRepository;
import mx.lania.g4d.service.ConfiguracionQueryService;
import mx.lania.g4d.service.ConfiguracionService;
import mx.lania.g4d.service.criteria.ConfiguracionCriteria;
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
 * REST controller for managing {@link mx.lania.g4d.domain.Configuracion}.
 */
@RestController
@RequestMapping("/api")
public class ConfiguracionResource {

    private final Logger log = LoggerFactory.getLogger(ConfiguracionResource.class);

    private static final String ENTITY_NAME = "configuracion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfiguracionService configuracionService;

    private final ConfiguracionRepository configuracionRepository;

    private final ConfiguracionQueryService configuracionQueryService;

    public ConfiguracionResource(
        ConfiguracionService configuracionService,
        ConfiguracionRepository configuracionRepository,
        ConfiguracionQueryService configuracionQueryService
    ) {
        this.configuracionService = configuracionService;
        this.configuracionRepository = configuracionRepository;
        this.configuracionQueryService = configuracionQueryService;
    }

    /**
     * {@code POST  /configuracions} : Create a new configuracion.
     *
     * @param configuracion the configuracion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configuracion, or with status {@code 400 (Bad Request)} if the configuracion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/configuracions")
    public ResponseEntity<Configuracion> createConfiguracion(@RequestBody Configuracion configuracion) throws URISyntaxException {
        log.debug("REST request to save Configuracion : {}", configuracion);
        if (configuracion.getId() != null) {
            throw new BadRequestAlertException("A new configuracion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Configuracion result = configuracionService.save(configuracion);
        return ResponseEntity
            .created(new URI("/api/configuracions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /configuracions/:id} : Updates an existing configuracion.
     *
     * @param id the id of the configuracion to save.
     * @param configuracion the configuracion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configuracion,
     * or with status {@code 400 (Bad Request)} if the configuracion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configuracion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/configuracions/{id}")
    public ResponseEntity<Configuracion> updateConfiguracion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Configuracion configuracion
    ) throws URISyntaxException {
        log.debug("REST request to update Configuracion : {}, {}", id, configuracion);
        if (configuracion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configuracion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configuracionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Configuracion result = configuracionService.update(configuracion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, configuracion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /configuracions/:id} : Partial updates given fields of an existing configuracion, field will ignore if it is null
     *
     * @param id the id of the configuracion to save.
     * @param configuracion the configuracion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configuracion,
     * or with status {@code 400 (Bad Request)} if the configuracion is not valid,
     * or with status {@code 404 (Not Found)} if the configuracion is not found,
     * or with status {@code 500 (Internal Server Error)} if the configuracion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/configuracions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Configuracion> partialUpdateConfiguracion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Configuracion configuracion
    ) throws URISyntaxException {
        log.debug("REST request to partial update Configuracion partially : {}, {}", id, configuracion);
        if (configuracion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configuracion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configuracionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Configuracion> result = configuracionService.partialUpdate(configuracion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, configuracion.getId().toString())
        );
    }

    /**
     * {@code GET  /configuracions} : get all the configuracions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configuracions in body.
     */
    @GetMapping("/configuracions")
    public ResponseEntity<List<Configuracion>> getAllConfiguracions(
        ConfiguracionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Configuracions by criteria: {}", criteria);
        Page<Configuracion> page = configuracionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /configuracions/count} : count all the configuracions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/configuracions/count")
    public ResponseEntity<Long> countConfiguracions(ConfiguracionCriteria criteria) {
        log.debug("REST request to count Configuracions by criteria: {}", criteria);
        return ResponseEntity.ok().body(configuracionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /configuracions/:id} : get the "id" configuracion.
     *
     * @param id the id of the configuracion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configuracion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/configuracions/{id}")
    public ResponseEntity<Configuracion> getConfiguracion(@PathVariable Long id) {
        log.debug("REST request to get Configuracion : {}", id);
        Optional<Configuracion> configuracion = configuracionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configuracion);
    }

    /**
     * {@code DELETE  /configuracions/:id} : delete the "id" configuracion.
     *
     * @param id the id of the configuracion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/configuracions/{id}")
    public ResponseEntity<Void> deleteConfiguracion(@PathVariable Long id) {
        log.debug("REST request to delete Configuracion : {}", id);
        configuracionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
