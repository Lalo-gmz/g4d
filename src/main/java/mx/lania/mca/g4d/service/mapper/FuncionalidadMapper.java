package mx.lania.mca.g4d.service.mapper;

import mx.lania.mca.g4d.domain.EstatusFuncionalidad;
import mx.lania.mca.g4d.domain.Funcionalidad;
import mx.lania.mca.g4d.domain.Iteracion;
import mx.lania.mca.g4d.service.dto.EstatusFuncionalidadDTO;
import mx.lania.mca.g4d.service.dto.FuncionalidadDTO;
import mx.lania.mca.g4d.service.dto.IteracionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Funcionalidad} and its DTO {@link FuncionalidadDTO}.
 */
@Mapper(componentModel = "spring")
public interface FuncionalidadMapper extends EntityMapper<FuncionalidadDTO, Funcionalidad> {
    @Mapping(target = "estatusFuncionalidad", source = "estatusFuncionalidad", qualifiedByName = "estatusFuncionalidadId")
    @Mapping(target = "iteracion", source = "iteracion", qualifiedByName = "iteracionId")
    FuncionalidadDTO toDto(Funcionalidad s);

    @Named("estatusFuncionalidadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EstatusFuncionalidadDTO toDtoEstatusFuncionalidadId(EstatusFuncionalidad estatusFuncionalidad);

    @Named("iteracionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IteracionDTO toDtoIteracionId(Iteracion iteracion);
}
