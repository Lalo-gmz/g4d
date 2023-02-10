package mx.lania.g4d.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import mx.lania.g4d.domain.enumeration.EtiquetaVisual;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link mx.lania.g4d.domain.Configuracion} entity. This class is used
 * in {@link mx.lania.g4d.web.rest.ConfiguracionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /configuracions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConfiguracionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EtiquetaVisual
     */
    public static class EtiquetaVisualFilter extends Filter<EtiquetaVisual> {

        public EtiquetaVisualFilter() {}

        public EtiquetaVisualFilter(EtiquetaVisualFilter filter) {
            super(filter);
        }

        @Override
        public EtiquetaVisualFilter copy() {
            return new EtiquetaVisualFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private EtiquetaVisualFilter clave;

    private StringFilter valor;

    private LongFilter proyectoId;

    private Boolean distinct;

    public ConfiguracionCriteria() {}

    public ConfiguracionCriteria(ConfiguracionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.clave = other.clave == null ? null : other.clave.copy();
        this.valor = other.valor == null ? null : other.valor.copy();
        this.proyectoId = other.proyectoId == null ? null : other.proyectoId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ConfiguracionCriteria copy() {
        return new ConfiguracionCriteria(this);
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

    public EtiquetaVisualFilter getClave() {
        return clave;
    }

    public EtiquetaVisualFilter clave() {
        if (clave == null) {
            clave = new EtiquetaVisualFilter();
        }
        return clave;
    }

    public void setClave(EtiquetaVisualFilter clave) {
        this.clave = clave;
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
        final ConfiguracionCriteria that = (ConfiguracionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(clave, that.clave) &&
            Objects.equals(valor, that.valor) &&
            Objects.equals(proyectoId, that.proyectoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clave, valor, proyectoId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConfiguracionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (clave != null ? "clave=" + clave + ", " : "") +
            (valor != null ? "valor=" + valor + ", " : "") +
            (proyectoId != null ? "proyectoId=" + proyectoId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
