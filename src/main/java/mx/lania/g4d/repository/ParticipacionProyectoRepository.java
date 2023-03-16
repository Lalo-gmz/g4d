package mx.lania.g4d.repository;

import java.util.List;
import java.util.Optional;
import mx.lania.g4d.domain.ParticipacionProyecto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ParticipacionProyecto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParticipacionProyectoRepository extends JpaRepository<ParticipacionProyecto, Long> {
    @Query(
        "select participacionProyecto from ParticipacionProyecto participacionProyecto where participacionProyecto.usuario.login = ?#{principal.username}"
    )
    List<ParticipacionProyecto> findByUsuarioIsCurrentUser();

    Optional<List<ParticipacionProyecto>> findAllByProyectoId(Long id);
}
