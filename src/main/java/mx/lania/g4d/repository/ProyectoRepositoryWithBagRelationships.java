package mx.lania.g4d.repository;

import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.Proyecto;
import org.springframework.data.domain.Page;

public interface ProyectoRepositoryWithBagRelationships {
    Optional<Proyecto> fetchBagRelationships(Optional<Proyecto> proyecto);

    List<Proyecto> fetchBagRelationships(List<Proyecto> proyectos);

    Page<Proyecto> fetchBagRelationships(Page<Proyecto> proyectos);
}
