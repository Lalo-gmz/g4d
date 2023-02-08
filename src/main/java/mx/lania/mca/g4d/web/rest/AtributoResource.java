package mx.lania.mca.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mx.lania.mca.g4d.repository.AtributoRepository;
import mx.lania.mca.g4d.service.AtributoService;
import mx.lania.mca.g4d.service.dto.AtributoDTO;
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
 * REST controller for managing {@link mx.lania.mca.g4d.domain.Atributo}.
 */
@RestController
@RequestMapping("/api")
public class AtributoResource {

    private final Logger log = LoggerFactory.getLogger(AtributoResource.class);

    private static final String ENTITY_NAME = "atributo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AtributoService atributoService;

    private final AtributoRepository atributoRepository;

    public AtributoResource(AtributoService atributoService, AtributoRepository atributoRepository) {
        this.atributoService = atributoService;
        this.atributoRepository = atributoRepository;
    }

    /**
     * {@code POST  /atributos} : Create a new atributo.
     *
     * @param atributoDTO the atributoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new atributoDTO, or with status {@code 400 (Bad Request)} if the atributo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/atributos")
    public ResponseEntity<AtributoDTO> createAtributo(@RequestBody AtributoDTO atributoDTO) throws URISyntaxException {
        log.debug("REST request to save Atributo : {}", atributoDTO);
        if (atributoDTO.getId() != null) {
            throw new BadRequestAlertException("A new atributo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AtributoDTO result = atributoService.save(atributoDTO);
        return ResponseEntity
            .created(new URI("/api/atributos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /atributos/:id} : Updates an existing atributo.
     *
     * @param id the id of the atributoDTO to save.
     * @param atributoDTO the atributoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated atributoDTO,
     * or with status {@code 400 (Bad Request)} if the atributoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the atributoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/atributos/{id}")
    public ResponseEntity<AtributoDTO> updateAtributo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AtributoDTO atributoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Atributo : {}, {}", id, atributoDTO);
        if (atributoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, atributoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!atributoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AtributoDTO result = atributoService.update(atributoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, atributoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /atributos/:id} : Partial updates given fields of an existing atributo, field will ignore if it is null
     *
     * @param id the id of the atributoDTO to save.
     * @param atributoDTO the atributoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated atributoDTO,
     * or with status {@code 400 (Bad Request)} if the atributoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the atributoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the atributoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/atributos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AtributoDTO> partialUpdateAtributo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AtributoDTO atributoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Atributo partially : {}, {}", id, atributoDTO);
        if (atributoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, atributoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!atributoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AtributoDTO> result = atributoService.partialUpdate(atributoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, atributoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /atributos} : get all the atributos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of atributos in body.
     */
    @GetMapping("/atributos")
    public ResponseEntity<List<AtributoDTO>> getAllAtributos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Atributos");
        Page<AtributoDTO> page = atributoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /atributos/:id} : get the "id" atributo.
     *
     * @param id the id of the atributoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the atributoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/atributos/{id}")
    public ResponseEntity<AtributoDTO> getAtributo(@PathVariable Long id) {
        log.debug("REST request to get Atributo : {}", id);
        Optional<AtributoDTO> atributoDTO = atributoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(atributoDTO);
    }

    /**
     * {@code DELETE  /atributos/:id} : delete the "id" atributo.
     *
     * @param id the id of the atributoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/atributos/{id}")
    public ResponseEntity<Void> deleteAtributo(@PathVariable Long id) {
        log.debug("REST request to delete Atributo : {}", id);
        atributoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
