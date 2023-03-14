package mx.lania.g4d.repository;

import java.util.Optional;
import mx.lania.g4d.domain.Prioridad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Prioridad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrioridadRepository extends JpaRepository<Prioridad, Long> {
    Optional<Prioridad> findByNombre(String nombre);
}
