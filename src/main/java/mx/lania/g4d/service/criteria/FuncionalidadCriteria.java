package mx.lania.g4d.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link mx.lania.g4d.domain.Funcionalidad} entity. This class is used
 * in {@link mx.lania.g4d.web.rest.FuncionalidadResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /funcionalidads?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FuncionalidadCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter descripcion;

    private StringFilter urlGitLab;

    private LocalDateFilter fechaEntrega;

    private InstantFilter creado;

    private InstantFilter modificado;

    private LongFilter estatusFuncionalidadId;

    private LongFilter iteracionId;

    private LongFilter etiquetaId;

    private LongFilter usuarioId;

    private LongFilter atributoId;

    private LongFilter comentarioId;

    private Boolean distinct;

    public FuncionalidadCriteria() {}

    public FuncionalidadCriteria(FuncionalidadCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.urlGitLab = other.urlGitLab == null ? null : other.urlGitLab.copy();
        this.fechaEntrega = other.fechaEntrega == null ? null : other.fechaEntrega.copy();
        this.creado = other.creado == null ? null : other.creado.copy();
        this.modificado = other.modificado == null ? null : other.modificado.copy();
        this.estatusFuncionalidadId = other.estatusFuncionalidadId == null ? null : other.estatusFuncionalidadId.copy();
        this.iteracionId = other.iteracionId == null ? null : other.iteracionId.copy();
        this.etiquetaId = other.etiquetaId == null ? null : other.etiquetaId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.atributoId = other.atributoId == null ? null : other.atributoId.copy();
        this.comentarioId = other.comentarioId == null ? null : other.comentarioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FuncionalidadCriteria copy() {
        return new FuncionalidadCriteria(this);
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

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            descripcion = new StringFilter();
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public StringFilter getUrlGitLab() {
        return urlGitLab;
    }

    public StringFilter urlGitLab() {
        if (urlGitLab == null) {
            urlGitLab = new StringFilter();
        }
        return urlGitLab;
    }

    public void setUrlGitLab(StringFilter urlGitLab) {
        this.urlGitLab = urlGitLab;
    }

    public LocalDateFilter getFechaEntrega() {
        return fechaEntrega;
    }

    public LocalDateFilter fechaEntrega() {
        if (fechaEntrega == null) {
            fechaEntrega = new LocalDateFilter();
        }
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateFilter fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
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

    public LongFilter getEstatusFuncionalidadId() {
        return estatusFuncionalidadId;
    }

    public LongFilter estatusFuncionalidadId() {
        if (estatusFuncionalidadId == null) {
            estatusFuncionalidadId = new LongFilter();
        }
        return estatusFuncionalidadId;
    }

    public void setEstatusFuncionalidadId(LongFilter estatusFuncionalidadId) {
        this.estatusFuncionalidadId = estatusFuncionalidadId;
    }

    public LongFilter getIteracionId() {
        return iteracionId;
    }

    public LongFilter iteracionId() {
        if (iteracionId == null) {
            iteracionId = new LongFilter();
        }
        return iteracionId;
    }

    public void setIteracionId(LongFilter iteracionId) {
        this.iteracionId = iteracionId;
    }

    public LongFilter getEtiquetaId() {
        return etiquetaId;
    }

    public LongFilter etiquetaId() {
        if (etiquetaId == null) {
            etiquetaId = new LongFilter();
        }
        return etiquetaId;
    }

    public void setEtiquetaId(LongFilter etiquetaId) {
        this.etiquetaId = etiquetaId;
    }

    public LongFilter getUsuarioId() {
        return usuarioId;
    }

    public LongFilter usuarioId() {
        if (usuarioId == null) {
            usuarioId = new LongFilter();
        }
        return usuarioId;
    }

    public void setUsuarioId(LongFilter usuarioId) {
        this.usuarioId = usuarioId;
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
        final FuncionalidadCriteria that = (FuncionalidadCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(urlGitLab, that.urlGitLab) &&
            Objects.equals(fechaEntrega, that.fechaEntrega) &&
            Objects.equals(creado, that.creado) &&
            Objects.equals(modificado, that.modificado) &&
            Objects.equals(estatusFuncionalidadId, that.estatusFuncionalidadId) &&
            Objects.equals(iteracionId, that.iteracionId) &&
            Objects.equals(etiquetaId, that.etiquetaId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(atributoId, that.atributoId) &&
            Objects.equals(comentarioId, that.comentarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            descripcion,
            urlGitLab,
            fechaEntrega,
            creado,
            modificado,
            estatusFuncionalidadId,
            iteracionId,
            etiquetaId,
            usuarioId,
            atributoId,
            comentarioId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FuncionalidadCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (urlGitLab != null ? "urlGitLab=" + urlGitLab + ", " : "") +
            (fechaEntrega != null ? "fechaEntrega=" + fechaEntrega + ", " : "") +
            (creado != null ? "creado=" + creado + ", " : "") +
            (modificado != null ? "modificado=" + modificado + ", " : "") +
            (estatusFuncionalidadId != null ? "estatusFuncionalidadId=" + estatusFuncionalidadId + ", " : "") +
            (iteracionId != null ? "iteracionId=" + iteracionId + ", " : "") +
            (etiquetaId != null ? "etiquetaId=" + etiquetaId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (atributoId != null ? "atributoId=" + atributoId + ", " : "") +
            (comentarioId != null ? "comentarioId=" + comentarioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
