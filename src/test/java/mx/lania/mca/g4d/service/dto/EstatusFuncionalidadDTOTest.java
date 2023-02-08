package mx.lania.mca.g4d.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.mca.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstatusFuncionalidadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstatusFuncionalidadDTO.class);
        EstatusFuncionalidadDTO estatusFuncionalidadDTO1 = new EstatusFuncionalidadDTO();
        estatusFuncionalidadDTO1.setId(1L);
        EstatusFuncionalidadDTO estatusFuncionalidadDTO2 = new EstatusFuncionalidadDTO();
        assertThat(estatusFuncionalidadDTO1).isNotEqualTo(estatusFuncionalidadDTO2);
        estatusFuncionalidadDTO2.setId(estatusFuncionalidadDTO1.getId());
        assertThat(estatusFuncionalidadDTO1).isEqualTo(estatusFuncionalidadDTO2);
        estatusFuncionalidadDTO2.setId(2L);
        assertThat(estatusFuncionalidadDTO1).isNotEqualTo(estatusFuncionalidadDTO2);
        estatusFuncionalidadDTO1.setId(null);
        assertThat(estatusFuncionalidadDTO1).isNotEqualTo(estatusFuncionalidadDTO2);
    }
}
