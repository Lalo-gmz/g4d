package mx.lania.g4d.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IteracionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Iteracion.class);
        Iteracion iteracion1 = new Iteracion();
        iteracion1.setId(1L);
        Iteracion iteracion2 = new Iteracion();
        iteracion2.setId(iteracion1.getId());
        assertThat(iteracion1).isEqualTo(iteracion2);
        iteracion2.setId(2L);
        assertThat(iteracion1).isNotEqualTo(iteracion2);
        iteracion1.setId(null);
        assertThat(iteracion1).isNotEqualTo(iteracion2);
    }
}
