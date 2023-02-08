package mx.lania.mca.g4d.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.mca.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfiguracionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Configuracion.class);
        Configuracion configuracion1 = new Configuracion();
        configuracion1.setId(1L);
        Configuracion configuracion2 = new Configuracion();
        configuracion2.setId(configuracion1.getId());
        assertThat(configuracion1).isEqualTo(configuracion2);
        configuracion2.setId(2L);
        assertThat(configuracion1).isNotEqualTo(configuracion2);
        configuracion1.setId(null);
        assertThat(configuracion1).isNotEqualTo(configuracion2);
    }
}
