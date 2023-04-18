package mx.lania.g4d.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import mx.lania.g4d.domain.AtributoFuncionalidad;
import mx.lania.g4d.domain.Funcionalidad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AtributoFuncionalidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AtributoFuncionalidadRepository extends JpaRepository<AtributoFuncionalidad, Long> {
    Optional<List<AtributoFuncionalidad>> findAtributoFuncionalidadByFuncionalidadId(Long id);
    Optional<Set<AtributoFuncionalidad>> findAtributoFuncionalidadByFuncionalidad(Funcionalidad funcionalidad);
}
