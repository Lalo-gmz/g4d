package mx.lania.mca.g4d.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link mx.lania.mca.g4d.domain.Proyecto} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProyectoDTO implements Serializable {

    private Long id;

    private String nombre;

    @NotNull
    private String idProyectoGitLab;

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

    public String getIdProyectoGitLab() {
        return idProyectoGitLab;
    }

    public void setIdProyectoGitLab(String idProyectoGitLab) {
        this.idProyectoGitLab = idProyectoGitLab;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProyectoDTO)) {
            return false;
        }

        ProyectoDTO proyectoDTO = (ProyectoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, proyectoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProyectoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", idProyectoGitLab='" + getIdProyectoGitLab() + "'" +
            "}";
    }
}
