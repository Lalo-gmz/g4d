package mx.lania.mca.g4d.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.mca.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EtiquetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EtiquetaDTO.class);
        EtiquetaDTO etiquetaDTO1 = new EtiquetaDTO();
        etiquetaDTO1.setId(1L);
        EtiquetaDTO etiquetaDTO2 = new EtiquetaDTO();
        assertThat(etiquetaDTO1).isNotEqualTo(etiquetaDTO2);
        etiquetaDTO2.setId(etiquetaDTO1.getId());
        assertThat(etiquetaDTO1).isEqualTo(etiquetaDTO2);
        etiquetaDTO2.setId(2L);
        assertThat(etiquetaDTO1).isNotEqualTo(etiquetaDTO2);
        etiquetaDTO1.setId(null);
        assertThat(etiquetaDTO1).isNotEqualTo(etiquetaDTO2);
    }
}
