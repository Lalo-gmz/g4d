package mx.lania.g4d.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link mx.lania.g4d.domain.Proyecto} entity. This class is used
 * in {@link mx.lania.g4d.web.rest.ProyectoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /proyectos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProyectoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter idProyectoGitLab;

    private LongFilter rolId;

    private LongFilter configuracionId;

    private LongFilter bitacoraId;

    private LongFilter usuarioId;

    private LongFilter iteracionId;

    private Boolean distinct;

    public ProyectoCriteria() {}

    public ProyectoCriteria(ProyectoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.idProyectoGitLab = other.idProyectoGitLab == null ? null : other.idProyectoGitLab.copy();
        this.rolId = other.rolId == null ? null : other.rolId.copy();
        this.configuracionId = other.configuracionId == null ? null : other.configuracionId.copy();
        this.bitacoraId = other.bitacoraId == null ? null : other.bitacoraId.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.iteracionId = other.iteracionId == null ? null : other.iteracionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProyectoCriteria copy() {
        return new ProyectoCriteria(this);
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

    public StringFilter getIdProyectoGitLab() {
        return idProyectoGitLab;
    }

    public StringFilter idProyectoGitLab() {
        if (idProyectoGitLab == null) {
            idProyectoGitLab = new StringFilter();
        }
        return idProyectoGitLab;
    }

    public void setIdProyectoGitLab(StringFilter idProyectoGitLab) {
        this.idProyectoGitLab = idProyectoGitLab;
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

    public LongFilter getConfiguracionId() {
        return configuracionId;
    }

    public LongFilter configuracionId() {
        if (configuracionId == null) {
            configuracionId = new LongFilter();
        }
        return configuracionId;
    }

    public void setConfiguracionId(LongFilter configuracionId) {
        this.configuracionId = configuracionId;
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
        final ProyectoCriteria that = (ProyectoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(idProyectoGitLab, that.idProyectoGitLab) &&
            Objects.equals(rolId, that.rolId) &&
            Objects.equals(configuracionId, that.configuracionId) &&
            Objects.equals(bitacoraId, that.bitacoraId) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(iteracionId, that.iteracionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, idProyectoGitLab, rolId, configuracionId, bitacoraId, usuarioId, iteracionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProyectoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (idProyectoGitLab != null ? "idProyectoGitLab=" + idProyectoGitLab + ", " : "") +
            (rolId != null ? "rolId=" + rolId + ", " : "") +
            (configuracionId != null ? "configuracionId=" + configuracionId + ", " : "") +
            (bitacoraId != null ? "bitacoraId=" + bitacoraId + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (iteracionId != null ? "iteracionId=" + iteracionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
