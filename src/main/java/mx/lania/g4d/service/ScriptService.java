package mx.lania.g4d.service;

import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.Script;
import mx.lania.g4d.repository.ScriptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScriptService {

    private final ScriptRepository scriptRepository;

    public ScriptService(ScriptRepository scriptRepository) {
        this.scriptRepository = scriptRepository;
    }

    public Script save(Script script) {
        return scriptRepository.save(script);
    }

    public Script update(Script script) {
        return scriptRepository.save(script);
    }

    @Transactional
    public List<Script> findAll() {
        return scriptRepository.findAll();
    }

    @Transactional
    public Optional<Script> findOne(Long id) {
        return scriptRepository.findById(id);
    }

    @Transactional
    public Optional<List<Script>> findAllByProyectoId(Long id) {
        return scriptRepository.findAllByProyectoIdOrderByOrdenAsc(id);
    }

    public void delete(Long id) {
        scriptRepository.deleteById(id);
    }
}
