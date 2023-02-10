package mx.lania.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EstatusFuncionalidad.
 */
@Entity
@Table(name = "estatus_funcionalidad")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EstatusFuncionalidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "prioridad")
    private Integer prioridad;

    @OneToMany(mappedBy = "estatusFuncionalidad")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "estatusFuncionalidad", "iteracion", "etiquetas", "usuarios", "atributos", "comentarios" },
        allowSetters = true
    )
    private Set<Funcionalidad> funcionalidads = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EstatusFuncionalidad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public EstatusFuncionalidad nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPrioridad() {
        return this.prioridad;
    }

    public EstatusFuncionalidad prioridad(Integer prioridad) {
        this.setPrioridad(prioridad);
        return this;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public Set<Funcionalidad> getFuncionalidads() {
        return this.funcionalidads;
    }

    public void setFuncionalidads(Set<Funcionalidad> funcionalidads) {
        if (this.funcionalidads != null) {
            this.funcionalidads.forEach(i -> i.setEstatusFuncionalidad(null));
        }
        if (funcionalidads != null) {
            funcionalidads.forEach(i -> i.setEstatusFuncionalidad(this));
        }
        this.funcionalidads = funcionalidads;
    }

    public EstatusFuncionalidad funcionalidads(Set<Funcionalidad> funcionalidads) {
        this.setFuncionalidads(funcionalidads);
        return this;
    }

    public EstatusFuncionalidad addFuncionalidad(Funcionalidad funcionalidad) {
        this.funcionalidads.add(funcionalidad);
        funcionalidad.setEstatusFuncionalidad(this);
        return this;
    }

    public EstatusFuncionalidad removeFuncionalidad(Funcionalidad funcionalidad) {
        this.funcionalidads.remove(funcionalidad);
        funcionalidad.setEstatusFuncionalidad(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EstatusFuncionalidad)) {
            return false;
        }
        return id != null && id.equals(((EstatusFuncionalidad) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EstatusFuncionalidad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", prioridad=" + getPrioridad() +
            "}";
    }
}
