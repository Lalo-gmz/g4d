package mx.lania.mca.g4d.service.mapper;

import mx.lania.mca.g4d.domain.Permiso;
import mx.lania.mca.g4d.domain.Rol;
import mx.lania.mca.g4d.service.dto.PermisoDTO;
import mx.lania.mca.g4d.service.dto.RolDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Permiso} and its DTO {@link PermisoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PermisoMapper extends EntityMapper<PermisoDTO, Permiso> {
    @Mapping(target = "rol", source = "rol", qualifiedByName = "rolId")
    PermisoDTO toDto(Permiso s);

    @Named("rolId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RolDTO toDtoRolId(Rol rol);
}
