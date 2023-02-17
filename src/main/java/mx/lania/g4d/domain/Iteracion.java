package mx.lania.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Iteracion.
 */
@Entity
@Table(name = "iteracion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Iteracion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "inicio")
    private LocalDate inicio;

    @Column(name = "fin")
    private LocalDate fin;

    @OneToMany(mappedBy = "iteracion")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "users", "estatusFuncionalidad", "iteracion", "prioridad", "etiquetas", "atributoFuncionalidads", "comentarios" },
        allowSetters = true
    )
    private Set<Funcionalidad> funcionalidads = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "participacionProyectos", "configuracions", "bitacoras", "iteracions" }, allowSetters = true)
    private Proyecto proyecto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Iteracion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Iteracion nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getInicio() {
        return this.inicio;
    }

    public Iteracion inicio(LocalDate inicio) {
        this.setInicio(inicio);
        return this;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getFin() {
        return this.fin;
    }

    public Iteracion fin(LocalDate fin) {
        this.setFin(fin);
        return this;
    }

    public void setFin(LocalDate fin) {
        this.fin = fin;
    }

    public Set<Funcionalidad> getFuncionalidads() {
        return this.funcionalidads;
    }

    public void setFuncionalidads(Set<Funcionalidad> funcionalidads) {
        if (this.funcionalidads != null) {
            this.funcionalidads.forEach(i -> i.setIteracion(null));
        }
        if (funcionalidads != null) {
            funcionalidads.forEach(i -> i.setIteracion(this));
        }
        this.funcionalidads = funcionalidads;
    }

    public Iteracion funcionalidads(Set<Funcionalidad> funcionalidads) {
        this.setFuncionalidads(funcionalidads);
        return this;
    }

    public Iteracion addFuncionalidad(Funcionalidad funcionalidad) {
        this.funcionalidads.add(funcionalidad);
        funcionalidad.setIteracion(this);
        return this;
    }

    public Iteracion removeFuncionalidad(Funcionalidad funcionalidad) {
        this.funcionalidads.remove(funcionalidad);
        funcionalidad.setIteracion(null);
        return this;
    }

    public Proyecto getProyecto() {
        return this.proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Iteracion proyecto(Proyecto proyecto) {
        this.setProyecto(proyecto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Iteracion)) {
            return false;
        }
        return id != null && id.equals(((Iteracion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Iteracion{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", inicio='" + getInicio() + "'" +
            ", fin='" + getFin() + "'" +
            "}";
    }
}
