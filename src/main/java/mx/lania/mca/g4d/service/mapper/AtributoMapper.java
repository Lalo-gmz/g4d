package mx.lania.mca.g4d.service.mapper;

import mx.lania.mca.g4d.domain.Atributo;
import mx.lania.mca.g4d.domain.Funcionalidad;
import mx.lania.mca.g4d.service.dto.AtributoDTO;
import mx.lania.mca.g4d.service.dto.FuncionalidadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Atributo} and its DTO {@link AtributoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AtributoMapper extends EntityMapper<AtributoDTO, Atributo> {
    @Mapping(target = "funcionalidad", source = "funcionalidad", qualifiedByName = "funcionalidadId")
    AtributoDTO toDto(Atributo s);

    @Named("funcionalidadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FuncionalidadDTO toDtoFuncionalidadId(Funcionalidad funcionalidad);
}
