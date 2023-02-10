package mx.lania.g4d.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FuncionalidadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Funcionalidad.class);
        Funcionalidad funcionalidad1 = new Funcionalidad();
        funcionalidad1.setId(1L);
        Funcionalidad funcionalidad2 = new Funcionalidad();
        funcionalidad2.setId(funcionalidad1.getId());
        assertThat(funcionalidad1).isEqualTo(funcionalidad2);
        funcionalidad2.setId(2L);
        assertThat(funcionalidad1).isNotEqualTo(funcionalidad2);
        funcionalidad1.setId(null);
        assertThat(funcionalidad1).isNotEqualTo(funcionalidad2);
    }
}
