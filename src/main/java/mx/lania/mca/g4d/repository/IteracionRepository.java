package mx.lania.mca.g4d.repository;

import mx.lania.mca.g4d.domain.Iteracion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Iteracion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IteracionRepository extends JpaRepository<Iteracion, Long> {}
