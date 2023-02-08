package mx.lania.mca.g4d.service.mapper;

import mx.lania.mca.g4d.domain.Proyecto;
import mx.lania.mca.g4d.domain.Rol;
import mx.lania.mca.g4d.service.dto.ProyectoDTO;
import mx.lania.mca.g4d.service.dto.RolDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rol} and its DTO {@link RolDTO}.
 */
@Mapper(componentModel = "spring")
public interface RolMapper extends EntityMapper<RolDTO, Rol> {
    @Mapping(target = "proyecto", source = "proyecto", qualifiedByName = "proyectoId")
    RolDTO toDto(Rol s);

    @Named("proyectoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProyectoDTO toDtoProyectoId(Proyecto proyecto);
}
