package mx.lania.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Captura.
 */
@Entity
@Table(name = "captura")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Captura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "funcionalidades")
    private String funcionalidades;

    @Column(name = "fecha")
    private Instant fecha;

    @ManyToOne
    @JsonIgnoreProperties(value = { "funcionalidads", "iteracion" }, allowSetters = true)
    private Proyecto proyecto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Captura id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFuncionalidades() {
        return this.funcionalidades;
    }

    public Captura funcionalidades(String funcionalidades) {
        this.setFuncionalidades(funcionalidades);
        return this;
    }

    public void setFuncionalidades(String funcionalidades) {
        this.funcionalidades = funcionalidades;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Captura fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Proyecto getProyecto() {
        return this.proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Captura proyecto(Proyecto proyecto) {
        this.setProyecto(proyecto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Captura)) {
            return false;
        }
        return id != null && id.equals(((Captura) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Captura{" +
            "id=" + getId() +
            ", funcionalidades='" + getFuncionalidades() + "'" +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
