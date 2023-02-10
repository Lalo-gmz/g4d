package mx.lania.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Atributo.
 */
@Entity
@Table(name = "atributo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Atributo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "marcado")
    private Boolean marcado;

    @Column(name = "auxiliar")
    private Boolean auxiliar;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "estatusFuncionalidad", "iteracion", "etiquetas", "usuarios", "atributos", "comentarios" },
        allowSetters = true
    )
    private Funcionalidad funcionalidad;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Atributo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Atributo nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getMarcado() {
        return this.marcado;
    }

    public Atributo marcado(Boolean marcado) {
        this.setMarcado(marcado);
        return this;
    }

    public void setMarcado(Boolean marcado) {
        this.marcado = marcado;
    }

    public Boolean getAuxiliar() {
        return this.auxiliar;
    }

    public Atributo auxiliar(Boolean auxiliar) {
        this.setAuxiliar(auxiliar);
        return this;
    }

    public void setAuxiliar(Boolean auxiliar) {
        this.auxiliar = auxiliar;
    }

    public Funcionalidad getFuncionalidad() {
        return this.funcionalidad;
    }

    public void setFuncionalidad(Funcionalidad funcionalidad) {
        this.funcionalidad = funcionalidad;
    }

    public Atributo funcionalidad(Funcionalidad funcionalidad) {
        this.setFuncionalidad(funcionalidad);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Atributo)) {
            return false;
        }
        return id != null && id.equals(((Atributo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Atributo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", marcado='" + getMarcado() + "'" +
            ", auxiliar='" + getAuxiliar() + "'" +
            "}";
    }
}
