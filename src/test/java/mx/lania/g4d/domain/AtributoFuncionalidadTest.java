package mx.lania.g4d.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AtributoFuncionalidadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AtributoFuncionalidad.class);
        AtributoFuncionalidad atributoFuncionalidad1 = new AtributoFuncionalidad();
        atributoFuncionalidad1.setId(1L);
        AtributoFuncionalidad atributoFuncionalidad2 = new AtributoFuncionalidad();
        atributoFuncionalidad2.setId(atributoFuncionalidad1.getId());
        assertThat(atributoFuncionalidad1).isEqualTo(atributoFuncionalidad2);
        atributoFuncionalidad2.setId(2L);
        assertThat(atributoFuncionalidad1).isNotEqualTo(atributoFuncionalidad2);
        atributoFuncionalidad1.setId(null);
        assertThat(atributoFuncionalidad1).isNotEqualTo(atributoFuncionalidad2);
    }
}
