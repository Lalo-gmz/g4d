package mx.lania.mca.g4d.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstatusFuncionalidadMapperTest {

    private EstatusFuncionalidadMapper estatusFuncionalidadMapper;

    @BeforeEach
    public void setUp() {
        estatusFuncionalidadMapper = new EstatusFuncionalidadMapperImpl();
    }
}
