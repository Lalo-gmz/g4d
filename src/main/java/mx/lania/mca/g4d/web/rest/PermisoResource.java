package mx.lania.mca.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mx.lania.mca.g4d.repository.PermisoRepository;
import mx.lania.mca.g4d.service.PermisoService;
import mx.lania.mca.g4d.service.dto.PermisoDTO;
import mx.lania.mca.g4d.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link mx.lania.mca.g4d.domain.Permiso}.
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

    public PermisoResource(PermisoService permisoService, PermisoRepository permisoRepository) {
        this.permisoService = permisoService;
        this.permisoRepository = permisoRepository;
    }

    /**
     * {@code POST  /permisos} : Create a new permiso.
     *
     * @param permisoDTO the permisoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new permisoDTO, or with status {@code 400 (Bad Request)} if the permiso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/permisos")
    public ResponseEntity<PermisoDTO> createPermiso(@RequestBody PermisoDTO permisoDTO) throws URISyntaxException {
        log.debug("REST request to save Permiso : {}", permisoDTO);
        if (permisoDTO.getId() != null) {
            throw new BadRequestAlertException("A new permiso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PermisoDTO result = permisoService.save(permisoDTO);
        return ResponseEntity
            .created(new URI("/api/permisos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /permisos/:id} : Updates an existing permiso.
     *
     * @param id the id of the permisoDTO to save.
     * @param permisoDTO the permisoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permisoDTO,
     * or with status {@code 400 (Bad Request)} if the permisoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the permisoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/permisos/{id}")
    public ResponseEntity<PermisoDTO> updatePermiso(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PermisoDTO permisoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Permiso : {}, {}", id, permisoDTO);
        if (permisoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permisoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permisoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PermisoDTO result = permisoService.update(permisoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, permisoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /permisos/:id} : Partial updates given fields of an existing permiso, field will ignore if it is null
     *
     * @param id the id of the permisoDTO to save.
     * @param permisoDTO the permisoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permisoDTO,
     * or with status {@code 400 (Bad Request)} if the permisoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the permisoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the permisoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/permisos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PermisoDTO> partialUpdatePermiso(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PermisoDTO permisoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Permiso partially : {}, {}", id, permisoDTO);
        if (permisoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permisoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permisoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PermisoDTO> result = permisoService.partialUpdate(permisoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, permisoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /permisos} : get all the permisos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permisos in body.
     */
    @GetMapping("/permisos")
    public ResponseEntity<List<PermisoDTO>> getAllPermisos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Permisos");
        Page<PermisoDTO> page = permisoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /permisos/:id} : get the "id" permiso.
     *
     * @param id the id of the permisoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permisoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/permisos/{id}")
    public ResponseEntity<PermisoDTO> getPermiso(@PathVariable Long id) {
        log.debug("REST request to get Permiso : {}", id);
        Optional<PermisoDTO> permisoDTO = permisoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(permisoDTO);
    }

    /**
     * {@code DELETE  /permisos/:id} : delete the "id" permiso.
     *
     * @param id the id of the permisoDTO to delete.
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
