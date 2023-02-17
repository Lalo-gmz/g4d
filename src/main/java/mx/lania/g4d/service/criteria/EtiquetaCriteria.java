package mx.lania.g4d.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link mx.lania.g4d.domain.Etiqueta} entity. This class is used
 * in {@link mx.lania.g4d.web.rest.EtiquetaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /etiquetas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EtiquetaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter color;

    private LongFilter funcionalidadId;

    private Boolean distinct;

    public EtiquetaCriteria() {}

    public EtiquetaCriteria(EtiquetaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.color = other.color == null ? null : other.color.copy();
        this.funcionalidadId = other.funcionalidadId == null ? null : other.funcionalidadId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EtiquetaCriteria copy() {
        return new EtiquetaCriteria(this);
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

    public StringFilter getColor() {
        return color;
    }

    public StringFilter color() {
        if (color == null) {
            color = new StringFilter();
        }
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
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
        final EtiquetaCriteria that = (EtiquetaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(color, that.color) &&
            Objects.equals(funcionalidadId, that.funcionalidadId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, color, funcionalidadId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EtiquetaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (color != null ? "color=" + color + ", " : "") +
            (funcionalidadId != null ? "funcionalidadId=" + funcionalidadId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
