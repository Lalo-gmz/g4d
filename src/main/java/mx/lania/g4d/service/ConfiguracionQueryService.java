package mx.lania.g4d.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import mx.lania.g4d.domain.*; // for static metamodels
import mx.lania.g4d.domain.Configuracion;
import mx.lania.g4d.repository.ConfiguracionRepository;
import mx.lania.g4d.service.criteria.ConfiguracionCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Configuracion} entities in the database.
 * The main input is a {@link ConfiguracionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Configuracion} or a {@link Page} of {@link Configuracion} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConfiguracionQueryService extends QueryService<Configuracion> {

    private final Logger log = LoggerFactory.getLogger(ConfiguracionQueryService.class);

    private final ConfiguracionRepository configuracionRepository;

    public ConfiguracionQueryService(ConfiguracionRepository configuracionRepository) {
        this.configuracionRepository = configuracionRepository;
    }

    /**
     * Return a {@link List} of {@link Configuracion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Configuracion> findByCriteria(ConfiguracionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Configuracion> specification = createSpecification(criteria);
        return configuracionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Configuracion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Configuracion> findByCriteria(ConfiguracionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Configuracion> specification = createSpecification(criteria);
        return configuracionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConfiguracionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Configuracion> specification = createSpecification(criteria);
        return configuracionRepository.count(specification);
    }

    /**
     * Function to convert {@link ConfiguracionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Configuracion> createSpecification(ConfiguracionCriteria criteria) {
        Specification<Configuracion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Configuracion_.id));
            }
            if (criteria.getClave() != null) {
                specification = specification.and(buildSpecification(criteria.getClave(), Configuracion_.clave));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValor(), Configuracion_.valor));
            }
            if (criteria.getProyectoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProyectoId(),
                            root -> root.join(Configuracion_.proyecto, JoinType.LEFT).get(Proyecto_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
