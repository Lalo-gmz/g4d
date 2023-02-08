package mx.lania.mca.g4d.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.mca.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PermisoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Permiso.class);
        Permiso permiso1 = new Permiso();
        permiso1.setId(1L);
        Permiso permiso2 = new Permiso();
        permiso2.setId(permiso1.getId());
        assertThat(permiso1).isEqualTo(permiso2);
        permiso2.setId(2L);
        assertThat(permiso1).isNotEqualTo(permiso2);
        permiso1.setId(null);
        assertThat(permiso1).isNotEqualTo(permiso2);
    }
}
