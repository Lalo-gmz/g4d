package mx.lania.g4d.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CapturaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Captura.class);
        Captura captura1 = new Captura();
        captura1.setId(1L);
        Captura captura2 = new Captura();
        captura2.setId(captura1.getId());
        assertThat(captura1).isEqualTo(captura2);
        captura2.setId(2L);
        assertThat(captura1).isNotEqualTo(captura2);
        captura1.setId(null);
        assertThat(captura1).isNotEqualTo(captura2);
    }
}
