package mx.lania.g4d.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import mx.lania.g4d.domain.*; // for static metamodels
import mx.lania.g4d.domain.Bitacora;
import mx.lania.g4d.repository.BitacoraRepository;
import mx.lania.g4d.service.criteria.BitacoraCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Bitacora} entities in the database.
 * The main input is a {@link BitacoraCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Bitacora} or a {@link Page} of {@link Bitacora} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BitacoraQueryService extends QueryService<Bitacora> {

    private final Logger log = LoggerFactory.getLogger(BitacoraQueryService.class);

    private final BitacoraRepository bitacoraRepository;

    public BitacoraQueryService(BitacoraRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }

    /**
     * Return a {@link List} of {@link Bitacora} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Bitacora> findByCriteria(BitacoraCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Bitacora> specification = createSpecification(criteria);
        return bitacoraRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Bitacora} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Bitacora> findByCriteria(BitacoraCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Bitacora> specification = createSpecification(criteria);
        return bitacoraRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BitacoraCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Bitacora> specification = createSpecification(criteria);
        return bitacoraRepository.count(specification);
    }

    /**
     * Function to convert {@link BitacoraCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Bitacora> createSpecification(BitacoraCriteria criteria) {
        Specification<Bitacora> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Bitacora_.id));
            }
            if (criteria.getTabla() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTabla(), Bitacora_.tabla));
            }
            if (criteria.getAccion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccion(), Bitacora_.accion));
            }
            if (criteria.getCreado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreado(), Bitacora_.creado));
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(Bitacora_.usuario, JoinType.LEFT).get(Usuario_.id))
                    );
            }
            if (criteria.getProyectoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProyectoId(), root -> root.join(Bitacora_.proyecto, JoinType.LEFT).get(Proyecto_.id))
                    );
            }
        }
        return specification;
    }
}
