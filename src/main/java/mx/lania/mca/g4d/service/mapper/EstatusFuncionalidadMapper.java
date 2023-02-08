package mx.lania.mca.g4d.service.mapper;

import mx.lania.mca.g4d.domain.EstatusFuncionalidad;
import mx.lania.mca.g4d.service.dto.EstatusFuncionalidadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EstatusFuncionalidad} and its DTO {@link EstatusFuncionalidadDTO}.
 */
@Mapper(componentModel = "spring")
public interface EstatusFuncionalidadMapper extends EntityMapper<EstatusFuncionalidadDTO, EstatusFuncionalidad> {}
