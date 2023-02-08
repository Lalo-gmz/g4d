package mx.lania.mca.g4d.service;

import java.util.Optional;
import mx.lania.mca.g4d.domain.Funcionalidad;
import mx.lania.mca.g4d.repository.FuncionalidadRepository;
import mx.lania.mca.g4d.service.dto.FuncionalidadDTO;
import mx.lania.mca.g4d.service.mapper.FuncionalidadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Funcionalidad}.
 */
@Service
@Transactional
public class FuncionalidadService {

    private final Logger log = LoggerFactory.getLogger(FuncionalidadService.class);

    private final FuncionalidadRepository funcionalidadRepository;

    private final FuncionalidadMapper funcionalidadMapper;

    public FuncionalidadService(FuncionalidadRepository funcionalidadRepository, FuncionalidadMapper funcionalidadMapper) {
        this.funcionalidadRepository = funcionalidadRepository;
        this.funcionalidadMapper = funcionalidadMapper;
    }

    /**
     * Save a funcionalidad.
     *
     * @param funcionalidadDTO the entity to save.
     * @return the persisted entity.
     */
    public FuncionalidadDTO save(FuncionalidadDTO funcionalidadDTO) {
        log.debug("Request to save Funcionalidad : {}", funcionalidadDTO);
        Funcionalidad funcionalidad = funcionalidadMapper.toEntity(funcionalidadDTO);
        funcionalidad = funcionalidadRepository.save(funcionalidad);
        return funcionalidadMapper.toDto(funcionalidad);
    }

    /**
     * Update a funcionalidad.
     *
     * @param funcionalidadDTO the entity to save.
     * @return the persisted entity.
     */
    public FuncionalidadDTO update(FuncionalidadDTO funcionalidadDTO) {
        log.debug("Request to update Funcionalidad : {}", funcionalidadDTO);
        Funcionalidad funcionalidad = funcionalidadMapper.toEntity(funcionalidadDTO);
        funcionalidad = funcionalidadRepository.save(funcionalidad);
        return funcionalidadMapper.toDto(funcionalidad);
    }

    /**
     * Partially update a funcionalidad.
     *
     * @param funcionalidadDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FuncionalidadDTO> partialUpdate(FuncionalidadDTO funcionalidadDTO) {
        log.debug("Request to partially update Funcionalidad : {}", funcionalidadDTO);

        return funcionalidadRepository
            .findById(funcionalidadDTO.getId())
            .map(existingFuncionalidad -> {
                funcionalidadMapper.partialUpdate(existingFuncionalidad, funcionalidadDTO);

                return existingFuncionalidad;
            })
            .map(funcionalidadRepository::save)
            .map(funcionalidadMapper::toDto);
    }

    /**
     * Get all the funcionalidads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FuncionalidadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Funcionalidads");
        return funcionalidadRepository.findAll(pageable).map(funcionalidadMapper::toDto);
    }

    /**
     * Get one funcionalidad by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FuncionalidadDTO> findOne(Long id) {
        log.debug("Request to get Funcionalidad : {}", id);
        return funcionalidadRepository.findById(id).map(funcionalidadMapper::toDto);
    }

    /**
     * Delete the funcionalidad by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Funcionalidad : {}", id);
        funcionalidadRepository.deleteById(id);
    }
}
