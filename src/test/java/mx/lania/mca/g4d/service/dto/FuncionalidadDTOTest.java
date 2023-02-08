package mx.lania.mca.g4d.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.mca.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FuncionalidadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FuncionalidadDTO.class);
        FuncionalidadDTO funcionalidadDTO1 = new FuncionalidadDTO();
        funcionalidadDTO1.setId(1L);
        FuncionalidadDTO funcionalidadDTO2 = new FuncionalidadDTO();
        assertThat(funcionalidadDTO1).isNotEqualTo(funcionalidadDTO2);
        funcionalidadDTO2.setId(funcionalidadDTO1.getId());
        assertThat(funcionalidadDTO1).isEqualTo(funcionalidadDTO2);
        funcionalidadDTO2.setId(2L);
        assertThat(funcionalidadDTO1).isNotEqualTo(funcionalidadDTO2);
        funcionalidadDTO1.setId(null);
        assertThat(funcionalidadDTO1).isNotEqualTo(funcionalidadDTO2);
    }
}
