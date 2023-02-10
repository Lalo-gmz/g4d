package mx.lania.g4d.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link mx.lania.g4d.domain.Usuario} entity. This class is used
 * in {@link mx.lania.g4d.web.rest.UsuarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /usuarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter idGitLab;

    private StringFilter tokenIdentificacion;

    private LongFilter funcionalidadId;

    private LongFilter proyectoId;

    private LongFilter rolId;

    private LongFilter bitacoraId;

    private LongFilter comentarioId;

    private Boolean distinct;

    public UsuarioCriteria() {}

    public UsuarioCriteria(UsuarioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.idGitLab = other.idGitLab == null ? null : other.idGitLab.copy();
        this.tokenIdentificacion = other.tokenIdentificacion == null ? null : other.tokenIdentificacion.copy();
        this.funcionalidadId = other.funcionalidadId == null ? null : other.funcionalidadId.copy();
        this.proyectoId = other.proyectoId == null ? null : other.proyectoId.copy();
        this.rolId = other.rolId == null ? null : other.rolId.copy();
        this.bitacoraId = other.bitacoraId == null ? null : other.bitacoraId.copy();
        this.comentarioId = other.comentarioId == null ? null : other.comentarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UsuarioCriteria copy() {
        return new UsuarioCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getIdGitLab() {
        return idGitLab;
    }

    public StringFilter idGitLab() {
        if (idGitLab == null) {
            idGitLab = new StringFilter();
        }
        return idGitLab;
    }

    public void setIdGitLab(StringFilter idGitLab) {
        this.idGitLab = idGitLab;
    }

    public StringFilter getTokenIdentificacion() {
        return tokenIdentificacion;
    }

    public StringFilter tokenIdentificacion() {
        if (tokenIdentificacion == null) {
            tokenIdentificacion = new StringFilter();
        }
        return tokenIdentificacion;
    }

    public void setTokenIdentificacion(StringFilter tokenIdentificacion) {
        this.tokenIdentificacion = tokenIdentificacion;
    }

    public LongFilter getFuncionalidadId() {
        return funcionalidadId;
    }

    public LongFilter funcionalidadId() {
        if (funcionalidadId == null) {
            funcionalidadId = new LongFilter();
        }
        return funcionalidadId;
    }

    public void setFuncionalidadId(LongFilter funcionalidadId) {
        this.funcionalidadId = funcionalidadId;
    }

    public LongFilter getProyectoId() {
        return proyectoId;
    }

    public LongFilter proyectoId() {
        if (proyectoId == null) {
            proyectoId = new LongFilter();
        }
        return proyectoId;
    }

    public void setProyectoId(LongFilter proyectoId) {
        this.proyectoId = proyectoId;
    }

    public LongFilter getRolId() {
        return rolId;
    }

    public LongFilter rolId() {
        if (rolId == null) {
            rolId = new LongFilter();
        }
        return rolId;
    }

    public void setRolId(LongFilter rolId) {
        this.rolId = rolId;
    }

    public LongFilter getBitacoraId() {
        return bitacoraId;
    }

    public LongFilter bitacoraId() {
        if (bitacoraId == null) {
            bitacoraId = new LongFilter();
        }
        return bitacoraId;
    }

    public void setBitacoraId(LongFilter bitacoraId) {
        this.bitacoraId = bitacoraId;
    }

    public LongFilter getComentarioId() {
        return comentarioId;
    }

    public LongFilter comentarioId() {
        if (comentarioId == null) {
            comentarioId = new LongFilter();
        }
        return comentarioId;
    }

    public void setComentarioId(LongFilter comentarioId) {
        this.comentarioId = comentarioId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UsuarioCriteria that = (UsuarioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(idGitLab, that.idGitLab) &&
            Objects.equals(tokenIdentificacion, that.tokenIdentificacion) &&
            Objects.equals(funcionalidadId, that.funcionalidadId) &&
            Objects.equals(proyectoId, that.proyectoId) &&
            Objects.equals(rolId, that.rolId) &&
            Objects.equals(bitacoraId, that.bitacoraId) &&
            Objects.equals(comentarioId, that.comentarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            idGitLab,
            tokenIdentificacion,
            funcionalidadId,
            proyectoId,
            rolId,
            bitacoraId,
            comentarioId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (idGitLab != null ? "idGitLab=" + idGitLab + ", " : "") +
            (tokenIdentificacion != null ? "tokenIdentificacion=" + tokenIdentificacion + ", " : "") +
            (funcionalidadId != null ? "funcionalidadId=" + funcionalidadId + ", " : "") +
            (proyectoId != null ? "proyectoId=" + proyectoId + ", " : "") +
            (rolId != null ? "rolId=" + rolId + ", " : "") +
            (bitacoraId != null ? "bitacoraId=" + bitacoraId + ", " : "") +
            (comentarioId != null ? "comentarioId=" + comentarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
