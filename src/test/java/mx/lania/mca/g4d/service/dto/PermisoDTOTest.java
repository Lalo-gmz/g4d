package mx.lania.mca.g4d.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.mca.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PermisoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PermisoDTO.class);
        PermisoDTO permisoDTO1 = new PermisoDTO();
        permisoDTO1.setId(1L);
        PermisoDTO permisoDTO2 = new PermisoDTO();
        assertThat(permisoDTO1).isNotEqualTo(permisoDTO2);
        permisoDTO2.setId(permisoDTO1.getId());
        assertThat(permisoDTO1).isEqualTo(permisoDTO2);
        permisoDTO2.setId(2L);
        assertThat(permisoDTO1).isNotEqualTo(permisoDTO2);
        permisoDTO1.setId(null);
        assertThat(permisoDTO1).isNotEqualTo(permisoDTO2);
    }
}
