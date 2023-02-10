package mx.lania.g4d.repository;

import mx.lania.g4d.domain.Bitacora;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Bitacora entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Long>, JpaSpecificationExecutor<Bitacora> {}
