package mx.lania.g4d.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link mx.lania.g4d.domain.Atributo} entity. This class is used
 * in {@link mx.lania.g4d.web.rest.AtributoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /atributos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AtributoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private LongFilter atributoFuncionalidadId;

    private Boolean distinct;

    public AtributoCriteria() {}

    public AtributoCriteria(AtributoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.atributoFuncionalidadId = other.atributoFuncionalidadId == null ? null : other.atributoFuncionalidadId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AtributoCriteria copy() {
        return new AtributoCriteria(this);
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

    public LongFilter getAtributoFuncionalidadId() {
        return atributoFuncionalidadId;
    }

    public LongFilter atributoFuncionalidadId() {
        if (atributoFuncionalidadId == null) {
            atributoFuncionalidadId = new LongFilter();
        }
        return atributoFuncionalidadId;
    }

    public void setAtributoFuncionalidadId(LongFilter atributoFuncionalidadId) {
        this.atributoFuncionalidadId = atributoFuncionalidadId;
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
        final AtributoCriteria that = (AtributoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(atributoFuncionalidadId, that.atributoFuncionalidadId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, atributoFuncionalidadId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AtributoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (atributoFuncionalidadId != null ? "atributoFuncionalidadId=" + atributoFuncionalidadId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
