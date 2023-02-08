package mx.lania.mca.g4d.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link mx.lania.mca.g4d.domain.Funcionalidad} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FuncionalidadDTO implements Serializable {

    private Long id;

    private String nombre;

    private String descripcion;

    private String urlGitLab;

    private LocalDate fechaEntrega;

    private Instant creado;

    private Instant modificado;

    private EstatusFuncionalidadDTO estatusFuncionalidad;

    private IteracionDTO iteracion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlGitLab() {
        return urlGitLab;
    }

    public void setUrlGitLab(String urlGitLab) {
        this.urlGitLab = urlGitLab;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Instant getCreado() {
        return creado;
    }

    public void setCreado(Instant creado) {
        this.creado = creado;
    }

    public Instant getModificado() {
        return modificado;
    }

    public void setModificado(Instant modificado) {
        this.modificado = modificado;
    }

    public EstatusFuncionalidadDTO getEstatusFuncionalidad() {
        return estatusFuncionalidad;
    }

    public void setEstatusFuncionalidad(EstatusFuncionalidadDTO estatusFuncionalidad) {
        this.estatusFuncionalidad = estatusFuncionalidad;
    }

    public IteracionDTO getIteracion() {
        return iteracion;
    }

    public void setIteracion(IteracionDTO iteracion) {
        this.iteracion = iteracion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FuncionalidadDTO)) {
            return false;
        }

        FuncionalidadDTO funcionalidadDTO = (FuncionalidadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, funcionalidadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FuncionalidadDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", urlGitLab='" + getUrlGitLab() + "'" +
            ", fechaEntrega='" + getFechaEntrega() + "'" +
            ", creado='" + getCreado() + "'" +
            ", modificado='" + getModificado() + "'" +
            ", estatusFuncionalidad=" + getEstatusFuncionalidad() +
            ", iteracion=" + getIteracion() +
            "}";
    }
}
