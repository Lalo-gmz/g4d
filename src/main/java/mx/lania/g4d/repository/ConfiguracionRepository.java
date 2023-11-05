package mx.lania.g4d.repository;

import mx.lania.g4d.domain.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Configuracion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {}
