package mx.lania.mca.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mx.lania.mca.g4d.repository.ConfiguracionRepository;
import mx.lania.mca.g4d.service.ConfiguracionService;
import mx.lania.mca.g4d.service.dto.ConfiguracionDTO;
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
 * REST controller for managing {@link mx.lania.mca.g4d.domain.Configuracion}.
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

    public ConfiguracionResource(ConfiguracionService configuracionService, ConfiguracionRepository configuracionRepository) {
        this.configuracionService = configuracionService;
        this.configuracionRepository = configuracionRepository;
    }

    /**
     * {@code POST  /configuracions} : Create a new configuracion.
     *
     * @param configuracionDTO the configuracionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configuracionDTO, or with status {@code 400 (Bad Request)} if the configuracion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/configuracions")
    public ResponseEntity<ConfiguracionDTO> createConfiguracion(@RequestBody ConfiguracionDTO configuracionDTO) throws URISyntaxException {
        log.debug("REST request to save Configuracion : {}", configuracionDTO);
        if (configuracionDTO.getId() != null) {
            throw new BadRequestAlertException("A new configuracion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfiguracionDTO result = configuracionService.save(configuracionDTO);
        return ResponseEntity
            .created(new URI("/api/configuracions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /configuracions/:id} : Updates an existing configuracion.
     *
     * @param id the id of the configuracionDTO to save.
     * @param configuracionDTO the configuracionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configuracionDTO,
     * or with status {@code 400 (Bad Request)} if the configuracionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configuracionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/configuracions/{id}")
    public ResponseEntity<ConfiguracionDTO> updateConfiguracion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConfiguracionDTO configuracionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Configuracion : {}, {}", id, configuracionDTO);
        if (configuracionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configuracionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configuracionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConfiguracionDTO result = configuracionService.update(configuracionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, configuracionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /configuracions/:id} : Partial updates given fields of an existing configuracion, field will ignore if it is null
     *
     * @param id the id of the configuracionDTO to save.
     * @param configuracionDTO the configuracionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configuracionDTO,
     * or with status {@code 400 (Bad Request)} if the configuracionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the configuracionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the configuracionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/configuracions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConfiguracionDTO> partialUpdateConfiguracion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConfiguracionDTO configuracionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Configuracion partially : {}, {}", id, configuracionDTO);
        if (configuracionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configuracionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configuracionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConfiguracionDTO> result = configuracionService.partialUpdate(configuracionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, configuracionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /configuracions} : get all the configuracions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configuracions in body.
     */
    @GetMapping("/configuracions")
    public ResponseEntity<List<ConfiguracionDTO>> getAllConfiguracions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Configuracions");
        Page<ConfiguracionDTO> page = configuracionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /configuracions/:id} : get the "id" configuracion.
     *
     * @param id the id of the configuracionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configuracionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/configuracions/{id}")
    public ResponseEntity<ConfiguracionDTO> getConfiguracion(@PathVariable Long id) {
        log.debug("REST request to get Configuracion : {}", id);
        Optional<ConfiguracionDTO> configuracionDTO = configuracionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configuracionDTO);
    }

    /**
     * {@code DELETE  /configuracions/:id} : delete the "id" configuracion.
     *
     * @param id the id of the configuracionDTO to delete.
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
