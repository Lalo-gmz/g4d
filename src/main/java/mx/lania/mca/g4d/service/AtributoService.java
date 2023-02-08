package mx.lania.mca.g4d.service;

import java.util.Optional;
import mx.lania.mca.g4d.domain.Atributo;
import mx.lania.mca.g4d.repository.AtributoRepository;
import mx.lania.mca.g4d.service.dto.AtributoDTO;
import mx.lania.mca.g4d.service.mapper.AtributoMapper;
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

    private final AtributoMapper atributoMapper;

    public AtributoService(AtributoRepository atributoRepository, AtributoMapper atributoMapper) {
        this.atributoRepository = atributoRepository;
        this.atributoMapper = atributoMapper;
    }

    /**
     * Save a atributo.
     *
     * @param atributoDTO the entity to save.
     * @return the persisted entity.
     */
    public AtributoDTO save(AtributoDTO atributoDTO) {
        log.debug("Request to save Atributo : {}", atributoDTO);
        Atributo atributo = atributoMapper.toEntity(atributoDTO);
        atributo = atributoRepository.save(atributo);
        return atributoMapper.toDto(atributo);
    }

    /**
     * Update a atributo.
     *
     * @param atributoDTO the entity to save.
     * @return the persisted entity.
     */
    public AtributoDTO update(AtributoDTO atributoDTO) {
        log.debug("Request to update Atributo : {}", atributoDTO);
        Atributo atributo = atributoMapper.toEntity(atributoDTO);
        atributo = atributoRepository.save(atributo);
        return atributoMapper.toDto(atributo);
    }

    /**
     * Partially update a atributo.
     *
     * @param atributoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AtributoDTO> partialUpdate(AtributoDTO atributoDTO) {
        log.debug("Request to partially update Atributo : {}", atributoDTO);

        return atributoRepository
            .findById(atributoDTO.getId())
            .map(existingAtributo -> {
                atributoMapper.partialUpdate(existingAtributo, atributoDTO);

                return existingAtributo;
            })
            .map(atributoRepository::save)
            .map(atributoMapper::toDto);
    }

    /**
     * Get all the atributos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AtributoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Atributos");
        return atributoRepository.findAll(pageable).map(atributoMapper::toDto);
    }

    /**
     * Get one atributo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AtributoDTO> findOne(Long id) {
        log.debug("Request to get Atributo : {}", id);
        return atributoRepository.findById(id).map(atributoMapper::toDto);
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
