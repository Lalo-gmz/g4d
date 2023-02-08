package mx.lania.mca.g4d.service;

import java.util.Optional;
import mx.lania.mca.g4d.domain.Iteracion;
import mx.lania.mca.g4d.repository.IteracionRepository;
import mx.lania.mca.g4d.service.dto.IteracionDTO;
import mx.lania.mca.g4d.service.mapper.IteracionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Iteracion}.
 */
@Service
@Transactional
public class IteracionService {

    private final Logger log = LoggerFactory.getLogger(IteracionService.class);

    private final IteracionRepository iteracionRepository;

    private final IteracionMapper iteracionMapper;

    public IteracionService(IteracionRepository iteracionRepository, IteracionMapper iteracionMapper) {
        this.iteracionRepository = iteracionRepository;
        this.iteracionMapper = iteracionMapper;
    }

    /**
     * Save a iteracion.
     *
     * @param iteracionDTO the entity to save.
     * @return the persisted entity.
     */
    public IteracionDTO save(IteracionDTO iteracionDTO) {
        log.debug("Request to save Iteracion : {}", iteracionDTO);
        Iteracion iteracion = iteracionMapper.toEntity(iteracionDTO);
        iteracion = iteracionRepository.save(iteracion);
        return iteracionMapper.toDto(iteracion);
    }

    /**
     * Update a iteracion.
     *
     * @param iteracionDTO the entity to save.
     * @return the persisted entity.
     */
    public IteracionDTO update(IteracionDTO iteracionDTO) {
        log.debug("Request to update Iteracion : {}", iteracionDTO);
        Iteracion iteracion = iteracionMapper.toEntity(iteracionDTO);
        iteracion = iteracionRepository.save(iteracion);
        return iteracionMapper.toDto(iteracion);
    }

    /**
     * Partially update a iteracion.
     *
     * @param iteracionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IteracionDTO> partialUpdate(IteracionDTO iteracionDTO) {
        log.debug("Request to partially update Iteracion : {}", iteracionDTO);

        return iteracionRepository
            .findById(iteracionDTO.getId())
            .map(existingIteracion -> {
                iteracionMapper.partialUpdate(existingIteracion, iteracionDTO);

                return existingIteracion;
            })
            .map(iteracionRepository::save)
            .map(iteracionMapper::toDto);
    }

    /**
     * Get all the iteracions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IteracionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Iteracions");
        return iteracionRepository.findAll(pageable).map(iteracionMapper::toDto);
    }

    /**
     * Get one iteracion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IteracionDTO> findOne(Long id) {
        log.debug("Request to get Iteracion : {}", id);
        return iteracionRepository.findById(id).map(iteracionMapper::toDto);
    }

    /**
     * Delete the iteracion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Iteracion : {}", id);
        iteracionRepository.deleteById(id);
    }
}
