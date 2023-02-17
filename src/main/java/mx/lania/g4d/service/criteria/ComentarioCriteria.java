package mx.lania.g4d.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link mx.lania.g4d.domain.Comentario} entity. This class is used
 * in {@link mx.lania.g4d.web.rest.ComentarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /comentarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ComentarioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter mensaje;

    private InstantFilter creado;

    private InstantFilter modificado;

    private LongFilter funcionalidadId;

    private LongFilter userId;

    private Boolean distinct;

    public ComentarioCriteria() {}

    public ComentarioCriteria(ComentarioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.mensaje = other.mensaje == null ? null : other.mensaje.copy();
        this.creado = other.creado == null ? null : other.creado.copy();
        this.modificado = other.modificado == null ? null : other.modificado.copy();
        this.funcionalidadId = other.funcionalidadId == null ? null : other.funcionalidadId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ComentarioCriteria copy() {
        return new ComentarioCriteria(this);
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

    public StringFilter getMensaje() {
        return mensaje;
    }

    public StringFilter mensaje() {
        if (mensaje == null) {
            mensaje = new StringFilter();
        }
        return mensaje;
    }

    public void setMensaje(StringFilter mensaje) {
        this.mensaje = mensaje;
    }

    public InstantFilter getCreado() {
        return creado;
    }

    public InstantFilter creado() {
        if (creado == null) {
            creado = new InstantFilter();
        }
        return creado;
    }

    public void setCreado(InstantFilter creado) {
        this.creado = creado;
    }

    public InstantFilter getModificado() {
        return modificado;
    }

    public InstantFilter modificado() {
        if (modificado == null) {
            modificado = new InstantFilter();
        }
        return modificado;
    }

    public void setModificado(InstantFilter modificado) {
        this.modificado = modificado;
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

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final ComentarioCriteria that = (ComentarioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(mensaje, that.mensaje) &&
            Objects.equals(creado, that.creado) &&
            Objects.equals(modificado, that.modificado) &&
            Objects.equals(funcionalidadId, that.funcionalidadId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mensaje, creado, modificado, funcionalidadId, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComentarioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (mensaje != null ? "mensaje=" + mensaje + ", " : "") +
            (creado != null ? "creado=" + creado + ", " : "") +
            (modificado != null ? "modificado=" + modificado + ", " : "") +
            (funcionalidadId != null ? "funcionalidadId=" + funcionalidadId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
