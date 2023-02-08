package mx.lania.mca.g4d.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link mx.lania.mca.g4d.domain.Permiso} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PermisoDTO implements Serializable {

    private Long id;

    private String nombre;

    private RolDTO rol;

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

    public RolDTO getRol() {
        return rol;
    }

    public void setRol(RolDTO rol) {
        this.rol = rol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermisoDTO)) {
            return false;
        }

        PermisoDTO permisoDTO = (PermisoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, permisoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermisoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", rol=" + getRol() +
            "}";
    }
}
