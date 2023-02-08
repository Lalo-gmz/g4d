package mx.lania.mca.g4d.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link mx.lania.mca.g4d.domain.Bitacora} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BitacoraDTO implements Serializable {

    private Long id;

    @NotNull
    private String tabla;

    @NotNull
    private String accion;

    @NotNull
    private Instant creado;

    private UsuarioDTO usuario;

    private ProyectoDTO proyecto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Instant getCreado() {
        return creado;
    }

    public void setCreado(Instant creado) {
        this.creado = creado;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
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
        if (!(o instanceof BitacoraDTO)) {
            return false;
        }

        BitacoraDTO bitacoraDTO = (BitacoraDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bitacoraDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BitacoraDTO{" +
            "id=" + getId() +
            ", tabla='" + getTabla() + "'" +
            ", accion='" + getAccion() + "'" +
            ", creado='" + getCreado() + "'" +
            ", usuario=" + getUsuario() +
            ", proyecto=" + getProyecto() +
            "}";
    }
}
