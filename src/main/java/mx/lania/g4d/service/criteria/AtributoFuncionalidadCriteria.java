package mx.lania.g4d.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link mx.lania.g4d.domain.AtributoFuncionalidad} entity. This class is used
 * in {@link mx.lania.g4d.web.rest.AtributoFuncionalidadResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /atributo-funcionalidads?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AtributoFuncionalidadCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter marcado;

    private StringFilter valor;

    private LongFilter funcionalidadId;

    private LongFilter atributoId;

    private Boolean distinct;

    public AtributoFuncionalidadCriteria() {}

    public AtributoFuncionalidadCriteria(AtributoFuncionalidadCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.marcado = other.marcado == null ? null : other.marcado.copy();
        this.valor = other.valor == null ? null : other.valor.copy();
        this.funcionalidadId = other.funcionalidadId == null ? null : other.funcionalidadId.copy();
        this.atributoId = other.atributoId == null ? null : other.atributoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AtributoFuncionalidadCriteria copy() {
        return new AtributoFuncionalidadCriteria(this);
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

    public BooleanFilter getMarcado() {
        return marcado;
    }

    public BooleanFilter marcado() {
        if (marcado == null) {
            marcado = new BooleanFilter();
        }
        return marcado;
    }

    public void setMarcado(BooleanFilter marcado) {
        this.marcado = marcado;
    }

    public StringFilter getValor() {
        return valor;
    }

    public StringFilter valor() {
        if (valor == null) {
            valor = new StringFilter();
        }
        return valor;
    }

    public void setValor(StringFilter valor) {
        this.valor = valor;
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

    public LongFilter getAtributoId() {
        return atributoId;
    }

    public LongFilter atributoId() {
        if (atributoId == null) {
            atributoId = new LongFilter();
        }
        return atributoId;
    }

    public void setAtributoId(LongFilter atributoId) {
        this.atributoId = atributoId;
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
        final AtributoFuncionalidadCriteria that = (AtributoFuncionalidadCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(marcado, that.marcado) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(funcionalidadId, that.funcionalidadId) &&
            Objects.equals(atributoId, that.atributoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, marcado, valor, funcionalidadId, atributoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AtributoFuncionalidadCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (marcado != null ? "marcado=" + marcado + ", " : "") +
            (valor != null ? "valor=" + valor + ", " : "") +
            (funcionalidadId != null ? "funcionalidadId=" + funcionalidadId + ", " : "") +
            (atributoId != null ? "atributoId=" + atributoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
