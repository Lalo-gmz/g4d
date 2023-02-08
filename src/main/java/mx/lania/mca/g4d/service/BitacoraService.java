package mx.lania.mca.g4d.service;

import java.util.Optional;
import mx.lania.mca.g4d.domain.Bitacora;
import mx.lania.mca.g4d.repository.BitacoraRepository;
import mx.lania.mca.g4d.service.dto.BitacoraDTO;
import mx.lania.mca.g4d.service.mapper.BitacoraMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Bitacora}.
 */
@Service
@Transactional
public class BitacoraService {

    private final Logger log = LoggerFactory.getLogger(BitacoraService.class);

    private final BitacoraRepository bitacoraRepository;

    private final BitacoraMapper bitacoraMapper;

    public BitacoraService(BitacoraRepository bitacoraRepository, BitacoraMapper bitacoraMapper) {
        this.bitacoraRepository = bitacoraRepository;
        this.bitacoraMapper = bitacoraMapper;
    }

    /**
     * Save a bitacora.
     *
     * @param bitacoraDTO the entity to save.
     * @return the persisted entity.
     */
    public BitacoraDTO save(BitacoraDTO bitacoraDTO) {
        log.debug("Request to save Bitacora : {}", bitacoraDTO);
        Bitacora bitacora = bitacoraMapper.toEntity(bitacoraDTO);
        bitacora = bitacoraRepository.save(bitacora);
        return bitacoraMapper.toDto(bitacora);
    }

    /**
     * Update a bitacora.
     *
     * @param bitacoraDTO the entity to save.
     * @return the persisted entity.
     */
    public BitacoraDTO update(BitacoraDTO bitacoraDTO) {
        log.debug("Request to update Bitacora : {}", bitacoraDTO);
        Bitacora bitacora = bitacoraMapper.toEntity(bitacoraDTO);
        bitacora = bitacoraRepository.save(bitacora);
        return bitacoraMapper.toDto(bitacora);
    }

    /**
     * Partially update a bitacora.
     *
     * @param bitacoraDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BitacoraDTO> partialUpdate(BitacoraDTO bitacoraDTO) {
        log.debug("Request to partially update Bitacora : {}", bitacoraDTO);

        return bitacoraRepository
            .findById(bitacoraDTO.getId())
            .map(existingBitacora -> {
                bitacoraMapper.partialUpdate(existingBitacora, bitacoraDTO);

                return existingBitacora;
            })
            .map(bitacoraRepository::save)
            .map(bitacoraMapper::toDto);
    }

    /**
     * Get all the bitacoras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BitacoraDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bitacoras");
        return bitacoraRepository.findAll(pageable).map(bitacoraMapper::toDto);
    }

    /**
     * Get one bitacora by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BitacoraDTO> findOne(Long id) {
        log.debug("Request to get Bitacora : {}", id);
        return bitacoraRepository.findById(id).map(bitacoraMapper::toDto);
    }

    /**
     * Delete the bitacora by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Bitacora : {}", id);
        bitacoraRepository.deleteById(id);
    }
}
