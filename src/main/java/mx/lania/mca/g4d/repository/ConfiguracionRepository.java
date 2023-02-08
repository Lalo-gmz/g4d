package mx.lania.mca.g4d.repository;

import mx.lania.mca.g4d.domain.Configuracion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Configuracion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {}
