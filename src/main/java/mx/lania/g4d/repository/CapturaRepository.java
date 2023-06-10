package mx.lania.g4d.repository;

import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.Captura;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Captura entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CapturaRepository extends JpaRepository<Captura, Long> {
    Optional<List<Captura>> getAllByProyectoId(Long id);
}
