package mx.lania.mca.g4d.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FuncionalidadMapperTest {

    private FuncionalidadMapper funcionalidadMapper;

    @BeforeEach
    public void setUp() {
        funcionalidadMapper = new FuncionalidadMapperImpl();
    }
}
