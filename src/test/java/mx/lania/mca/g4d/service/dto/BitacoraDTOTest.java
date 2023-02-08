package mx.lania.mca.g4d.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.mca.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BitacoraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BitacoraDTO.class);
        BitacoraDTO bitacoraDTO1 = new BitacoraDTO();
        bitacoraDTO1.setId(1L);
        BitacoraDTO bitacoraDTO2 = new BitacoraDTO();
        assertThat(bitacoraDTO1).isNotEqualTo(bitacoraDTO2);
        bitacoraDTO2.setId(bitacoraDTO1.getId());
        assertThat(bitacoraDTO1).isEqualTo(bitacoraDTO2);
        bitacoraDTO2.setId(2L);
        assertThat(bitacoraDTO1).isNotEqualTo(bitacoraDTO2);
        bitacoraDTO1.setId(null);
        assertThat(bitacoraDTO1).isNotEqualTo(bitacoraDTO2);
    }
}
