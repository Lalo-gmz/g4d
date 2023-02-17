package mx.lania.g4d.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrioridadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prioridad.class);
        Prioridad prioridad1 = new Prioridad();
        prioridad1.setId(1L);
        Prioridad prioridad2 = new Prioridad();
        prioridad2.setId(prioridad1.getId());
        assertThat(prioridad1).isEqualTo(prioridad2);
        prioridad2.setId(2L);
        assertThat(prioridad1).isNotEqualTo(prioridad2);
        prioridad1.setId(null);
        assertThat(prioridad1).isNotEqualTo(prioridad2);
    }
}
