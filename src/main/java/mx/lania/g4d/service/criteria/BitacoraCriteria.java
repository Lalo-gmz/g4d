package mx.lania.g4d.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link mx.lania.g4d.domain.Bitacora} entity. This class is used
 * in {@link mx.lania.g4d.web.rest.BitacoraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bitacoras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BitacoraCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tabla;

    private StringFilter accion;

    private InstantFilter creado;

    private LongFilter userId;

    private LongFilter proyectoId;

    private Boolean distinct;

    public BitacoraCriteria() {}

    public BitacoraCriteria(BitacoraCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tabla = other.tabla == null ? null : other.tabla.copy();
        this.accion = other.accion == null ? null : other.accion.copy();
        this.creado = other.creado == null ? null : other.creado.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.proyectoId = other.proyectoId == null ? null : other.proyectoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BitacoraCriteria copy() {
        return new BitacoraCriteria(this);
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

    public StringFilter getTabla() {
        return tabla;
    }

    public StringFilter tabla() {
        if (tabla == null) {
            tabla = new StringFilter();
        }
        return tabla;
    }

    public void setTabla(StringFilter tabla) {
        this.tabla = tabla;
    }

    public StringFilter getAccion() {
        return accion;
    }

    public StringFilter accion() {
        if (accion == null) {
            accion = new StringFilter();
        }
        return accion;
    }

    public void setAccion(StringFilter accion) {
        this.accion = accion;
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
        final BitacoraCriteria that = (BitacoraCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tabla, that.tabla) &&
            Objects.equals(accion, that.accion) &&
            Objects.equals(creado, that.creado) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(proyectoId, that.proyectoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tabla, accion, creado, userId, proyectoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BitacoraCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tabla != null ? "tabla=" + tabla + ", " : "") +
            (accion != null ? "accion=" + accion + ", " : "") +
            (creado != null ? "creado=" + creado + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (proyectoId != null ? "proyectoId=" + proyectoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
