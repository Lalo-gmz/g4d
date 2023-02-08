package mx.lania.mca.g4d.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.mca.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AtributoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AtributoDTO.class);
        AtributoDTO atributoDTO1 = new AtributoDTO();
        atributoDTO1.setId(1L);
        AtributoDTO atributoDTO2 = new AtributoDTO();
        assertThat(atributoDTO1).isNotEqualTo(atributoDTO2);
        atributoDTO2.setId(atributoDTO1.getId());
        assertThat(atributoDTO1).isEqualTo(atributoDTO2);
        atributoDTO2.setId(2L);
        assertThat(atributoDTO1).isNotEqualTo(atributoDTO2);
        atributoDTO1.setId(null);
        assertThat(atributoDTO1).isNotEqualTo(atributoDTO2);
    }
}
