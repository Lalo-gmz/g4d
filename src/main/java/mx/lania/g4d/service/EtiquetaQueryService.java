package mx.lania.g4d.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import mx.lania.g4d.domain.*; // for static metamodels
import mx.lania.g4d.domain.Etiqueta;
import mx.lania.g4d.repository.EtiquetaRepository;
import mx.lania.g4d.service.criteria.EtiquetaCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Etiqueta} entities in the database.
 * The main input is a {@link EtiquetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Etiqueta} or a {@link Page} of {@link Etiqueta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EtiquetaQueryService extends QueryService<Etiqueta> {

    private final Logger log = LoggerFactory.getLogger(EtiquetaQueryService.class);

    private final EtiquetaRepository etiquetaRepository;

    public EtiquetaQueryService(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    /**
     * Return a {@link List} of {@link Etiqueta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Etiqueta> findByCriteria(EtiquetaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Etiqueta> specification = createSpecification(criteria);
        return etiquetaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Etiqueta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Etiqueta> findByCriteria(EtiquetaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Etiqueta> specification = createSpecification(criteria);
        return etiquetaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EtiquetaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Etiqueta> specification = createSpecification(criteria);
        return etiquetaRepository.count(specification);
    }

    /**
     * Function to convert {@link EtiquetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Etiqueta> createSpecification(EtiquetaCriteria criteria) {
        Specification<Etiqueta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Etiqueta_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Etiqueta_.nombre));
            }
            if (criteria.getPrioridad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrioridad(), Etiqueta_.prioridad));
            }
            if (criteria.getFuncionalidadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFuncionalidadId(),
                            root -> root.join(Etiqueta_.funcionalidad, JoinType.LEFT).get(Funcionalidad_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
