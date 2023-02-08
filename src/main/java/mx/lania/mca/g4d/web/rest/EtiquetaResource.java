package mx.lania.mca.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import mx.lania.mca.g4d.repository.EtiquetaRepository;
import mx.lania.mca.g4d.service.EtiquetaService;
import mx.lania.mca.g4d.service.dto.EtiquetaDTO;
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
 * REST controller for managing {@link mx.lania.mca.g4d.domain.Etiqueta}.
 */
@RestController
@RequestMapping("/api")
public class EtiquetaResource {

    private final Logger log = LoggerFactory.getLogger(EtiquetaResource.class);

    private static final String ENTITY_NAME = "etiqueta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtiquetaService etiquetaService;

    private final EtiquetaRepository etiquetaRepository;

    public EtiquetaResource(EtiquetaService etiquetaService, EtiquetaRepository etiquetaRepository) {
        this.etiquetaService = etiquetaService;
        this.etiquetaRepository = etiquetaRepository;
    }

    /**
     * {@code POST  /etiquetas} : Create a new etiqueta.
     *
     * @param etiquetaDTO the etiquetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etiquetaDTO, or with status {@code 400 (Bad Request)} if the etiqueta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/etiquetas")
    public ResponseEntity<EtiquetaDTO> createEtiqueta(@Valid @RequestBody EtiquetaDTO etiquetaDTO) throws URISyntaxException {
        log.debug("REST request to save Etiqueta : {}", etiquetaDTO);
        if (etiquetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new etiqueta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EtiquetaDTO result = etiquetaService.save(etiquetaDTO);
        return ResponseEntity
            .created(new URI("/api/etiquetas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /etiquetas/:id} : Updates an existing etiqueta.
     *
     * @param id the id of the etiquetaDTO to save.
     * @param etiquetaDTO the etiquetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etiquetaDTO,
     * or with status {@code 400 (Bad Request)} if the etiquetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etiquetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/etiquetas/{id}")
    public ResponseEntity<EtiquetaDTO> updateEtiqueta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EtiquetaDTO etiquetaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Etiqueta : {}, {}", id, etiquetaDTO);
        if (etiquetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etiquetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etiquetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EtiquetaDTO result = etiquetaService.update(etiquetaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, etiquetaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /etiquetas/:id} : Partial updates given fields of an existing etiqueta, field will ignore if it is null
     *
     * @param id the id of the etiquetaDTO to save.
     * @param etiquetaDTO the etiquetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etiquetaDTO,
     * or with status {@code 400 (Bad Request)} if the etiquetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the etiquetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the etiquetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/etiquetas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EtiquetaDTO> partialUpdateEtiqueta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EtiquetaDTO etiquetaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Etiqueta partially : {}, {}", id, etiquetaDTO);
        if (etiquetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, etiquetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!etiquetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EtiquetaDTO> result = etiquetaService.partialUpdate(etiquetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, etiquetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /etiquetas} : get all the etiquetas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etiquetas in body.
     */
    @GetMapping("/etiquetas")
    public ResponseEntity<List<EtiquetaDTO>> getAllEtiquetas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Etiquetas");
        Page<EtiquetaDTO> page = etiquetaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /etiquetas/:id} : get the "id" etiqueta.
     *
     * @param id the id of the etiquetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etiquetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/etiquetas/{id}")
    public ResponseEntity<EtiquetaDTO> getEtiqueta(@PathVariable Long id) {
        log.debug("REST request to get Etiqueta : {}", id);
        Optional<EtiquetaDTO> etiquetaDTO = etiquetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(etiquetaDTO);
    }

    /**
     * {@code DELETE  /etiquetas/:id} : delete the "id" etiqueta.
     *
     * @param id the id of the etiquetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/etiquetas/{id}")
    public ResponseEntity<Void> deleteEtiqueta(@PathVariable Long id) {
        log.debug("REST request to delete Etiqueta : {}", id);
        etiquetaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
