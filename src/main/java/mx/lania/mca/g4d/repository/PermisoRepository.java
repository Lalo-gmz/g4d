package mx.lania.mca.g4d.repository;

import mx.lania.mca.g4d.domain.Permiso;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Permiso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {}
