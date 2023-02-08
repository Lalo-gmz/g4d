package mx.lania.mca.g4d.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link mx.lania.mca.g4d.domain.Comentario} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ComentarioDTO implements Serializable {

    private Long id;

    @NotNull
    private String mensaje;

    private Instant creado;

    private Instant modificado;

    private FuncionalidadDTO funcionalidad;

    private UsuarioDTO usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Instant getCreado() {
        return creado;
    }

    public void setCreado(Instant creado) {
        this.creado = creado;
    }

    public Instant getModificado() {
        return modificado;
    }

    public void setModificado(Instant modificado) {
        this.modificado = modificado;
    }

    public FuncionalidadDTO getFuncionalidad() {
        return funcionalidad;
    }

    public void setFuncionalidad(FuncionalidadDTO funcionalidad) {
        this.funcionalidad = funcionalidad;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComentarioDTO)) {
            return false;
        }

        ComentarioDTO comentarioDTO = (ComentarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, comentarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComentarioDTO{" +
            "id=" + getId() +
            ", mensaje='" + getMensaje() + "'" +
            ", creado='" + getCreado() + "'" +
            ", modificado='" + getModificado() + "'" +
            ", funcionalidad=" + getFuncionalidad() +
            ", usuario=" + getUsuario() +
            "}";
    }
}
