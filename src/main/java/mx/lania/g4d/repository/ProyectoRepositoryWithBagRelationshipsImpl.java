package mx.lania.g4d.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mx.lania.g4d.domain.Proyecto;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ProyectoRepositoryWithBagRelationshipsImpl implements ProyectoRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Proyecto> fetchBagRelationships(Optional<Proyecto> proyecto) {
        return proyecto.map(this::fetchParticipantes);
    }

    @Override
    public Page<Proyecto> fetchBagRelationships(Page<Proyecto> proyectos) {
        return new PageImpl<>(fetchBagRelationships(proyectos.getContent()), proyectos.getPageable(), proyectos.getTotalElements());
    }

    @Override
    public List<Proyecto> fetchBagRelationships(List<Proyecto> proyectos) {
        return Optional.of(proyectos).map(this::fetchParticipantes).orElse(Collections.emptyList());
    }

    Proyecto fetchParticipantes(Proyecto result) {
        return entityManager
            .createQuery(
                "select proyecto from Proyecto proyecto left join fetch proyecto.participantes where proyecto is :proyecto",
                Proyecto.class
            )
            .setParameter("proyecto", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Proyecto> fetchParticipantes(List<Proyecto> proyectos) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, proyectos.size()).forEach(index -> order.put(proyectos.get(index).getId(), index));
        List<Proyecto> result = entityManager
            .createQuery(
                "select distinct proyecto from Proyecto proyecto left join fetch proyecto.participantes where proyecto in :proyectos",
                Proyecto.class
            )
            .setParameter("proyectos", proyectos)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
