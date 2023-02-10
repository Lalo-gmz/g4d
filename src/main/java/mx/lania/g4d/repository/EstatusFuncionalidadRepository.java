package mx.lania.g4d.repository;

import mx.lania.g4d.domain.EstatusFuncionalidad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EstatusFuncionalidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EstatusFuncionalidadRepository
    extends JpaRepository<EstatusFuncionalidad, Long>, JpaSpecificationExecutor<EstatusFuncionalidad> {}
