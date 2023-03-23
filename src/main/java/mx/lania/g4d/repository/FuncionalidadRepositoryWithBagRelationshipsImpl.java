package mx.lania.g4d.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mx.lania.g4d.domain.Funcionalidad;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class FuncionalidadRepositoryWithBagRelationshipsImpl implements FuncionalidadRepositoryWithBagRelationships {

    private final EtiquetaRepository etiquetaRepository;

    FuncionalidadRepositoryWithBagRelationshipsImpl(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Funcionalidad> fetchBagRelationships(Optional<Funcionalidad> funcionalidad) {
        return funcionalidad.map(this::fetchUsers);
    }

    @Override
    public Page<Funcionalidad> fetchBagRelationships(Page<Funcionalidad> funcionalidads) {
        return new PageImpl<>(
            fetchBagRelationships(funcionalidads.getContent()),
            funcionalidads.getPageable(),
            funcionalidads.getTotalElements()
        );
    }

    @Override
    public List<Funcionalidad> fetchBagRelationships(List<Funcionalidad> funcionalidads) {
        List<Funcionalidad> result = Optional.of(funcionalidads).map(this::fetchUsers).orElse(Collections.emptyList());
        result.forEach(funcionalidad -> {
            funcionalidad.setEtiquetas(etiquetaRepository.findByFuncionalidad(funcionalidad));
        });
        return result;
    }

    Funcionalidad fetchUsers(Funcionalidad result) {
        return entityManager
            .createQuery(
                "select funcionalidad from Funcionalidad funcionalidad left join fetch funcionalidad.users where funcionalidad is :funcionalidad",
                Funcionalidad.class
            )
            .setParameter("funcionalidad", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Funcionalidad> fetchUsers(List<Funcionalidad> funcionalidads) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, funcionalidads.size()).forEach(index -> order.put(funcionalidads.get(index).getId(), index));
        List<Funcionalidad> result = entityManager
            .createQuery(
                "select distinct funcionalidad from Funcionalidad funcionalidad left join fetch funcionalidad.users where funcionalidad in :funcionalidads",
                Funcionalidad.class
            )
            .setParameter("funcionalidads", funcionalidads)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
