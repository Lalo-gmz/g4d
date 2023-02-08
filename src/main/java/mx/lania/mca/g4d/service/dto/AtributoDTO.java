package mx.lania.mca.g4d.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link mx.lania.mca.g4d.domain.Atributo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AtributoDTO implements Serializable {

    private Long id;

    private String nombre;

    private Boolean marcado;

    private Boolean auxiliar;

    private FuncionalidadDTO funcionalidad;

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

    public Boolean getMarcado() {
        return marcado;
    }

    public void setMarcado(Boolean marcado) {
        this.marcado = marcado;
    }

    public Boolean getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(Boolean auxiliar) {
        this.auxiliar = auxiliar;
    }

    public FuncionalidadDTO getFuncionalidad() {
        return funcionalidad;
    }

    public void setFuncionalidad(FuncionalidadDTO funcionalidad) {
        this.funcionalidad = funcionalidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AtributoDTO)) {
            return false;
        }

        AtributoDTO atributoDTO = (AtributoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, atributoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AtributoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", marcado='" + getMarcado() + "'" +
            ", auxiliar='" + getAuxiliar() + "'" +
            ", funcionalidad=" + getFuncionalidad() +
            "}";
    }
}
