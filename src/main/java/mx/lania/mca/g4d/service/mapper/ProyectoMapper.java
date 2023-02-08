package mx.lania.mca.g4d.service.mapper;

import mx.lania.mca.g4d.domain.Proyecto;
import mx.lania.mca.g4d.service.dto.ProyectoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Proyecto} and its DTO {@link ProyectoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProyectoMapper extends EntityMapper<ProyectoDTO, Proyecto> {}
