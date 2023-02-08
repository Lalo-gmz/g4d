package mx.lania.mca.g4d.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link mx.lania.mca.g4d.domain.Usuario} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private String idGitLab;

    private String tokenIdentificacion;

    private FuncionalidadDTO funcionalidad;

    private ProyectoDTO proyecto;

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

    public String getIdGitLab() {
        return idGitLab;
    }

    public void setIdGitLab(String idGitLab) {
        this.idGitLab = idGitLab;
    }

    public String getTokenIdentificacion() {
        return tokenIdentificacion;
    }

    public void setTokenIdentificacion(String tokenIdentificacion) {
        this.tokenIdentificacion = tokenIdentificacion;
    }

    public FuncionalidadDTO getFuncionalidad() {
        return funcionalidad;
    }

    public void setFuncionalidad(FuncionalidadDTO funcionalidad) {
        this.funcionalidad = funcionalidad;
    }

    public ProyectoDTO getProyecto() {
        return proyecto;
    }

    public void setProyecto(ProyectoDTO proyecto) {
        this.proyecto = proyecto;
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
        if (!(o instanceof UsuarioDTO)) {
            return false;
        }

        UsuarioDTO usuarioDTO = (UsuarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, usuarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", idGitLab='" + getIdGitLab() + "'" +
            ", tokenIdentificacion='" + getTokenIdentificacion() + "'" +
            ", funcionalidad=" + getFuncionalidad() +
            ", proyecto=" + getProyecto() +
            ", rol=" + getRol() +
            "}";
    }
}
