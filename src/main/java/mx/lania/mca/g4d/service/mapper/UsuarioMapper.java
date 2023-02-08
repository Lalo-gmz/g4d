package mx.lania.mca.g4d.service.mapper;

import mx.lania.mca.g4d.domain.Funcionalidad;
import mx.lania.mca.g4d.domain.Proyecto;
import mx.lania.mca.g4d.domain.Rol;
import mx.lania.mca.g4d.domain.Usuario;
import mx.lania.mca.g4d.service.dto.FuncionalidadDTO;
import mx.lania.mca.g4d.service.dto.ProyectoDTO;
import mx.lania.mca.g4d.service.dto.RolDTO;
import mx.lania.mca.g4d.service.dto.UsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Usuario} and its DTO {@link UsuarioDTO}.
 */
@Mapper(componentModel = "spring")
public interface UsuarioMapper extends EntityMapper<UsuarioDTO, Usuario> {
    @Mapping(target = "funcionalidad", source = "funcionalidad", qualifiedByName = "funcionalidadId")
    @Mapping(target = "proyecto", source = "proyecto", qualifiedByName = "proyectoId")
    @Mapping(target = "rol", source = "rol", qualifiedByName = "rolId")
    UsuarioDTO toDto(Usuario s);

    @Named("funcionalidadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FuncionalidadDTO toDtoFuncionalidadId(Funcionalidad funcionalidad);

    @Named("proyectoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProyectoDTO toDtoProyectoId(Proyecto proyecto);

    @Named("rolId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RolDTO toDtoRolId(Rol rol);
}
