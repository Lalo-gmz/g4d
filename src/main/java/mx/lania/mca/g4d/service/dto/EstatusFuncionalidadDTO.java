package mx.lania.mca.g4d.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link mx.lania.mca.g4d.domain.EstatusFuncionalidad} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EstatusFuncionalidadDTO implements Serializable {

    private Long id;

    private String nombre;

    private Integer prioridad;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EstatusFuncionalidadDTO)) {
            return false;
        }

        EstatusFuncionalidadDTO estatusFuncionalidadDTO = (EstatusFuncionalidadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, estatusFuncionalidadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EstatusFuncionalidadDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", prioridad=" + getPrioridad() +
            "}";
    }
}
