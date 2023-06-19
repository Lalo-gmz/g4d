package mx.lania.g4d.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import mx.lania.g4d.domain.Bitacora;
import mx.lania.g4d.domain.Script;
import mx.lania.g4d.repository.ScriptRepository;
import mx.lania.g4d.service.ScriptService;
import mx.lania.g4d.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class ScriptResource {

    private static final String ENTITY_NAME = "script";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScriptService scriptService;
    private final ScriptRepository scriptRepository;

    public ScriptResource(ScriptService scriptService, ScriptRepository scriptRepository) {
        this.scriptService = scriptService;
        this.scriptRepository = scriptRepository;
    }

    @PostMapping("/scripts")
    public ResponseEntity<Script> createScript(@RequestBody Script script) throws URISyntaxException {
        if (script.getId() != null) {
            throw new BadRequestAlertException("A new atributo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Script result = scriptService.save(script);
        return ResponseEntity
            .created(new URI("/api/scripts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/scripts/{id}")
    public ResponseEntity<Script> updateScript(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Script script
    ) throws URISyntaxException {
        if (script.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, script.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scriptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Script result = scriptService.update(script);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, script.getId().toString()))
            .body(result);
    }

    @GetMapping("/scripts")
    public List<Script> getAllScripts() {
        return scriptService.findAll();
    }

    @GetMapping("/scripts/{id}")
    public ResponseEntity<Script> getScript(@PathVariable Long id) {
        Optional<Script> script = scriptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(script);
    }

    @GetMapping("/scripts/proyecto/{id}")
    public List<Script> getScriptsByProyectoId(@PathVariable Long id) {
        Optional<List<Script>> script = scriptService.findAllByProyectoId(id);
        return script.orElseGet(ArrayList::new);
    }

    @DeleteMapping("/scripts/{id}")
    public ResponseEntity<Void> deleteScript(@PathVariable Long id) {
        scriptService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
