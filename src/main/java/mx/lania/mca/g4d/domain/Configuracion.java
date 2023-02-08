package mx.lania.mca.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import mx.lania.mca.g4d.domain.enumeration.EtiquetaVisual;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Configuracion.
 */
@Entity
@Table(name = "configuracion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Configuracion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "clave")
    private EtiquetaVisual clave;

    @Column(name = "valor")
    private String valor;

    @ManyToOne
    @JsonIgnoreProperties(value = { "rols", "configuracions", "bitacoras", "usuarios", "iteracions" }, allowSetters = true)
    private Proyecto proyecto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Configuracion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EtiquetaVisual getClave() {
        return this.clave;
    }

    public Configuracion clave(EtiquetaVisual clave) {
        this.setClave(clave);
        return this;
    }

    public void setClave(EtiquetaVisual clave) {
        this.clave = clave;
    }

    public String getValor() {
        return this.valor;
    }

    public Configuracion valor(String valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Proyecto getProyecto() {
        return this.proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Configuracion proyecto(Proyecto proyecto) {
        this.setProyecto(proyecto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Configuracion)) {
            return false;
        }
        return id != null && id.equals(((Configuracion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Configuracion{" +
            "id=" + getId() +
            ", clave='" + getClave() + "'" +
            ", valor='" + getValor() + "'" +
            "}";
    }
}
