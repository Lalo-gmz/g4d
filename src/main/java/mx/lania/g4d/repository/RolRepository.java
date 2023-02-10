package mx.lania.g4d.repository;

import mx.lania.g4d.domain.Rol;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Rol entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RolRepository extends JpaRepository<Rol, Long>, JpaSpecificationExecutor<Rol> {}
