package mx.lania.mca.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mx.lania.mca.g4d.repository.EstatusFuncionalidadRepository;
import mx.lania.mca.g4d.service.EstatusFuncionalidadService;
import mx.lania.mca.g4d.service.dto.EstatusFuncionalidadDTO;
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
 * REST controller for managing {@link mx.lania.mca.g4d.domain.EstatusFuncionalidad}.
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
     * @param estatusFuncionalidadDTO the estatusFuncionalidadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new estatusFuncionalidadDTO, or with status {@code 400 (Bad Request)} if the estatusFuncionalidad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/estatus-funcionalidads")
    public ResponseEntity<EstatusFuncionalidadDTO> createEstatusFuncionalidad(@RequestBody EstatusFuncionalidadDTO estatusFuncionalidadDTO)
        throws URISyntaxException {
        log.debug("REST request to save EstatusFuncionalidad : {}", estatusFuncionalidadDTO);
        if (estatusFuncionalidadDTO.getId() != null) {
            throw new BadRequestAlertException("A new estatusFuncionalidad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EstatusFuncionalidadDTO result = estatusFuncionalidadService.save(estatusFuncionalidadDTO);
        return ResponseEntity
            .created(new URI("/api/estatus-funcionalidads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /estatus-funcionalidads/:id} : Updates an existing estatusFuncionalidad.
     *
     * @param id the id of the estatusFuncionalidadDTO to save.
     * @param estatusFuncionalidadDTO the estatusFuncionalidadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estatusFuncionalidadDTO,
     * or with status {@code 400 (Bad Request)} if the estatusFuncionalidadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the estatusFuncionalidadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/estatus-funcionalidads/{id}")
    public ResponseEntity<EstatusFuncionalidadDTO> updateEstatusFuncionalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EstatusFuncionalidadDTO estatusFuncionalidadDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EstatusFuncionalidad : {}, {}", id, estatusFuncionalidadDTO);
        if (estatusFuncionalidadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estatusFuncionalidadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estatusFuncionalidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EstatusFuncionalidadDTO result = estatusFuncionalidadService.update(estatusFuncionalidadDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estatusFuncionalidadDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /estatus-funcionalidads/:id} : Partial updates given fields of an existing estatusFuncionalidad, field will ignore if it is null
     *
     * @param id the id of the estatusFuncionalidadDTO to save.
     * @param estatusFuncionalidadDTO the estatusFuncionalidadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estatusFuncionalidadDTO,
     * or with status {@code 400 (Bad Request)} if the estatusFuncionalidadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the estatusFuncionalidadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the estatusFuncionalidadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/estatus-funcionalidads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EstatusFuncionalidadDTO> partialUpdateEstatusFuncionalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EstatusFuncionalidadDTO estatusFuncionalidadDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EstatusFuncionalidad partially : {}, {}", id, estatusFuncionalidadDTO);
        if (estatusFuncionalidadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estatusFuncionalidadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estatusFuncionalidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EstatusFuncionalidadDTO> result = estatusFuncionalidadService.partialUpdate(estatusFuncionalidadDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estatusFuncionalidadDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /estatus-funcionalidads} : get all the estatusFuncionalidads.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of estatusFuncionalidads in body.
     */
    @GetMapping("/estatus-funcionalidads")
    public ResponseEntity<List<EstatusFuncionalidadDTO>> getAllEstatusFuncionalidads(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of EstatusFuncionalidads");
        Page<EstatusFuncionalidadDTO> page = estatusFuncionalidadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /estatus-funcionalidads/:id} : get the "id" estatusFuncionalidad.
     *
     * @param id the id of the estatusFuncionalidadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estatusFuncionalidadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/estatus-funcionalidads/{id}")
    public ResponseEntity<EstatusFuncionalidadDTO> getEstatusFuncionalidad(@PathVariable Long id) {
        log.debug("REST request to get EstatusFuncionalidad : {}", id);
        Optional<EstatusFuncionalidadDTO> estatusFuncionalidadDTO = estatusFuncionalidadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(estatusFuncionalidadDTO);
    }

    /**
     * {@code DELETE  /estatus-funcionalidads/:id} : delete the "id" estatusFuncionalidad.
     *
     * @param id the id of the estatusFuncionalidadDTO to delete.
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
