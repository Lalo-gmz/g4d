package mx.lania.g4d.service;

import java.util.Optional;
import mx.lania.g4d.domain.Bitacora;
import mx.lania.g4d.repository.BitacoraRepository;
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

    public BitacoraService(BitacoraRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }

    /**
     * Save a bitacora.
     *
     * @param bitacora the entity to save.
     * @return the persisted entity.
     */
    public Bitacora save(Bitacora bitacora) {
        log.debug("Request to save Bitacora : {}", bitacora);
        return bitacoraRepository.save(bitacora);
    }

    /**
     * Update a bitacora.
     *
     * @param bitacora the entity to save.
     * @return the persisted entity.
     */
    public Bitacora update(Bitacora bitacora) {
        log.debug("Request to update Bitacora : {}", bitacora);
        return bitacoraRepository.save(bitacora);
    }

    /**
     * Partially update a bitacora.
     *
     * @param bitacora the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Bitacora> partialUpdate(Bitacora bitacora) {
        log.debug("Request to partially update Bitacora : {}", bitacora);

        return bitacoraRepository
            .findById(bitacora.getId())
            .map(existingBitacora -> {
                if (bitacora.getTabla() != null) {
                    existingBitacora.setTabla(bitacora.getTabla());
                }
                if (bitacora.getAccion() != null) {
                    existingBitacora.setAccion(bitacora.getAccion());
                }
                if (bitacora.getCreado() != null) {
                    existingBitacora.setCreado(bitacora.getCreado());
                }

                return existingBitacora;
            })
            .map(bitacoraRepository::save);
    }

    /**
     * Get all the bitacoras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Bitacora> findAll(Pageable pageable) {
        log.debug("Request to get all Bitacoras");
        return bitacoraRepository.findAll(pageable);
    }

    /**
     * Get one bitacora by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Bitacora> findOne(Long id) {
        log.debug("Request to get Bitacora : {}", id);
        return bitacoraRepository.findById(id);
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
