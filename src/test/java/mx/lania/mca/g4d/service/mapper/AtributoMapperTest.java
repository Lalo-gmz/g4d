package mx.lania.mca.g4d.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AtributoMapperTest {

    private AtributoMapper atributoMapper;

    @BeforeEach
    public void setUp() {
        atributoMapper = new AtributoMapperImpl();
    }
}
