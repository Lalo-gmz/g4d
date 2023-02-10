package mx.lania.g4d.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import mx.lania.g4d.domain.*; // for static metamodels
import mx.lania.g4d.domain.Atributo;
import mx.lania.g4d.repository.AtributoRepository;
import mx.lania.g4d.service.criteria.AtributoCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Atributo} entities in the database.
 * The main input is a {@link AtributoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Atributo} or a {@link Page} of {@link Atributo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AtributoQueryService extends QueryService<Atributo> {

    private final Logger log = LoggerFactory.getLogger(AtributoQueryService.class);

    private final AtributoRepository atributoRepository;

    public AtributoQueryService(AtributoRepository atributoRepository) {
        this.atributoRepository = atributoRepository;
    }

    /**
     * Return a {@link List} of {@link Atributo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Atributo> findByCriteria(AtributoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Atributo> specification = createSpecification(criteria);
        return atributoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Atributo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Atributo> findByCriteria(AtributoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Atributo> specification = createSpecification(criteria);
        return atributoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AtributoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Atributo> specification = createSpecification(criteria);
        return atributoRepository.count(specification);
    }

    /**
     * Function to convert {@link AtributoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Atributo> createSpecification(AtributoCriteria criteria) {
        Specification<Atributo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Atributo_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Atributo_.nombre));
            }
            if (criteria.getMarcado() != null) {
                specification = specification.and(buildSpecification(criteria.getMarcado(), Atributo_.marcado));
            }
            if (criteria.getAuxiliar() != null) {
                specification = specification.and(buildSpecification(criteria.getAuxiliar(), Atributo_.auxiliar));
            }
            if (criteria.getFuncionalidadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFuncionalidadId(),
                            root -> root.join(Atributo_.funcionalidad, JoinType.LEFT).get(Funcionalidad_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
