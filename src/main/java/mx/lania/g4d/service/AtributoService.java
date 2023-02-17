package mx.lania.g4d.service;

import java.util.Optional;
import mx.lania.g4d.domain.Atributo;
import mx.lania.g4d.repository.AtributoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Atributo> findAll(Pageable pageable) {
        log.debug("Request to get all Atributos");
        return atributoRepository.findAll(pageable);
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
