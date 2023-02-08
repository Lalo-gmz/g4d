package mx.lania.mca.g4d.service.mapper;

import mx.lania.mca.g4d.domain.Etiqueta;
import mx.lania.mca.g4d.domain.Funcionalidad;
import mx.lania.mca.g4d.service.dto.EtiquetaDTO;
import mx.lania.mca.g4d.service.dto.FuncionalidadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Etiqueta} and its DTO {@link EtiquetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface EtiquetaMapper extends EntityMapper<EtiquetaDTO, Etiqueta> {
    @Mapping(target = "funcionalidad", source = "funcionalidad", qualifiedByName = "funcionalidadId")
    EtiquetaDTO toDto(Etiqueta s);

    @Named("funcionalidadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FuncionalidadDTO toDtoFuncionalidadId(Funcionalidad funcionalidad);
}
