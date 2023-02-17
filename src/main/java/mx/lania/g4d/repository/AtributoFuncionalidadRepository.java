package mx.lania.g4d.repository;

import mx.lania.g4d.domain.AtributoFuncionalidad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AtributoFuncionalidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AtributoFuncionalidadRepository
    extends JpaRepository<AtributoFuncionalidad, Long>, JpaSpecificationExecutor<AtributoFuncionalidad> {}
