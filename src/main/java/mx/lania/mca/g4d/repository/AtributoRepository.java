package mx.lania.mca.g4d.repository;

import mx.lania.mca.g4d.domain.Atributo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Atributo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AtributoRepository extends JpaRepository<Atributo, Long> {}
