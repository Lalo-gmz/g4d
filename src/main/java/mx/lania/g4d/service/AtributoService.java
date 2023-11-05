package mx.lania.g4d.service;

import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.Atributo;
import mx.lania.g4d.repository.AtributoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Atributo}.
 */
@Service
@Transactional
public class AtributoService {

    private final Logger log = LoggerFactory.getLogger(AtributoService.class);

    private final AtributoRepository atributoRepository;

    public AtributoService(AtributoRepository atributoRepository) {
        this.atributoRepository = atributoRepository;
    }

    /**
     * Save a atributo.
     *
     * @param atributo the entity to save.
     * @return the persisted entity.
     */
    public Atributo save(Atributo atributo) {
        log.debug("Request to save Atributo : {}", atributo);
        return atributoRepository.save(atributo);
    }

    /**
     * Save an atributo List.
     *
     * @param atributo the entity to save.
     * @return the persisted entity.
     */
    public List<Atributo> saveAll(List<Atributo> atributoList) {
        log.debug("Request to save atributoList");
        return atributoRepository.saveAll(atributoList);
    }

    /**
     * Update a atributo.
     *
     * @param atributo the entity to save.
     * @return the persisted entity.
     */
    public Atributo update(Atributo atributo) {
        log.debug("Request to update Atributo : {}", atributo);
        return atributoRepository.save(atributo);
    }

    /**
     * Partially update a atributo.
     *
     * @param atributo the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Atributo> partialUpdate(Atributo atributo) {
        log.debug("Request to partially update Atributo : {}", atributo);

        return atributoRepository
            .findById(atributo.getId())
            .map(existingAtributo -> {
                if (atributo.getNombre() != null) {
                    existingAtributo.setNombre(atributo.getNombre());
                }

                return existingAtributo;
            })
            .map(atributoRepository::save);
    }

    /**
     * Get all the atributos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Atributo> findAll() {
        log.debug("Request to get all Atributos");
        return atributoRepository.findAll();
    }

    /**
     * Get one atributo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Atributo> findOne(Long id) {
        log.debug("Request to get Atributo : {}", id);
        return atributoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Atributo> findOneByNombre(String nombre) {
        log.debug("Request to get Atributo : {}", nombre);
        return atributoRepository.findOneByNombre(nombre);
    }

    /**
     * Delete the atributo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Atributo : {}", id);
        atributoRepository.deleteById(id);
    }
}
