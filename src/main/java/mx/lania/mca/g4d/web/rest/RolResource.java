package mx.lania.mca.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mx.lania.mca.g4d.repository.RolRepository;
import mx.lania.mca.g4d.service.RolService;
import mx.lania.mca.g4d.service.dto.RolDTO;
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
 * REST controller for managing {@link mx.lania.mca.g4d.domain.Rol}.
 */
@RestController
@RequestMapping("/api")
public class RolResource {

    private final Logger log = LoggerFactory.getLogger(RolResource.class);

    private static final String ENTITY_NAME = "rol";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RolService rolService;

    private final RolRepository rolRepository;

    public RolResource(RolService rolService, RolRepository rolRepository) {
        this.rolService = rolService;
        this.rolRepository = rolRepository;
    }

    /**
     * {@code POST  /rols} : Create a new rol.
     *
     * @param rolDTO the rolDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rolDTO, or with status {@code 400 (Bad Request)} if the rol has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rols")
    public ResponseEntity<RolDTO> createRol(@RequestBody RolDTO rolDTO) throws URISyntaxException {
        log.debug("REST request to save Rol : {}", rolDTO);
        if (rolDTO.getId() != null) {
            throw new BadRequestAlertException("A new rol cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RolDTO result = rolService.save(rolDTO);
        return ResponseEntity
            .created(new URI("/api/rols/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rols/:id} : Updates an existing rol.
     *
     * @param id the id of the rolDTO to save.
     * @param rolDTO the rolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rolDTO,
     * or with status {@code 400 (Bad Request)} if the rolDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rolDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rols/{id}")
    public ResponseEntity<RolDTO> updateRol(@PathVariable(value = "id", required = false) final Long id, @RequestBody RolDTO rolDTO)
        throws URISyntaxException {
        log.debug("REST request to update Rol : {}, {}", id, rolDTO);
        if (rolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rolDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RolDTO result = rolService.update(rolDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rolDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rols/:id} : Partial updates given fields of an existing rol, field will ignore if it is null
     *
     * @param id the id of the rolDTO to save.
     * @param rolDTO the rolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rolDTO,
     * or with status {@code 400 (Bad Request)} if the rolDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rolDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rolDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rols/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RolDTO> partialUpdateRol(@PathVariable(value = "id", required = false) final Long id, @RequestBody RolDTO rolDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update Rol partially : {}, {}", id, rolDTO);
        if (rolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rolDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RolDTO> result = rolService.partialUpdate(rolDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rolDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rols} : get all the rols.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rols in body.
     */
    @GetMapping("/rols")
    public ResponseEntity<List<RolDTO>> getAllRols(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Rols");
        Page<RolDTO> page = rolService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rols/:id} : get the "id" rol.
     *
     * @param id the id of the rolDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rolDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rols/{id}")
    public ResponseEntity<RolDTO> getRol(@PathVariable Long id) {
        log.debug("REST request to get Rol : {}", id);
        Optional<RolDTO> rolDTO = rolService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rolDTO);
    }

    /**
     * {@code DELETE  /rols/:id} : delete the "id" rol.
     *
     * @param id the id of the rolDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rols/{id}")
    public ResponseEntity<Void> deleteRol(@PathVariable Long id) {
        log.debug("REST request to delete Rol : {}", id);
        rolService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
