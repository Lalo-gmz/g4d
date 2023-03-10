package mx.lania.g4d.repository;

import java.util.List;
import mx.lania.g4d.domain.Iteracion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Iteracion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IteracionRepository extends JpaRepository<Iteracion, Long> {
    List<Iteracion> findAllByProyectoId(Long id);
}
