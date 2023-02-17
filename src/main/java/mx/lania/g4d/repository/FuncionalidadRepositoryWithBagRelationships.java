package mx.lania.g4d.repository;

import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.Funcionalidad;
import org.springframework.data.domain.Page;

public interface FuncionalidadRepositoryWithBagRelationships {
    Optional<Funcionalidad> fetchBagRelationships(Optional<Funcionalidad> funcionalidad);

    List<Funcionalidad> fetchBagRelationships(List<Funcionalidad> funcionalidads);

    Page<Funcionalidad> fetchBagRelationships(Page<Funcionalidad> funcionalidads);
}
