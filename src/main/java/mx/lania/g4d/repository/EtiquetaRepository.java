package mx.lania.g4d.repository;

import mx.lania.g4d.domain.Etiqueta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Etiqueta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long>, JpaSpecificationExecutor<Etiqueta> {}
