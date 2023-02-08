package mx.lania.mca.g4d.service.mapper;

import mx.lania.mca.g4d.domain.Bitacora;
import mx.lania.mca.g4d.domain.Proyecto;
import mx.lania.mca.g4d.domain.Usuario;
import mx.lania.mca.g4d.service.dto.BitacoraDTO;
import mx.lania.mca.g4d.service.dto.ProyectoDTO;
import mx.lania.mca.g4d.service.dto.UsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bitacora} and its DTO {@link BitacoraDTO}.
 */
@Mapper(componentModel = "spring")
public interface BitacoraMapper extends EntityMapper<BitacoraDTO, Bitacora> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "usuarioId")
    @Mapping(target = "proyecto", source = "proyecto", qualifiedByName = "proyectoId")
    BitacoraDTO toDto(Bitacora s);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);

    @Named("proyectoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProyectoDTO toDtoProyectoId(Proyecto proyecto);
}
