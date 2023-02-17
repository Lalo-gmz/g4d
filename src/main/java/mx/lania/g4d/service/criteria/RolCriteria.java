package mx.lania.g4d.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link mx.lania.g4d.domain.Rol} entity. This class is used
 * in {@link mx.lania.g4d.web.rest.RolResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rols?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RolCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private LongFilter participacionProyectoId;

    private Boolean distinct;

    public RolCriteria() {}

    public RolCriteria(RolCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.participacionProyectoId = other.participacionProyectoId == null ? null : other.participacionProyectoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RolCriteria copy() {
        return new RolCriteria(this);
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

    public LongFilter getParticipacionProyectoId() {
        return participacionProyectoId;
    }

    public LongFilter participacionProyectoId() {
        if (participacionProyectoId == null) {
            participacionProyectoId = new LongFilter();
        }
        return participacionProyectoId;
    }

    public void setParticipacionProyectoId(LongFilter participacionProyectoId) {
        this.participacionProyectoId = participacionProyectoId;
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
        final RolCriteria that = (RolCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(participacionProyectoId, that.participacionProyectoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, participacionProyectoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (participacionProyectoId != null ? "participacionProyectoId=" + participacionProyectoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
