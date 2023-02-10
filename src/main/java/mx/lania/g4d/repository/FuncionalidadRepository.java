package mx.lania.g4d.repository;

import mx.lania.g4d.domain.Funcionalidad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Funcionalidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FuncionalidadRepository extends JpaRepository<Funcionalidad, Long>, JpaSpecificationExecutor<Funcionalidad> {}
