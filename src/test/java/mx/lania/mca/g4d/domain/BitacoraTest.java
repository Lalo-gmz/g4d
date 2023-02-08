package mx.lania.mca.g4d.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.mca.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BitacoraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bitacora.class);
        Bitacora bitacora1 = new Bitacora();
        bitacora1.setId(1L);
        Bitacora bitacora2 = new Bitacora();
        bitacora2.setId(bitacora1.getId());
        assertThat(bitacora1).isEqualTo(bitacora2);
        bitacora2.setId(2L);
        assertThat(bitacora1).isNotEqualTo(bitacora2);
        bitacora1.setId(null);
        assertThat(bitacora1).isNotEqualTo(bitacora2);
    }
}
