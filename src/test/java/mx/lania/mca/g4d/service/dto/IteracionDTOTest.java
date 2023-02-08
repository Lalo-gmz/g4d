package mx.lania.mca.g4d.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.mca.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IteracionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IteracionDTO.class);
        IteracionDTO iteracionDTO1 = new IteracionDTO();
        iteracionDTO1.setId(1L);
        IteracionDTO iteracionDTO2 = new IteracionDTO();
        assertThat(iteracionDTO1).isNotEqualTo(iteracionDTO2);
        iteracionDTO2.setId(iteracionDTO1.getId());
        assertThat(iteracionDTO1).isEqualTo(iteracionDTO2);
        iteracionDTO2.setId(2L);
        assertThat(iteracionDTO1).isNotEqualTo(iteracionDTO2);
        iteracionDTO1.setId(null);
        assertThat(iteracionDTO1).isNotEqualTo(iteracionDTO2);
    }
}
