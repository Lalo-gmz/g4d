package mx.lania.g4d.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import mx.lania.g4d.domain.*; // for static metamodels
import mx.lania.g4d.domain.Iteracion;
import mx.lania.g4d.repository.IteracionRepository;
import mx.lania.g4d.service.criteria.IteracionCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Iteracion} entities in the database.
 * The main input is a {@link IteracionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Iteracion} or a {@link Page} of {@link Iteracion} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IteracionQueryService extends QueryService<Iteracion> {

    private final Logger log = LoggerFactory.getLogger(IteracionQueryService.class);

    private final IteracionRepository iteracionRepository;

    public IteracionQueryService(IteracionRepository iteracionRepository) {
        this.iteracionRepository = iteracionRepository;
    }

    /**
     * Return a {@link List} of {@link Iteracion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Iteracion> findByCriteria(IteracionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Iteracion> specification = createSpecification(criteria);
        return iteracionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Iteracion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Iteracion> findByCriteria(IteracionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Iteracion> specification = createSpecification(criteria);
        return iteracionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IteracionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Iteracion> specification = createSpecification(criteria);
        return iteracionRepository.count(specification);
    }

    /**
     * Function to convert {@link IteracionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Iteracion> createSpecification(IteracionCriteria criteria) {
        Specification<Iteracion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Iteracion_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Iteracion_.nombre));
            }
            if (criteria.getInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInicio(), Iteracion_.inicio));
            }
            if (criteria.getFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFin(), Iteracion_.fin));
            }
            if (criteria.getFuncionalidadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFuncionalidadId(),
                            root -> root.join(Iteracion_.funcionalidads, JoinType.LEFT).get(Funcionalidad_.id)
                        )
                    );
            }
            if (criteria.getProyectoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProyectoId(),
                            root -> root.join(Iteracion_.proyecto, JoinType.LEFT).get(Proyecto_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
