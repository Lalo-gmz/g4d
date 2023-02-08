package mx.lania.mca.g4d.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link mx.lania.mca.g4d.domain.Rol} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RolDTO implements Serializable {

    private Long id;

    private String nombre;

    private ProyectoDTO proyecto;

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

    public ProyectoDTO getProyecto() {
        return proyecto;
    }

    public void setProyecto(ProyectoDTO proyecto) {
        this.proyecto = proyecto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RolDTO)) {
            return false;
        }

        RolDTO rolDTO = (RolDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rolDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", proyecto=" + getProyecto() +
            "}";
    }
}
