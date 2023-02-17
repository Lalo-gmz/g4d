package mx.lania.g4d.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import mx.lania.g4d.domain.*; // for static metamodels
import mx.lania.g4d.domain.ParticipacionProyecto;
import mx.lania.g4d.repository.ParticipacionProyectoRepository;
import mx.lania.g4d.service.criteria.ParticipacionProyectoCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ParticipacionProyecto} entities in the database.
 * The main input is a {@link ParticipacionProyectoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ParticipacionProyecto} or a {@link Page} of {@link ParticipacionProyecto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParticipacionProyectoQueryService extends QueryService<ParticipacionProyecto> {

    private final Logger log = LoggerFactory.getLogger(ParticipacionProyectoQueryService.class);

    private final ParticipacionProyectoRepository participacionProyectoRepository;

    public ParticipacionProyectoQueryService(ParticipacionProyectoRepository participacionProyectoRepository) {
        this.participacionProyectoRepository = participacionProyectoRepository;
    }

    /**
     * Return a {@link List} of {@link ParticipacionProyecto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ParticipacionProyecto> findByCriteria(ParticipacionProyectoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ParticipacionProyecto> specification = createSpecification(criteria);
        return participacionProyectoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ParticipacionProyecto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ParticipacionProyecto> findByCriteria(ParticipacionProyectoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ParticipacionProyecto> specification = createSpecification(criteria);
        return participacionProyectoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParticipacionProyectoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ParticipacionProyecto> specification = createSpecification(criteria);
        return participacionProyectoRepository.count(specification);
    }

    /**
     * Function to convert {@link ParticipacionProyectoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ParticipacionProyecto> createSpecification(ParticipacionProyectoCriteria criteria) {
        Specification<ParticipacionProyecto> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ParticipacionProyecto_.id));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserId(),
                            root -> root.join(ParticipacionProyecto_.user, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
            if (criteria.getProyectoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProyectoId(),
                            root -> root.join(ParticipacionProyecto_.proyecto, JoinType.LEFT).get(Proyecto_.id)
                        )
                    );
            }
            if (criteria.getRolId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRolId(), root -> root.join(ParticipacionProyecto_.rol, JoinType.LEFT).get(Rol_.id))
                    );
            }
        }
        return specification;
    }
}
