package mx.lania.mca.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import mx.lania.mca.g4d.repository.BitacoraRepository;
import mx.lania.mca.g4d.service.BitacoraService;
import mx.lania.mca.g4d.service.dto.BitacoraDTO;
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
 * REST controller for managing {@link mx.lania.mca.g4d.domain.Bitacora}.
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

    public BitacoraResource(BitacoraService bitacoraService, BitacoraRepository bitacoraRepository) {
        this.bitacoraService = bitacoraService;
        this.bitacoraRepository = bitacoraRepository;
    }

    /**
     * {@code POST  /bitacoras} : Create a new bitacora.
     *
     * @param bitacoraDTO the bitacoraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bitacoraDTO, or with status {@code 400 (Bad Request)} if the bitacora has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bitacoras")
    public ResponseEntity<BitacoraDTO> createBitacora(@Valid @RequestBody BitacoraDTO bitacoraDTO) throws URISyntaxException {
        log.debug("REST request to save Bitacora : {}", bitacoraDTO);
        if (bitacoraDTO.getId() != null) {
            throw new BadRequestAlertException("A new bitacora cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BitacoraDTO result = bitacoraService.save(bitacoraDTO);
        return ResponseEntity
            .created(new URI("/api/bitacoras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bitacoras/:id} : Updates an existing bitacora.
     *
     * @param id the id of the bitacoraDTO to save.
     * @param bitacoraDTO the bitacoraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bitacoraDTO,
     * or with status {@code 400 (Bad Request)} if the bitacoraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bitacoraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bitacoras/{id}")
    public ResponseEntity<BitacoraDTO> updateBitacora(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BitacoraDTO bitacoraDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Bitacora : {}, {}", id, bitacoraDTO);
        if (bitacoraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bitacoraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bitacoraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BitacoraDTO result = bitacoraService.update(bitacoraDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bitacoraDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bitacoras/:id} : Partial updates given fields of an existing bitacora, field will ignore if it is null
     *
     * @param id the id of the bitacoraDTO to save.
     * @param bitacoraDTO the bitacoraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bitacoraDTO,
     * or with status {@code 400 (Bad Request)} if the bitacoraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bitacoraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bitacoraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bitacoras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BitacoraDTO> partialUpdateBitacora(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BitacoraDTO bitacoraDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Bitacora partially : {}, {}", id, bitacoraDTO);
        if (bitacoraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bitacoraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bitacoraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BitacoraDTO> result = bitacoraService.partialUpdate(bitacoraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bitacoraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bitacoras} : get all the bitacoras.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bitacoras in body.
     */
    @GetMapping("/bitacoras")
    public ResponseEntity<List<BitacoraDTO>> getAllBitacoras(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Bitacoras");
        Page<BitacoraDTO> page = bitacoraService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bitacoras/:id} : get the "id" bitacora.
     *
     * @param id the id of the bitacoraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bitacoraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bitacoras/{id}")
    public ResponseEntity<BitacoraDTO> getBitacora(@PathVariable Long id) {
        log.debug("REST request to get Bitacora : {}", id);
        Optional<BitacoraDTO> bitacoraDTO = bitacoraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bitacoraDTO);
    }

    /**
     * {@code DELETE  /bitacoras/:id} : delete the "id" bitacora.
     *
     * @param id the id of the bitacoraDTO to delete.
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
