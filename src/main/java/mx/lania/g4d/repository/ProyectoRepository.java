package mx.lania.g4d.repository;

import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.ParticipacionProyecto;
import mx.lania.g4d.domain.Proyecto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Proyecto entity.
 *
 * When extending this class, extend ProyectoRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ProyectoRepository extends ProyectoRepositoryWithBagRelationships, JpaRepository<Proyecto, Long> {
    default Optional<Proyecto> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Proyecto> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Proyecto> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
