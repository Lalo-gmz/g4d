package mx.lania.mca.g4d.service.mapper;

import mx.lania.mca.g4d.domain.Iteracion;
import mx.lania.mca.g4d.domain.Proyecto;
import mx.lania.mca.g4d.service.dto.IteracionDTO;
import mx.lania.mca.g4d.service.dto.ProyectoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Iteracion} and its DTO {@link IteracionDTO}.
 */
@Mapper(componentModel = "spring")
public interface IteracionMapper extends EntityMapper<IteracionDTO, Iteracion> {
    @Mapping(target = "proyecto", source = "proyecto", qualifiedByName = "proyectoId")
    IteracionDTO toDto(Iteracion s);

    @Named("proyectoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProyectoDTO toDtoProyectoId(Proyecto proyecto);
}
