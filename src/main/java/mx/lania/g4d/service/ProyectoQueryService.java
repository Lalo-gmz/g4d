package mx.lania.g4d.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import mx.lania.g4d.domain.*; // for static metamodels
import mx.lania.g4d.domain.Proyecto;
import mx.lania.g4d.repository.ProyectoRepository;
import mx.lania.g4d.service.criteria.ProyectoCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Proyecto} entities in the database.
 * The main input is a {@link ProyectoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Proyecto} or a {@link Page} of {@link Proyecto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProyectoQueryService extends QueryService<Proyecto> {

    private final Logger log = LoggerFactory.getLogger(ProyectoQueryService.class);

    private final ProyectoRepository proyectoRepository;

    public ProyectoQueryService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    /**
     * Return a {@link List} of {@link Proyecto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Proyecto> findByCriteria(ProyectoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Proyecto> specification = createSpecification(criteria);
        return proyectoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Proyecto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Proyecto> findByCriteria(ProyectoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Proyecto> specification = createSpecification(criteria);
        return proyectoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProyectoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Proyecto> specification = createSpecification(criteria);
        return proyectoRepository.count(specification);
    }

    /**
     * Function to convert {@link ProyectoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Proyecto> createSpecification(ProyectoCriteria criteria) {
        Specification<Proyecto> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Proyecto_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Proyecto_.nombre));
            }
            if (criteria.getIdProyectoGitLab() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdProyectoGitLab(), Proyecto_.idProyectoGitLab));
            }
            if (criteria.getParticipacionProyectoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParticipacionProyectoId(),
                            root -> root.join(Proyecto_.participacionProyectos, JoinType.LEFT).get(ParticipacionProyecto_.id)
                        )
                    );
            }
            if (criteria.getConfiguracionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConfiguracionId(),
                            root -> root.join(Proyecto_.configuracions, JoinType.LEFT).get(Configuracion_.id)
                        )
                    );
            }
            if (criteria.getBitacoraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBitacoraId(),
                            root -> root.join(Proyecto_.bitacoras, JoinType.LEFT).get(Bitacora_.id)
                        )
                    );
            }
            if (criteria.getIteracionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getIteracionId(),
                            root -> root.join(Proyecto_.iteracions, JoinType.LEFT).get(Iteracion_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
