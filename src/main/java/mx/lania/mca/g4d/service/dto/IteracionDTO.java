package mx.lania.mca.g4d.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link mx.lania.mca.g4d.domain.Iteracion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IteracionDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    private LocalDate inicio;

    private LocalDate fin;

    private ProyectoDTO proyecto;

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

    public LocalDate getInicio() {
        return inicio;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getFin() {
        return fin;
    }

    public void setFin(LocalDate fin) {
        this.fin = fin;
    }

    public ProyectoDTO getProyecto() {
        return proyecto;
    }

    public void setProyecto(ProyectoDTO proyecto) {
        this.proyecto = proyecto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IteracionDTO)) {
            return false;
        }

        IteracionDTO iteracionDTO = (IteracionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, iteracionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IteracionDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", inicio='" + getInicio() + "'" +
            ", fin='" + getFin() + "'" +
            ", proyecto=" + getProyecto() +
            "}";
    }
}
