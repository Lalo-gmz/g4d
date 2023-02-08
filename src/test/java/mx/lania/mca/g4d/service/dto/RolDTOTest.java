package mx.lania.mca.g4d.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.mca.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RolDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RolDTO.class);
        RolDTO rolDTO1 = new RolDTO();
        rolDTO1.setId(1L);
        RolDTO rolDTO2 = new RolDTO();
        assertThat(rolDTO1).isNotEqualTo(rolDTO2);
        rolDTO2.setId(rolDTO1.getId());
        assertThat(rolDTO1).isEqualTo(rolDTO2);
        rolDTO2.setId(2L);
        assertThat(rolDTO1).isNotEqualTo(rolDTO2);
        rolDTO1.setId(null);
        assertThat(rolDTO1).isNotEqualTo(rolDTO2);
    }
}
