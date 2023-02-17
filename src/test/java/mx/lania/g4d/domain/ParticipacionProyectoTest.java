package mx.lania.g4d.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParticipacionProyectoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParticipacionProyecto.class);
        ParticipacionProyecto participacionProyecto1 = new ParticipacionProyecto();
        participacionProyecto1.setId(1L);
        ParticipacionProyecto participacionProyecto2 = new ParticipacionProyecto();
        participacionProyecto2.setId(participacionProyecto1.getId());
        assertThat(participacionProyecto1).isEqualTo(participacionProyecto2);
        participacionProyecto2.setId(2L);
        assertThat(participacionProyecto1).isNotEqualTo(participacionProyecto2);
        participacionProyecto1.setId(null);
        assertThat(participacionProyecto1).isNotEqualTo(participacionProyecto2);
    }
}
