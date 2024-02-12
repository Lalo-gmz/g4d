package mx.lania.g4d.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import mx.lania.g4d.domain.Proyecto;
import mx.lania.g4d.repository.ProyectoRepository;
import mx.lania.g4d.service.ProyectoService;
import mx.lania.g4d.service.utils.ExcelUploadService;
import mx.lania.g4d.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link mx.lania.g4d.domain.Proyecto}.
 */
@RestController
@RequestMapping("/api")
public class ProyectoResource {

    private final Logger log = LoggerFactory.getLogger(ProyectoResource.class);

    private static final String ENTITY_NAME = "proyecto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProyectoService proyectoService;

    private final ProyectoRepository proyectoRepository;
    private final ExcelUploadService excelUploadService;

    public ProyectoResource(ProyectoService proyectoService, ProyectoRepository proyectoRepository, ExcelUploadService excelUploadService) {
        this.proyectoService = proyectoService;
        this.proyectoRepository = proyectoRepository;
        this.excelUploadService = excelUploadService;
    }

    /**
     * {@code POST  /proyectos} : Create a new proyecto.
     *
     * @param proyecto the proyecto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proyecto, or with status {@code 400 (Bad Request)} if the proyecto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/proyectos")
    public ResponseEntity<Proyecto> createProyecto(@Valid @RequestBody Proyecto proyecto) throws URISyntaxException {
        log.debug("REST request to save Proyecto : {}", proyecto);
        if (proyecto.getId() != null) {
            throw new BadRequestAlertException("A new proyecto cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Proyecto result = proyectoService.save(proyecto);
        if (result.getId() == null) {
            throw new BadRequestAlertException("El nombre del proyecto ya existe. Por favor, elija otro nombre.", ENTITY_NAME, "idexists");
        }

        return ResponseEntity
            .created(new URI("/api/proyectos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /proyectos/:id} : Updates an existing proyecto.
     *
     * @param id the id of the proyecto to save.
     * @param proyecto the proyecto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proyecto,
     * or with status {@code 400 (Bad Request)} if the proyecto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proyecto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/proyectos/{id}")
    public ResponseEntity<Proyecto> updateProyecto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Proyecto proyecto
    ) throws URISyntaxException {
        log.debug("REST request to update Proyecto : {}, {}", id, proyecto);
        if (proyecto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proyecto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proyectoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Proyecto result = proyectoService.update(proyecto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, proyecto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /proyectos/:id} : Partial updates given fields of an existing proyecto, field will ignore if it is null
     *
     * @param id the id of the proyecto to save.
     * @param proyecto the proyecto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proyecto,
     * or with status {@code 400 (Bad Request)} if the proyecto is not valid,
     * or with status {@code 404 (Not Found)} if the proyecto is not found,
     * or with status {@code 500 (Internal Server Error)} if the proyecto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/proyectos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Proyecto> partialUpdateProyecto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Proyecto proyecto
    ) throws URISyntaxException {
        log.debug("REST request to partial update Proyecto partially : {}, {}", id, proyecto);
        if (proyecto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proyecto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proyectoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Proyecto> result = proyectoService.partialUpdate(proyecto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, proyecto.getId().toString())
        );
    }

    /**
     * {@code GET  /proyectos} : get all the proyectos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proyectos in body.
     */
    @GetMapping("/proyectos")
    public List<Proyecto> getAllProyectos(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Proyectos");
        return proyectoService.findAll();
    }

    /**
     * {@code GET  /proyectos/:id} : get the "id" proyecto.
     *
     * @param id the id of the proyecto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proyecto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/proyectos/{id}")
    public ResponseEntity<Proyecto> getProyecto(@PathVariable Long id) {
        log.debug("REST request to get Proyecto : {}", id);
        Optional<Proyecto> proyecto = proyectoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(proyecto);
    }

    @GetMapping("/proyectos/{id}/excel")
    public ResponseEntity<byte[]> generarExcel(@PathVariable Long id) throws IOException {
        byte[] excelBytes = excelUploadService.generarExcel(id, false);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("proyecto-" + id + ".xlsx").build());
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/proyectos/{id}/PlantillaExcel")
    public ResponseEntity<byte[]> generarPlantillaExcel(@PathVariable Long id) throws IOException {
        byte[] excelBytes = excelUploadService.generarExcel(id, true);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("Plantilla-" + id + ".xlsx").build());
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

    /**
     * {@code DELETE  /proyectos/:id} : delete the "id" proyecto.
     *
     * @param id the id of the proyecto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/proyectos/{id}")
    public ResponseEntity<Void> deleteProyecto(@PathVariable Long id) {
        log.debug("REST request to delete Proyecto : {}", id);
        proyectoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
