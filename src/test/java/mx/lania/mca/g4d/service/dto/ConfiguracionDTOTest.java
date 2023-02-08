package mx.lania.mca.g4d.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.mca.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfiguracionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfiguracionDTO.class);
        ConfiguracionDTO configuracionDTO1 = new ConfiguracionDTO();
        configuracionDTO1.setId(1L);
        ConfiguracionDTO configuracionDTO2 = new ConfiguracionDTO();
        assertThat(configuracionDTO1).isNotEqualTo(configuracionDTO2);
        configuracionDTO2.setId(configuracionDTO1.getId());
        assertThat(configuracionDTO1).isEqualTo(configuracionDTO2);
        configuracionDTO2.setId(2L);
        assertThat(configuracionDTO1).isNotEqualTo(configuracionDTO2);
        configuracionDTO1.setId(null);
        assertThat(configuracionDTO1).isNotEqualTo(configuracionDTO2);
    }
}
