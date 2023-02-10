package mx.lania.g4d.repository;

import mx.lania.g4d.domain.Permiso;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Permiso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long>, JpaSpecificationExecutor<Permiso> {}
