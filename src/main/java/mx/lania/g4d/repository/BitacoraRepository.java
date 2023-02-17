package mx.lania.g4d.repository;

import java.util.List;
import mx.lania.g4d.domain.Bitacora;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Bitacora entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Long>, JpaSpecificationExecutor<Bitacora> {
    @Query("select bitacora from Bitacora bitacora where bitacora.user.login = ?#{principal.username}")
    List<Bitacora> findByUserIsCurrentUser();
}
