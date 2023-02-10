package mx.lania.g4d.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link mx.lania.g4d.domain.Iteracion} entity. This class is used
 * in {@link mx.lania.g4d.web.rest.IteracionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /iteracions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IteracionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private LocalDateFilter inicio;

    private LocalDateFilter fin;

    private LongFilter funcionalidadId;

    private LongFilter proyectoId;

    private Boolean distinct;

    public IteracionCriteria() {}

    public IteracionCriteria(IteracionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.inicio = other.inicio == null ? null : other.inicio.copy();
        this.fin = other.fin == null ? null : other.fin.copy();
        this.funcionalidadId = other.funcionalidadId == null ? null : other.funcionalidadId.copy();
        this.proyectoId = other.proyectoId == null ? null : other.proyectoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public IteracionCriteria copy() {
        return new IteracionCriteria(this);
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

    public LocalDateFilter getInicio() {
        return inicio;
    }

    public LocalDateFilter inicio() {
        if (inicio == null) {
            inicio = new LocalDateFilter();
        }
        return inicio;
    }

    public void setInicio(LocalDateFilter inicio) {
        this.inicio = inicio;
    }

    public LocalDateFilter getFin() {
        return fin;
    }

    public LocalDateFilter fin() {
        if (fin == null) {
            fin = new LocalDateFilter();
        }
        return fin;
    }

    public void setFin(LocalDateFilter fin) {
        this.fin = fin;
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
        final IteracionCriteria that = (IteracionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(inicio, that.inicio) &&
            Objects.equals(fin, that.fin) &&
            Objects.equals(funcionalidadId, that.funcionalidadId) &&
            Objects.equals(proyectoId, that.proyectoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, inicio, fin, funcionalidadId, proyectoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IteracionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (inicio != null ? "inicio=" + inicio + ", " : "") +
            (fin != null ? "fin=" + fin + ", " : "") +
            (funcionalidadId != null ? "funcionalidadId=" + funcionalidadId + ", " : "") +
            (proyectoId != null ? "proyectoId=" + proyectoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
