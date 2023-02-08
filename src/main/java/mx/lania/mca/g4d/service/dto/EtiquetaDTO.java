package mx.lania.mca.g4d.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link mx.lania.mca.g4d.domain.Etiqueta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EtiquetaDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    private Integer prioridad;

    private FuncionalidadDTO funcionalidad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public FuncionalidadDTO getFuncionalidad() {
        return funcionalidad;
    }

    public void setFuncionalidad(FuncionalidadDTO funcionalidad) {
        this.funcionalidad = funcionalidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EtiquetaDTO)) {
            return false;
        }

        EtiquetaDTO etiquetaDTO = (EtiquetaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, etiquetaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EtiquetaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", prioridad=" + getPrioridad() +
            ", funcionalidad=" + getFuncionalidad() +
            "}";
    }
}
