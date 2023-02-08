package mx.lania.mca.g4d.service;

import java.util.Optional;
import mx.lania.mca.g4d.domain.Proyecto;
import mx.lania.mca.g4d.repository.ProyectoRepository;
import mx.lania.mca.g4d.service.dto.ProyectoDTO;
import mx.lania.mca.g4d.service.mapper.ProyectoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Proyecto}.
 */
@Service
@Transactional
public class ProyectoService {

    private final Logger log = LoggerFactory.getLogger(ProyectoService.class);

    private final ProyectoRepository proyectoRepository;

    private final ProyectoMapper proyectoMapper;

    public ProyectoService(ProyectoRepository proyectoRepository, ProyectoMapper proyectoMapper) {
        this.proyectoRepository = proyectoRepository;
        this.proyectoMapper = proyectoMapper;
    }

    /**
     * Save a proyecto.
     *
     * @param proyectoDTO the entity to save.
     * @return the persisted entity.
     */
    public ProyectoDTO save(ProyectoDTO proyectoDTO) {
        log.debug("Request to save Proyecto : {}", proyectoDTO);
        Proyecto proyecto = proyectoMapper.toEntity(proyectoDTO);
        proyecto = proyectoRepository.save(proyecto);
        return proyectoMapper.toDto(proyecto);
    }

    /**
     * Update a proyecto.
     *
     * @param proyectoDTO the entity to save.
     * @return the persisted entity.
     */
    public ProyectoDTO update(ProyectoDTO proyectoDTO) {
        log.debug("Request to update Proyecto : {}", proyectoDTO);
        Proyecto proyecto = proyectoMapper.toEntity(proyectoDTO);
        proyecto = proyectoRepository.save(proyecto);
        return proyectoMapper.toDto(proyecto);
    }

    /**
     * Partially update a proyecto.
     *
     * @param proyectoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProyectoDTO> partialUpdate(ProyectoDTO proyectoDTO) {
        log.debug("Request to partially update Proyecto : {}", proyectoDTO);

        return proyectoRepository
            .findById(proyectoDTO.getId())
            .map(existingProyecto -> {
                proyectoMapper.partialUpdate(existingProyecto, proyectoDTO);

                return existingProyecto;
            })
            .map(proyectoRepository::save)
            .map(proyectoMapper::toDto);
    }

    /**
     * Get all the proyectos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProyectoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Proyectos");
        return proyectoRepository.findAll(pageable).map(proyectoMapper::toDto);
    }

    /**
     * Get one proyecto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProyectoDTO> findOne(Long id) {
        log.debug("Request to get Proyecto : {}", id);
        return proyectoRepository.findById(id).map(proyectoMapper::toDto);
    }

    /**
     * Delete the proyecto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Proyecto : {}", id);
        proyectoRepository.deleteById(id);
    }
}
