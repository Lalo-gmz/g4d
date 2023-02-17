package mx.lania.g4d.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import mx.lania.g4d.domain.*; // for static metamodels
import mx.lania.g4d.domain.Funcionalidad;
import mx.lania.g4d.repository.FuncionalidadRepository;
import mx.lania.g4d.service.criteria.FuncionalidadCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Funcionalidad} entities in the database.
 * The main input is a {@link FuncionalidadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Funcionalidad} or a {@link Page} of {@link Funcionalidad} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FuncionalidadQueryService extends QueryService<Funcionalidad> {

    private final Logger log = LoggerFactory.getLogger(FuncionalidadQueryService.class);

    private final FuncionalidadRepository funcionalidadRepository;

    public FuncionalidadQueryService(FuncionalidadRepository funcionalidadRepository) {
        this.funcionalidadRepository = funcionalidadRepository;
    }

    /**
     * Return a {@link List} of {@link Funcionalidad} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Funcionalidad> findByCriteria(FuncionalidadCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Funcionalidad> specification = createSpecification(criteria);
        return funcionalidadRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Funcionalidad} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Funcionalidad> findByCriteria(FuncionalidadCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Funcionalidad> specification = createSpecification(criteria);
        return funcionalidadRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FuncionalidadCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Funcionalidad> specification = createSpecification(criteria);
        return funcionalidadRepository.count(specification);
    }

    /**
     * Function to convert {@link FuncionalidadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Funcionalidad> createSpecification(FuncionalidadCriteria criteria) {
        Specification<Funcionalidad> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Funcionalidad_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Funcionalidad_.nombre));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Funcionalidad_.descripcion));
            }
            if (criteria.getUrlGitLab() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlGitLab(), Funcionalidad_.urlGitLab));
            }
            if (criteria.getFechaEntrega() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaEntrega(), Funcionalidad_.fechaEntrega));
            }
            if (criteria.getCreado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreado(), Funcionalidad_.creado));
            }
            if (criteria.getModificado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModificado(), Funcionalidad_.modificado));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Funcionalidad_.users, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getEstatusFuncionalidadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEstatusFuncionalidadId(),
                            root -> root.join(Funcionalidad_.estatusFuncionalidad, JoinType.LEFT).get(EstatusFuncionalidad_.id)
                        )
                    );
            }
            if (criteria.getIteracionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getIteracionId(),
                            root -> root.join(Funcionalidad_.iteracion, JoinType.LEFT).get(Iteracion_.id)
                        )
                    );
            }
            if (criteria.getPrioridadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrioridadId(),
                            root -> root.join(Funcionalidad_.prioridad, JoinType.LEFT).get(Prioridad_.id)
                        )
                    );
            }
            if (criteria.getEtiquetaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEtiquetaId(),
                            root -> root.join(Funcionalidad_.etiquetas, JoinType.LEFT).get(Etiqueta_.id)
                        )
                    );
            }
            if (criteria.getAtributoFuncionalidadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAtributoFuncionalidadId(),
                            root -> root.join(Funcionalidad_.atributoFuncionalidads, JoinType.LEFT).get(AtributoFuncionalidad_.id)
                        )
                    );
            }
            if (criteria.getComentarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getComentarioId(),
                            root -> root.join(Funcionalidad_.comentarios, JoinType.LEFT).get(Comentario_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
