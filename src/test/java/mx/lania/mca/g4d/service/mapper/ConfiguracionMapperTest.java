package mx.lania.mca.g4d.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfiguracionMapperTest {

    private ConfiguracionMapper configuracionMapper;

    @BeforeEach
    public void setUp() {
        configuracionMapper = new ConfiguracionMapperImpl();
    }
}
