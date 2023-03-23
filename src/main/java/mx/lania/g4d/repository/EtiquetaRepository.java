package mx.lania.g4d.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import mx.lania.g4d.domain.Etiqueta;
import mx.lania.g4d.domain.Funcionalidad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Etiqueta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {
    Optional<List<Etiqueta>> findAllByFuncionalidadId(Long id);

    Optional<Etiqueta> findByNombre(String nombre);

    Set<Etiqueta> findByFuncionalidad(Funcionalidad funcionalidad);
}
