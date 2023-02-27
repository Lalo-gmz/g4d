package mx.lania.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Prioridad.
 */
@Entity
@Table(name = "prioridad")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Prioridad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "prioridad_numerica")
    private Integer prioridadNumerica;

    @OneToMany(mappedBy = "prioridad")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "users", "estatusFuncionalidad", "iteracion", "prioridad", "etiquetas", "atributoFuncionalidads", "comentarios", "bitacoras",
        },
        allowSetters = true
    )
    private Set<Funcionalidad> funcionalidads = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Prioridad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Prioridad nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPrioridadNumerica() {
        return this.prioridadNumerica;
    }

    public Prioridad prioridadNumerica(Integer prioridadNumerica) {
        this.setPrioridadNumerica(prioridadNumerica);
        return this;
    }

    public void setPrioridadNumerica(Integer prioridadNumerica) {
        this.prioridadNumerica = prioridadNumerica;
    }

    public Set<Funcionalidad> getFuncionalidads() {
        return this.funcionalidads;
    }

    public void setFuncionalidads(Set<Funcionalidad> funcionalidads) {
        if (this.funcionalidads != null) {
            this.funcionalidads.forEach(i -> i.setPrioridad(null));
        }
        if (funcionalidads != null) {
            funcionalidads.forEach(i -> i.setPrioridad(this));
        }
        this.funcionalidads = funcionalidads;
    }

    public Prioridad funcionalidads(Set<Funcionalidad> funcionalidads) {
        this.setFuncionalidads(funcionalidads);
        return this;
    }

    public Prioridad addFuncionalidad(Funcionalidad funcionalidad) {
        this.funcionalidads.add(funcionalidad);
        funcionalidad.setPrioridad(this);
        return this;
    }

    public Prioridad removeFuncionalidad(Funcionalidad funcionalidad) {
        this.funcionalidads.remove(funcionalidad);
        funcionalidad.setPrioridad(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prioridad)) {
            return false;
        }
        return id != null && id.equals(((Prioridad) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prioridad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", prioridadNumerica=" + getPrioridadNumerica() +
            "}";
    }
}
