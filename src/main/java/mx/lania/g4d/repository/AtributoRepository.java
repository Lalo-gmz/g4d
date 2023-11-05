package mx.lania.g4d.repository;

import java.util.Optional;
import mx.lania.g4d.domain.Atributo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Atributo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AtributoRepository extends JpaRepository<Atributo, Long> {
    Optional<Atributo> findOneByNombre(String nombre);
}
