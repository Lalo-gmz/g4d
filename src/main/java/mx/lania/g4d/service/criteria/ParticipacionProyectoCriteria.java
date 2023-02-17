package mx.lania.g4d.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link mx.lania.g4d.domain.ParticipacionProyecto} entity. This class is used
 * in {@link mx.lania.g4d.web.rest.ParticipacionProyectoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /participacion-proyectos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParticipacionProyectoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter userId;

    private LongFilter proyectoId;

    private LongFilter rolId;

    private Boolean distinct;

    public ParticipacionProyectoCriteria() {}

    public ParticipacionProyectoCriteria(ParticipacionProyectoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.proyectoId = other.proyectoId == null ? null : other.proyectoId.copy();
        this.rolId = other.rolId == null ? null : other.rolId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ParticipacionProyectoCriteria copy() {
        return new ParticipacionProyectoCriteria(this);
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
        final ParticipacionProyectoCriteria that = (ParticipacionProyectoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(proyectoId, that.proyectoId) &&
            Objects.equals(rolId, that.rolId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, proyectoId, rolId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParticipacionProyectoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (proyectoId != null ? "proyectoId=" + proyectoId + ", " : "") +
            (rolId != null ? "rolId=" + rolId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
