package mx.lania.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import mx.lania.g4d.domain.Bitacora;
import mx.lania.g4d.repository.BitacoraRepository;
import mx.lania.g4d.service.BitacoraQueryService;
import mx.lania.g4d.service.BitacoraService;
import mx.lania.g4d.service.criteria.BitacoraCriteria;
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
 * REST controller for managing {@link mx.lania.g4d.domain.Bitacora}.
 */
@RestController
@RequestMapping("/api")
public class BitacoraResource {

    private final Logger log = LoggerFactory.getLogger(BitacoraResource.class);

    private static final String ENTITY_NAME = "bitacora";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BitacoraService bitacoraService;

    private final BitacoraRepository bitacoraRepository;

    private final BitacoraQueryService bitacoraQueryService;

    public BitacoraResource(
        BitacoraService bitacoraService,
        BitacoraRepository bitacoraRepository,
        BitacoraQueryService bitacoraQueryService
    ) {
        this.bitacoraService = bitacoraService;
        this.bitacoraRepository = bitacoraRepository;
        this.bitacoraQueryService = bitacoraQueryService;
    }

    /**
     * {@code POST  /bitacoras} : Create a new bitacora.
     *
     * @param bitacora the bitacora to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bitacora, or with status {@code 400 (Bad Request)} if the bitacora has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bitacoras")
    public ResponseEntity<Bitacora> createBitacora(@Valid @RequestBody Bitacora bitacora) throws URISyntaxException {
        log.debug("REST request to save Bitacora : {}", bitacora);
        if (bitacora.getId() != null) {
            throw new BadRequestAlertException("A new bitacora cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bitacora result = bitacoraService.save(bitacora);
        return ResponseEntity
            .created(new URI("/api/bitacoras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bitacoras/:id} : Updates an existing bitacora.
     *
     * @param id the id of the bitacora to save.
     * @param bitacora the bitacora to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bitacora,
     * or with status {@code 400 (Bad Request)} if the bitacora is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bitacora couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bitacoras/{id}")
    public ResponseEntity<Bitacora> updateBitacora(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Bitacora bitacora
    ) throws URISyntaxException {
        log.debug("REST request to update Bitacora : {}, {}", id, bitacora);
        if (bitacora.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bitacora.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bitacoraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Bitacora result = bitacoraService.update(bitacora);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bitacora.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bitacoras/:id} : Partial updates given fields of an existing bitacora, field will ignore if it is null
     *
     * @param id the id of the bitacora to save.
     * @param bitacora the bitacora to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bitacora,
     * or with status {@code 400 (Bad Request)} if the bitacora is not valid,
     * or with status {@code 404 (Not Found)} if the bitacora is not found,
     * or with status {@code 500 (Internal Server Error)} if the bitacora couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bitacoras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Bitacora> partialUpdateBitacora(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Bitacora bitacora
    ) throws URISyntaxException {
        log.debug("REST request to partial update Bitacora partially : {}, {}", id, bitacora);
        if (bitacora.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bitacora.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bitacoraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Bitacora> result = bitacoraService.partialUpdate(bitacora);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bitacora.getId().toString())
        );
    }

    /**
     * {@code GET  /bitacoras} : get all the bitacoras.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bitacoras in body.
     */
    @GetMapping("/bitacoras")
    public ResponseEntity<List<Bitacora>> getAllBitacoras(
        BitacoraCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Bitacoras by criteria: {}", criteria);
        Page<Bitacora> page = bitacoraQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bitacoras/count} : count all the bitacoras.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bitacoras/count")
    public ResponseEntity<Long> countBitacoras(BitacoraCriteria criteria) {
        log.debug("REST request to count Bitacoras by criteria: {}", criteria);
        return ResponseEntity.ok().body(bitacoraQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bitacoras/:id} : get the "id" bitacora.
     *
     * @param id the id of the bitacora to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bitacora, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bitacoras/{id}")
    public ResponseEntity<Bitacora> getBitacora(@PathVariable Long id) {
        log.debug("REST request to get Bitacora : {}", id);
        Optional<Bitacora> bitacora = bitacoraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bitacora);
    }

    /**
     * {@code DELETE  /bitacoras/:id} : delete the "id" bitacora.
     *
     * @param id the id of the bitacora to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bitacoras/{id}")
    public ResponseEntity<Void> deleteBitacora(@PathVariable Long id) {
        log.debug("REST request to delete Bitacora : {}", id);
        bitacoraService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
