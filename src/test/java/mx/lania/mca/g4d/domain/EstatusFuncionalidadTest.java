package mx.lania.mca.g4d.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.mca.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstatusFuncionalidadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstatusFuncionalidad.class);
        EstatusFuncionalidad estatusFuncionalidad1 = new EstatusFuncionalidad();
        estatusFuncionalidad1.setId(1L);
        EstatusFuncionalidad estatusFuncionalidad2 = new EstatusFuncionalidad();
        estatusFuncionalidad2.setId(estatusFuncionalidad1.getId());
        assertThat(estatusFuncionalidad1).isEqualTo(estatusFuncionalidad2);
        estatusFuncionalidad2.setId(2L);
        assertThat(estatusFuncionalidad1).isNotEqualTo(estatusFuncionalidad2);
        estatusFuncionalidad1.setId(null);
        assertThat(estatusFuncionalidad1).isNotEqualTo(estatusFuncionalidad2);
    }
}
