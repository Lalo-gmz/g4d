package mx.lania.mca.g4d.service;

import java.util.Optional;
import mx.lania.mca.g4d.domain.Etiqueta;
import mx.lania.mca.g4d.repository.EtiquetaRepository;
import mx.lania.mca.g4d.service.dto.EtiquetaDTO;
import mx.lania.mca.g4d.service.mapper.EtiquetaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Etiqueta}.
 */
@Service
@Transactional
public class EtiquetaService {

    private final Logger log = LoggerFactory.getLogger(EtiquetaService.class);

    private final EtiquetaRepository etiquetaRepository;

    private final EtiquetaMapper etiquetaMapper;

    public EtiquetaService(EtiquetaRepository etiquetaRepository, EtiquetaMapper etiquetaMapper) {
        this.etiquetaRepository = etiquetaRepository;
        this.etiquetaMapper = etiquetaMapper;
    }

    /**
     * Save a etiqueta.
     *
     * @param etiquetaDTO the entity to save.
     * @return the persisted entity.
     */
    public EtiquetaDTO save(EtiquetaDTO etiquetaDTO) {
        log.debug("Request to save Etiqueta : {}", etiquetaDTO);
        Etiqueta etiqueta = etiquetaMapper.toEntity(etiquetaDTO);
        etiqueta = etiquetaRepository.save(etiqueta);
        return etiquetaMapper.toDto(etiqueta);
    }

    /**
     * Update a etiqueta.
     *
     * @param etiquetaDTO the entity to save.
     * @return the persisted entity.
     */
    public EtiquetaDTO update(EtiquetaDTO etiquetaDTO) {
        log.debug("Request to update Etiqueta : {}", etiquetaDTO);
        Etiqueta etiqueta = etiquetaMapper.toEntity(etiquetaDTO);
        etiqueta = etiquetaRepository.save(etiqueta);
        return etiquetaMapper.toDto(etiqueta);
    }

    /**
     * Partially update a etiqueta.
     *
     * @param etiquetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EtiquetaDTO> partialUpdate(EtiquetaDTO etiquetaDTO) {
        log.debug("Request to partially update Etiqueta : {}", etiquetaDTO);

        return etiquetaRepository
            .findById(etiquetaDTO.getId())
            .map(existingEtiqueta -> {
                etiquetaMapper.partialUpdate(existingEtiqueta, etiquetaDTO);

                return existingEtiqueta;
            })
            .map(etiquetaRepository::save)
            .map(etiquetaMapper::toDto);
    }

    /**
     * Get all the etiquetas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EtiquetaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Etiquetas");
        return etiquetaRepository.findAll(pageable).map(etiquetaMapper::toDto);
    }

    /**
     * Get one etiqueta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EtiquetaDTO> findOne(Long id) {
        log.debug("Request to get Etiqueta : {}", id);
        return etiquetaRepository.findById(id).map(etiquetaMapper::toDto);
    }

    /**
     * Delete the etiqueta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Etiqueta : {}", id);
        etiquetaRepository.deleteById(id);
    }
}
