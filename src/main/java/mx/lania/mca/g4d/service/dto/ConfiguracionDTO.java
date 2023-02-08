package mx.lania.mca.g4d.service.dto;

import java.io.Serializable;
import java.util.Objects;
import mx.lania.mca.g4d.domain.enumeration.EtiquetaVisual;

/**
 * A DTO for the {@link mx.lania.mca.g4d.domain.Configuracion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConfiguracionDTO implements Serializable {

    private Long id;

    private EtiquetaVisual clave;

    private String valor;

    private ProyectoDTO proyecto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EtiquetaVisual getClave() {
        return clave;
    }

    public void setClave(EtiquetaVisual clave) {
        this.clave = clave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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
        if (!(o instanceof ConfiguracionDTO)) {
            return false;
        }

        ConfiguracionDTO configuracionDTO = (ConfiguracionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, configuracionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConfiguracionDTO{" +
            "id=" + getId() +
            ", clave='" + getClave() + "'" +
            ", valor='" + getValor() + "'" +
            ", proyecto=" + getProyecto() +
            "}";
    }
}
