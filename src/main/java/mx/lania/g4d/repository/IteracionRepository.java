package mx.lania.g4d.repository;

import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.Iteracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Iteracion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IteracionRepository extends JpaRepository<Iteracion, Long> {
    List<Iteracion> findAllByProyectoId(Long id);

    Optional<Iteracion> findByNombreAndProyectoId(String nombre, Long id);
}
