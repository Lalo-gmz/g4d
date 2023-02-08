package mx.lania.mca.g4d.service.mapper;

import mx.lania.mca.g4d.domain.Comentario;
import mx.lania.mca.g4d.domain.Funcionalidad;
import mx.lania.mca.g4d.domain.Usuario;
import mx.lania.mca.g4d.service.dto.ComentarioDTO;
import mx.lania.mca.g4d.service.dto.FuncionalidadDTO;
import mx.lania.mca.g4d.service.dto.UsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comentario} and its DTO {@link ComentarioDTO}.
 */
@Mapper(componentModel = "spring")
public interface ComentarioMapper extends EntityMapper<ComentarioDTO, Comentario> {
    @Mapping(target = "funcionalidad", source = "funcionalidad", qualifiedByName = "funcionalidadId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "usuarioId")
    ComentarioDTO toDto(Comentario s);

    @Named("funcionalidadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FuncionalidadDTO toDtoFuncionalidadId(Funcionalidad funcionalidad);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);
}
