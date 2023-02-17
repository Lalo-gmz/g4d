package mx.lania.g4d.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import mx.lania.g4d.domain.*; // for static metamodels
import mx.lania.g4d.domain.Prioridad;
import mx.lania.g4d.repository.PrioridadRepository;
import mx.lania.g4d.service.criteria.PrioridadCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Prioridad} entities in the database.
 * The main input is a {@link PrioridadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Prioridad} or a {@link Page} of {@link Prioridad} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrioridadQueryService extends QueryService<Prioridad> {

    private final Logger log = LoggerFactory.getLogger(PrioridadQueryService.class);

    private final PrioridadRepository prioridadRepository;

    public PrioridadQueryService(PrioridadRepository prioridadRepository) {
        this.prioridadRepository = prioridadRepository;
    }

    /**
     * Return a {@link List} of {@link Prioridad} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Prioridad> findByCriteria(PrioridadCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Prioridad> specification = createSpecification(criteria);
        return prioridadRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Prioridad} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Prioridad> findByCriteria(PrioridadCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Prioridad> specification = createSpecification(criteria);
        return prioridadRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrioridadCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Prioridad> specification = createSpecification(criteria);
        return prioridadRepository.count(specification);
    }

    /**
     * Function to convert {@link PrioridadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Prioridad> createSpecification(PrioridadCriteria criteria) {
        Specification<Prioridad> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Prioridad_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Prioridad_.nombre));
            }
            if (criteria.getPrioridadNumerica() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrioridadNumerica(), Prioridad_.prioridadNumerica));
            }
            if (criteria.getFuncionalidadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFuncionalidadId(),
                            root -> root.join(Prioridad_.funcionalidads, JoinType.LEFT).get(Funcionalidad_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
