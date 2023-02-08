package mx.lania.mca.g4d.service.mapper;

import mx.lania.mca.g4d.domain.Configuracion;
import mx.lania.mca.g4d.domain.Proyecto;
import mx.lania.mca.g4d.service.dto.ConfiguracionDTO;
import mx.lania.mca.g4d.service.dto.ProyectoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Configuracion} and its DTO {@link ConfiguracionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConfiguracionMapper extends EntityMapper<ConfiguracionDTO, Configuracion> {
    @Mapping(target = "proyecto", source = "proyecto", qualifiedByName = "proyectoId")
    ConfiguracionDTO toDto(Configuracion s);

    @Named("proyectoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProyectoDTO toDtoProyectoId(Proyecto proyecto);
}
