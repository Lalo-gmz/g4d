package mx.lania.mca.g4d.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EtiquetaMapperTest {

    private EtiquetaMapper etiquetaMapper;

    @BeforeEach
    public void setUp() {
        etiquetaMapper = new EtiquetaMapperImpl();
    }
}
