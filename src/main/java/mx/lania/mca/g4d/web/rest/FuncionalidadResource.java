package mx.lania.mca.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mx.lania.mca.g4d.repository.FuncionalidadRepository;
import mx.lania.mca.g4d.service.FuncionalidadService;
import mx.lania.mca.g4d.service.dto.FuncionalidadDTO;
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
 * REST controller for managing {@link mx.lania.mca.g4d.domain.Funcionalidad}.
 */
@RestController
@RequestMapping("/api")
public class FuncionalidadResource {

    private final Logger log = LoggerFactory.getLogger(FuncionalidadResource.class);

    private static final String ENTITY_NAME = "funcionalidad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FuncionalidadService funcionalidadService;

    private final FuncionalidadRepository funcionalidadRepository;

    public FuncionalidadResource(FuncionalidadService funcionalidadService, FuncionalidadRepository funcionalidadRepository) {
        this.funcionalidadService = funcionalidadService;
        this.funcionalidadRepository = funcionalidadRepository;
    }

    /**
     * {@code POST  /funcionalidads} : Create a new funcionalidad.
     *
     * @param funcionalidadDTO the funcionalidadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new funcionalidadDTO, or with status {@code 400 (Bad Request)} if the funcionalidad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/funcionalidads")
    public ResponseEntity<FuncionalidadDTO> createFuncionalidad(@RequestBody FuncionalidadDTO funcionalidadDTO) throws URISyntaxException {
        log.debug("REST request to save Funcionalidad : {}", funcionalidadDTO);
        if (funcionalidadDTO.getId() != null) {
            throw new BadRequestAlertException("A new funcionalidad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FuncionalidadDTO result = funcionalidadService.save(funcionalidadDTO);
        return ResponseEntity
            .created(new URI("/api/funcionalidads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /funcionalidads/:id} : Updates an existing funcionalidad.
     *
     * @param id the id of the funcionalidadDTO to save.
     * @param funcionalidadDTO the funcionalidadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funcionalidadDTO,
     * or with status {@code 400 (Bad Request)} if the funcionalidadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the funcionalidadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/funcionalidads/{id}")
    public ResponseEntity<FuncionalidadDTO> updateFuncionalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FuncionalidadDTO funcionalidadDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Funcionalidad : {}, {}", id, funcionalidadDTO);
        if (funcionalidadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funcionalidadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!funcionalidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FuncionalidadDTO result = funcionalidadService.update(funcionalidadDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, funcionalidadDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /funcionalidads/:id} : Partial updates given fields of an existing funcionalidad, field will ignore if it is null
     *
     * @param id the id of the funcionalidadDTO to save.
     * @param funcionalidadDTO the funcionalidadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funcionalidadDTO,
     * or with status {@code 400 (Bad Request)} if the funcionalidadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the funcionalidadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the funcionalidadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/funcionalidads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FuncionalidadDTO> partialUpdateFuncionalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FuncionalidadDTO funcionalidadDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Funcionalidad partially : {}, {}", id, funcionalidadDTO);
        if (funcionalidadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funcionalidadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!funcionalidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FuncionalidadDTO> result = funcionalidadService.partialUpdate(funcionalidadDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, funcionalidadDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /funcionalidads} : get all the funcionalidads.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of funcionalidads in body.
     */
    @GetMapping("/funcionalidads")
    public ResponseEntity<List<FuncionalidadDTO>> getAllFuncionalidads(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Funcionalidads");
        Page<FuncionalidadDTO> page = funcionalidadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /funcionalidads/:id} : get the "id" funcionalidad.
     *
     * @param id the id of the funcionalidadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the funcionalidadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/funcionalidads/{id}")
    public ResponseEntity<FuncionalidadDTO> getFuncionalidad(@PathVariable Long id) {
        log.debug("REST request to get Funcionalidad : {}", id);
        Optional<FuncionalidadDTO> funcionalidadDTO = funcionalidadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(funcionalidadDTO);
    }

    /**
     * {@code DELETE  /funcionalidads/:id} : delete the "id" funcionalidad.
     *
     * @param id the id of the funcionalidadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/funcionalidads/{id}")
    public ResponseEntity<Void> deleteFuncionalidad(@PathVariable Long id) {
        log.debug("REST request to delete Funcionalidad : {}", id);
        funcionalidadService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
