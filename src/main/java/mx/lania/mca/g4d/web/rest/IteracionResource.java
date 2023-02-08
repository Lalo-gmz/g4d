package mx.lania.mca.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import mx.lania.mca.g4d.repository.IteracionRepository;
import mx.lania.mca.g4d.service.IteracionService;
import mx.lania.mca.g4d.service.dto.IteracionDTO;
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
 * REST controller for managing {@link mx.lania.mca.g4d.domain.Iteracion}.
 */
@RestController
@RequestMapping("/api")
public class IteracionResource {

    private final Logger log = LoggerFactory.getLogger(IteracionResource.class);

    private static final String ENTITY_NAME = "iteracion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IteracionService iteracionService;

    private final IteracionRepository iteracionRepository;

    public IteracionResource(IteracionService iteracionService, IteracionRepository iteracionRepository) {
        this.iteracionService = iteracionService;
        this.iteracionRepository = iteracionRepository;
    }

    /**
     * {@code POST  /iteracions} : Create a new iteracion.
     *
     * @param iteracionDTO the iteracionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iteracionDTO, or with status {@code 400 (Bad Request)} if the iteracion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/iteracions")
    public ResponseEntity<IteracionDTO> createIteracion(@Valid @RequestBody IteracionDTO iteracionDTO) throws URISyntaxException {
        log.debug("REST request to save Iteracion : {}", iteracionDTO);
        if (iteracionDTO.getId() != null) {
            throw new BadRequestAlertException("A new iteracion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IteracionDTO result = iteracionService.save(iteracionDTO);
        return ResponseEntity
            .created(new URI("/api/iteracions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /iteracions/:id} : Updates an existing iteracion.
     *
     * @param id the id of the iteracionDTO to save.
     * @param iteracionDTO the iteracionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iteracionDTO,
     * or with status {@code 400 (Bad Request)} if the iteracionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iteracionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/iteracions/{id}")
    public ResponseEntity<IteracionDTO> updateIteracion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IteracionDTO iteracionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Iteracion : {}, {}", id, iteracionDTO);
        if (iteracionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iteracionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iteracionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IteracionDTO result = iteracionService.update(iteracionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, iteracionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /iteracions/:id} : Partial updates given fields of an existing iteracion, field will ignore if it is null
     *
     * @param id the id of the iteracionDTO to save.
     * @param iteracionDTO the iteracionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iteracionDTO,
     * or with status {@code 400 (Bad Request)} if the iteracionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the iteracionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the iteracionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/iteracions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IteracionDTO> partialUpdateIteracion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IteracionDTO iteracionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Iteracion partially : {}, {}", id, iteracionDTO);
        if (iteracionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iteracionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iteracionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IteracionDTO> result = iteracionService.partialUpdate(iteracionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, iteracionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /iteracions} : get all the iteracions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iteracions in body.
     */
    @GetMapping("/iteracions")
    public ResponseEntity<List<IteracionDTO>> getAllIteracions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Iteracions");
        Page<IteracionDTO> page = iteracionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /iteracions/:id} : get the "id" iteracion.
     *
     * @param id the id of the iteracionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iteracionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/iteracions/{id}")
    public ResponseEntity<IteracionDTO> getIteracion(@PathVariable Long id) {
        log.debug("REST request to get Iteracion : {}", id);
        Optional<IteracionDTO> iteracionDTO = iteracionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iteracionDTO);
    }

    /**
     * {@code DELETE  /iteracions/:id} : delete the "id" iteracion.
     *
     * @param id the id of the iteracionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/iteracions/{id}")
    public ResponseEntity<Void> deleteIteracion(@PathVariable Long id) {
        log.debug("REST request to delete Iteracion : {}", id);
        iteracionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
