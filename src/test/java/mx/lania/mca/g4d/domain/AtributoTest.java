package mx.lania.mca.g4d.domain;

import static org.assertj.core.api.Assertions.assertThat;

import mx.lania.mca.g4d.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AtributoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Atributo.class);
        Atributo atributo1 = new Atributo();
        atributo1.setId(1L);
        Atributo atributo2 = new Atributo();
        atributo2.setId(atributo1.getId());
        assertThat(atributo1).isEqualTo(atributo2);
        atributo2.setId(2L);
        assertThat(atributo1).isNotEqualTo(atributo2);
        atributo1.setId(null);
        assertThat(atributo1).isNotEqualTo(atributo2);
    }
}
