package mx.lania.g4d.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import mx.lania.g4d.domain.*; // for static metamodels
import mx.lania.g4d.domain.Permiso;
import mx.lania.g4d.repository.PermisoRepository;
import mx.lania.g4d.service.criteria.PermisoCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Permiso} entities in the database.
 * The main input is a {@link PermisoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Permiso} or a {@link Page} of {@link Permiso} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PermisoQueryService extends QueryService<Permiso> {

    private final Logger log = LoggerFactory.getLogger(PermisoQueryService.class);

    private final PermisoRepository permisoRepository;

    public PermisoQueryService(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    /**
     * Return a {@link List} of {@link Permiso} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Permiso> findByCriteria(PermisoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Permiso> specification = createSpecification(criteria);
        return permisoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Permiso} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Permiso> findByCriteria(PermisoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Permiso> specification = createSpecification(criteria);
        return permisoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PermisoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Permiso> specification = createSpecification(criteria);
        return permisoRepository.count(specification);
    }

    /**
     * Function to convert {@link PermisoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Permiso> createSpecification(PermisoCriteria criteria) {
        Specification<Permiso> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Permiso_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Permiso_.nombre));
            }
            if (criteria.getRolId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getRolId(), root -> root.join(Permiso_.rol, JoinType.LEFT).get(Rol_.id)));
            }
        }
        return specification;
    }
}
