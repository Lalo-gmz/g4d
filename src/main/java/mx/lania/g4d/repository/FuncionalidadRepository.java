package mx.lania.g4d.repository;

import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.Funcionalidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Funcionalidad entity.
 *
 * When extending this class, extend FuncionalidadRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface FuncionalidadRepository extends FuncionalidadRepositoryWithBagRelationships, JpaRepository<Funcionalidad, Long> {
    default Optional<Funcionalidad> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Funcionalidad> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Funcionalidad> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    default List<Funcionalidad> findAllWithEagerRelationshipsByIteracionId(Long id) {
        return this.fetchBagRelationships(this.findAllByIteracionId(id));
    }

    default List<Funcionalidad> findAllWithEagerRelationshipsByProyectoId(Long id) {
        return this.fetchBagRelationships(this.findAllByProyectoId(id));
    }

    List<Funcionalidad> findAllByIteracionId(Long id);

    @Query("SELECT f FROM Funcionalidad f JOIN f.iteracion i JOIN i.proyecto p WHERE p.id = :proyectoId")
    List<Funcionalidad> findAllByProyectoId(@Param("proyectoId") Long proyectoId);
}
