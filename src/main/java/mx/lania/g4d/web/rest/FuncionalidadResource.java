package mx.lania.g4d.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mx.lania.g4d.domain.Bitacora;
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.domain.User;
import mx.lania.g4d.domain.enumeration.AccionBitacora;
import mx.lania.g4d.repository.BitacoraRepository;
import mx.lania.g4d.repository.FuncionalidadRepository;
import mx.lania.g4d.repository.UserRepository;
import mx.lania.g4d.service.BitacoraService;
import mx.lania.g4d.service.FuncionalidadService;
import mx.lania.g4d.service.Utils.ExcelUploadService;
import mx.lania.g4d.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link mx.lania.g4d.domain.Funcionalidad}.
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

    private final UserRepository userRepository;

    private final BitacoraService bitacoraService;

    private AccionBitacora accionBitacora;

    private ExcelUploadService excelUploadService;

    public FuncionalidadResource(
        FuncionalidadService funcionalidadService,
        FuncionalidadRepository funcionalidadRepository,
        UserRepository userRepository,
        BitacoraService bitacoraService,
        ExcelUploadService excelUploadService
    ) {
        this.funcionalidadService = funcionalidadService;
        this.funcionalidadRepository = funcionalidadRepository;
        this.userRepository = userRepository;
        this.bitacoraService = bitacoraService;
        this.excelUploadService = excelUploadService;
    }

    /**
     * {@code POST  /funcionalidads} : Create a new funcionalidad.
     *
     * @param funcionalidad the funcionalidad to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new funcionalidad, or with status {@code 400 (Bad Request)} if the funcionalidad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/funcionalidads")
    public ResponseEntity<Funcionalidad> createFuncionalidad(@RequestBody Funcionalidad funcionalidad) throws URISyntaxException {
        log.debug("REST request to save Funcionalidad : {}", funcionalidad);
        if (funcionalidad.getId() != null) {
            throw new BadRequestAlertException("A new funcionalidad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Funcionalidad result = funcionalidadService.save(funcionalidad);

        return ResponseEntity
            .created(new URI("/api/funcionalidads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("funcionalidades/excel/proyecto/{id}")
    public ResponseEntity<?> createFuncionalidadByExcel(@RequestParam("file") MultipartFile file, @PathVariable Long id)
        throws IOException {
        /*
        if (excelUploadService.isValidExcelFile(file)) {
            List<Funcionalidad> funcionalidads = excelUploadService.getFuncionalidadsDataFromExcel(file.getInputStream(), id);

            return ResponseEntity.ok(funcionalidadService.saveAll(funcionalidads));
        }
        */
        if (excelUploadService.isValidExcelFile(file)) {
            List<Funcionalidad> funcionalidads = excelUploadService.updateFuncionalidadByExcel(file.getInputStream(), id);

            return ResponseEntity.ok(funcionalidads);
        }

        return ResponseEntity.ok("ok");
    }

    @PostMapping("funcionalidades/excelUpdate/proyecto/{id}")
    public ResponseEntity<?> updateFuncionalidadByExcel(@RequestParam("file") MultipartFile file, @PathVariable Long id)
        throws IOException {
        /*
        if (excelUploadService.isValidExcelFile(file)) {
            List<Funcionalidad> funcionalidads = excelUploadService.getFuncionalidadsDataFromExcel(file.getInputStream(), id);

            return ResponseEntity.ok(funcionalidadService.saveAll(funcionalidads));
        }
        */
        if (excelUploadService.isValidExcelFile(file)) {
            List<Funcionalidad> funcionalidads = excelUploadService.updateFuncionalidadByExcel(file.getInputStream(), id);
            List<Funcionalidad> result = funcionalidadService.saveAll(funcionalidads);
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.ok("ok");
    }

    /**
     * {@code PUT  /funcionalidads/:id} : Updates an existing funcionalidad.
     *
     * @param id the id of the funcionalidad to save.
     * @param funcionalidad the funcionalidad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funcionalidad,
     * or with status {@code 400 (Bad Request)} if the funcionalidad is not valid,
     * or with status {@code 500 (Internal Server Error)} if the funcionalidad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/funcionalidads/{id}")
    public ResponseEntity<Funcionalidad> updateFuncionalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Funcionalidad funcionalidad
    ) throws URISyntaxException {
        log.debug("REST request to update Funcionalidad : {}, {}", id, funcionalidad);
        if (funcionalidad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funcionalidad.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!funcionalidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Funcionalidad prev = funcionalidadService.findOne(funcionalidad.getId()).get();

        Funcionalidad result = funcionalidadService.update(funcionalidad);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        User user = null;
        Optional<User> optionalUser = userRepository.findOneByLogin(login);

        if (optionalUser.isPresent()) {
            user = optionalUser.get();

            if (!result.getEstatusFuncionalidad().equals(prev.getEstatusFuncionalidad())) {
                Bitacora bitacora = new Bitacora();
                bitacora.setFuncionalidad(result);
                bitacora.setUser(user);
                bitacora.setCreado(Instant.now());
                bitacora.setAccion(String.valueOf(AccionBitacora.CAMBIO_ESTATUS) + " a " + result.getEstatusFuncionalidad());
                bitacoraService.save(bitacora);
            }
            if (!result.getIteracion().equals(prev.getIteracion())) {
                Bitacora bitacora = new Bitacora();
                bitacora.setFuncionalidad(result);
                bitacora.setUser(user);
                bitacora.setCreado(Instant.now());
                bitacora.setAccion(String.valueOf(AccionBitacora.CAMBIO_ITERACION) + " a " + result.getIteracion().getNombre());
                bitacoraService.save(bitacora);
            }
            if (!result.getPrioridad().equals(prev.getPrioridad())) {
                Bitacora bitacora = new Bitacora();
                bitacora.setFuncionalidad(result);
                bitacora.setUser(user);
                bitacora.setCreado(Instant.now());
                bitacora.setAccion(String.valueOf(AccionBitacora.CAMBIO_PRIORIDAD) + " a " + result.getPrioridad());
                bitacoraService.save(bitacora);
            }
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, funcionalidad.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /funcionalidads/:id} : Partial updates given fields of an existing funcionalidad, field will ignore if it is null
     *
     * @param id the id of the funcionalidad to save.
     * @param funcionalidad the funcionalidad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated funcionalidad,
     * or with status {@code 400 (Bad Request)} if the funcionalidad is not valid,
     * or with status {@code 404 (Not Found)} if the funcionalidad is not found,
     * or with status {@code 500 (Internal Server Error)} if the funcionalidad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/funcionalidads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Funcionalidad> partialUpdateFuncionalidad(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Funcionalidad funcionalidad
    ) throws URISyntaxException {
        log.debug("REST request to partial update Funcionalidad partially : {}, {}", id, funcionalidad);
        if (funcionalidad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, funcionalidad.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!funcionalidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Funcionalidad> result = funcionalidadService.partialUpdate(funcionalidad);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, funcionalidad.getId().toString())
        );
    }

    /**
     * {@code GET  /funcionalidads} : get all the funcionalidads.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of funcionalidads in body.
     */
    @GetMapping("/funcionalidads")
    public List<Funcionalidad> getAllFuncionalidads(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Funcionalidads");
        return funcionalidadService.findAll();
    }

    @GetMapping("/funcionalidads/iteracion/{iteracionId}")
    public List<Funcionalidad> getAllFuncionalidadsByIteracionId(
        @RequestParam(required = false, defaultValue = "false") boolean eagerload,
        @PathVariable Long iteracionId
    ) {
        log.debug("REST request to get all Funcionalidads");
        return funcionalidadService.findAllByIteracionId(iteracionId);
    }

    /**
     * {@code GET  /funcionalidads/:id} : get the "id" funcionalidad.
     *
     * @param id the id of the funcionalidad to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the funcionalidad, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/funcionalidads/{id}")
    public ResponseEntity<Funcionalidad> getFuncionalidad(@PathVariable Long id) {
        log.debug("REST request to get Funcionalidad : {}", id);
        Optional<Funcionalidad> funcionalidad = funcionalidadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(funcionalidad);
    }

    /**
     * {@code DELETE  /funcionalidads/:id} : delete the "id" funcionalidad.
     *
     * @param id the id of the funcionalidad to delete.
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
