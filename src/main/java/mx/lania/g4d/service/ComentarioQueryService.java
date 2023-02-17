package mx.lania.g4d.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import mx.lania.g4d.domain.*; // for static metamodels
import mx.lania.g4d.domain.Comentario;
import mx.lania.g4d.repository.ComentarioRepository;
import mx.lania.g4d.service.criteria.ComentarioCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Comentario} entities in the database.
 * The main input is a {@link ComentarioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Comentario} or a {@link Page} of {@link Comentario} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComentarioQueryService extends QueryService<Comentario> {

    private final Logger log = LoggerFactory.getLogger(ComentarioQueryService.class);

    private final ComentarioRepository comentarioRepository;

    public ComentarioQueryService(ComentarioRepository comentarioRepository) {
        this.comentarioRepository = comentarioRepository;
    }

    /**
     * Return a {@link List} of {@link Comentario} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Comentario> findByCriteria(ComentarioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Comentario> specification = createSpecification(criteria);
        return comentarioRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Comentario} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Comentario> findByCriteria(ComentarioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Comentario> specification = createSpecification(criteria);
        return comentarioRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ComentarioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Comentario> specification = createSpecification(criteria);
        return comentarioRepository.count(specification);
    }

    /**
     * Function to convert {@link ComentarioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Comentario> createSpecification(ComentarioCriteria criteria) {
        Specification<Comentario> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Comentario_.id));
            }
            if (criteria.getMensaje() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMensaje(), Comentario_.mensaje));
            }
            if (criteria.getCreado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreado(), Comentario_.creado));
            }
            if (criteria.getModificado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModificado(), Comentario_.modificado));
            }
            if (criteria.getFuncionalidadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFuncionalidadId(),
                            root -> root.join(Comentario_.funcionalidad, JoinType.LEFT).get(Funcionalidad_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Comentario_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
