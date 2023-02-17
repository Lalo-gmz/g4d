package mx.lania.g4d.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import mx.lania.g4d.domain.*; // for static metamodels
import mx.lania.g4d.domain.AtributoFuncionalidad;
import mx.lania.g4d.repository.AtributoFuncionalidadRepository;
import mx.lania.g4d.service.criteria.AtributoFuncionalidadCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AtributoFuncionalidad} entities in the database.
 * The main input is a {@link AtributoFuncionalidadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AtributoFuncionalidad} or a {@link Page} of {@link AtributoFuncionalidad} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AtributoFuncionalidadQueryService extends QueryService<AtributoFuncionalidad> {

    private final Logger log = LoggerFactory.getLogger(AtributoFuncionalidadQueryService.class);

    private final AtributoFuncionalidadRepository atributoFuncionalidadRepository;

    public AtributoFuncionalidadQueryService(AtributoFuncionalidadRepository atributoFuncionalidadRepository) {
        this.atributoFuncionalidadRepository = atributoFuncionalidadRepository;
    }

    /**
     * Return a {@link List} of {@link AtributoFuncionalidad} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AtributoFuncionalidad> findByCriteria(AtributoFuncionalidadCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AtributoFuncionalidad> specification = createSpecification(criteria);
        return atributoFuncionalidadRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AtributoFuncionalidad} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AtributoFuncionalidad> findByCriteria(AtributoFuncionalidadCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AtributoFuncionalidad> specification = createSpecification(criteria);
        return atributoFuncionalidadRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AtributoFuncionalidadCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AtributoFuncionalidad> specification = createSpecification(criteria);
        return atributoFuncionalidadRepository.count(specification);
    }

    /**
     * Function to convert {@link AtributoFuncionalidadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AtributoFuncionalidad> createSpecification(AtributoFuncionalidadCriteria criteria) {
        Specification<AtributoFuncionalidad> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AtributoFuncionalidad_.id));
            }
            if (criteria.getMarcado() != null) {
                specification = specification.and(buildSpecification(criteria.getMarcado(), AtributoFuncionalidad_.marcado));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValor(), AtributoFuncionalidad_.valor));
            }
            if (criteria.getFuncionalidadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFuncionalidadId(),
                            root -> root.join(AtributoFuncionalidad_.funcionalidad, JoinType.LEFT).get(Funcionalidad_.id)
                        )
                    );
            }
            if (criteria.getAtributoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAtributoId(),
                            root -> root.join(AtributoFuncionalidad_.atributo, JoinType.LEFT).get(Atributo_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
