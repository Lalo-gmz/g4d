package mx.lania.g4d.repository;

import java.util.List;
import mx.lania.g4d.domain.Comentario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Comentario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long>, JpaSpecificationExecutor<Comentario> {
    @Query("select comentario from Comentario comentario where comentario.user.login = ?#{principal.username}")
    List<Comentario> findByUserIsCurrentUser();
}
