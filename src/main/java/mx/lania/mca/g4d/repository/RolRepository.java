package mx.lania.mca.g4d.repository;

import mx.lania.mca.g4d.domain.Rol;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Rol entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {}
