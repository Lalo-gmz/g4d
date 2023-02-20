package mx.lania.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import mx.lania.g4d.domain.Comentario;
import mx.lania.g4d.repository.ComentarioRepository;
import mx.lania.g4d.service.ComentarioQueryService;
import mx.lania.g4d.service.ComentarioService;
import mx.lania.g4d.service.criteria.ComentarioCriteria;
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
 * REST controller for managing {@link mx.lania.g4d.domain.Comentario}.
 */
@RestController
@RequestMapping("/api")
public class ComentarioResource {

    private final Logger log = LoggerFactory.getLogger(ComentarioResource.class);

    private static final String ENTITY_NAME = "comentario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComentarioService comentarioService;

    private final ComentarioRepository comentarioRepository;

    private final ComentarioQueryService comentarioQueryService;

    public ComentarioResource(
        ComentarioService comentarioService,
        ComentarioRepository comentarioRepository,
        ComentarioQueryService comentarioQueryService
    ) {
        this.comentarioService = comentarioService;
        this.comentarioRepository = comentarioRepository;
        this.comentarioQueryService = comentarioQueryService;
    }

    /**
     * {@code POST  /comentarios} : Create a new comentario.
     *
     * @param comentario the comentario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comentario, or with status {@code 400 (Bad Request)} if the comentario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comentarios")
    public ResponseEntity<Comentario> createComentario(@Valid @RequestBody Comentario comentario) throws URISyntaxException {
        log.debug("REST request to save Comentario : {}", comentario);
        if (comentario.getId() != null) {
            throw new BadRequestAlertException("A new comentario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Comentario result = comentarioService.save(comentario);
        return ResponseEntity
            .created(new URI("/api/comentarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comentarios/:id} : Updates an existing comentario.
     *
     * @param id the id of the comentario to save.
     * @param comentario the comentario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comentario,
     * or with status {@code 400 (Bad Request)} if the comentario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comentario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comentarios/{id}")
    public ResponseEntity<Comentario> updateComentario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Comentario comentario
    ) throws URISyntaxException {
        log.debug("REST request to update Comentario : {}, {}", id, comentario);
        if (comentario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comentario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comentarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Comentario result = comentarioService.update(comentario);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, comentario.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /comentarios/:id} : Partial updates given fields of an existing comentario, field will ignore if it is null
     *
     * @param id the id of the comentario to save.
     * @param comentario the comentario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comentario,
     * or with status {@code 400 (Bad Request)} if the comentario is not valid,
     * or with status {@code 404 (Not Found)} if the comentario is not found,
     * or with status {@code 500 (Internal Server Error)} if the comentario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comentarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Comentario> partialUpdateComentario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Comentario comentario
    ) throws URISyntaxException {
        log.debug("REST request to partial update Comentario partially : {}, {}", id, comentario);
        if (comentario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comentario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comentarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Comentario> result = comentarioService.partialUpdate(comentario);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, comentario.getId().toString())
        );
    }

    /**
     * {@code GET  /comentarios} : get all the comentarios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comentarios in body.
     */
    @GetMapping("/comentarios")
    public ResponseEntity<List<Comentario>> getAllComentarios(
        ComentarioCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Comentarios by criteria: {}", criteria);
        Page<Comentario> page = comentarioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comentarios/count} : count all the comentarios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/comentarios/count")
    public ResponseEntity<Long> countComentarios(ComentarioCriteria criteria) {
        log.debug("REST request to count Comentarios by criteria: {}", criteria);
        return ResponseEntity.ok().body(comentarioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /comentarios/:id} : get the "id" comentario.
     *
     * @param id the id of the comentario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comentario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comentarios/{id}")
    public ResponseEntity<Comentario> getComentario(@PathVariable Long id) {
        log.debug("REST request to get Comentario : {}", id);
        Optional<Comentario> comentario = comentarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(comentario);
    }

    /**
     * {@code GET  /comentarios/:id} : get the "id" comentario.
     *
     * @param id the id of the funciton to retrieve comentario.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comentario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comentarios/funcionalidad/{id}")
    public ResponseEntity<List<Comentario>> getComentarioByFuncId(@PathVariable Long id) {
        log.debug("REST request to get Comentarios by func id : {}", id);
        Optional<List<Comentario>> comentario = comentarioService.findAllByFuncId(id);
        return ResponseUtil.wrapOrNotFound(comentario);
    }

    /**
     * {@code DELETE  /comentarios/:id} : delete the "id" comentario.
     *
     * @param id the id of the comentario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comentarios/{id}")
    public ResponseEntity<Void> deleteComentario(@PathVariable Long id) {
        log.debug("REST request to delete Comentario : {}", id);
        comentarioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
