package mx.lania.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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

    @Column(name = "para_gitlab")
    private boolean paraGitLab;

    @OneToMany(mappedBy = "atributo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "funcionalidad", "atributo" }, allowSetters = true)
    private Set<AtributoFuncionalidad> atributoFuncionalidads = new HashSet<>();

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

    public boolean isParaGitLab() {
        return paraGitLab;
    }

    public void setParaGitLab(boolean paraGitLab) {
        this.paraGitLab = paraGitLab;
    }

    public Set<AtributoFuncionalidad> getAtributoFuncionalidads() {
        return this.atributoFuncionalidads;
    }

    public void setAtributoFuncionalidads(Set<AtributoFuncionalidad> atributoFuncionalidads) {
        if (this.atributoFuncionalidads != null) {
            this.atributoFuncionalidads.forEach(i -> i.setAtributo(null));
        }
        if (atributoFuncionalidads != null) {
            atributoFuncionalidads.forEach(i -> i.setAtributo(this));
        }
        this.atributoFuncionalidads = atributoFuncionalidads;
    }

    public Atributo atributoFuncionalidads(Set<AtributoFuncionalidad> atributoFuncionalidads) {
        this.setAtributoFuncionalidads(atributoFuncionalidads);
        return this;
    }

    public Atributo addAtributoFuncionalidad(AtributoFuncionalidad atributoFuncionalidad) {
        this.atributoFuncionalidads.add(atributoFuncionalidad);
        atributoFuncionalidad.setAtributo(this);
        return this;
    }

    public Atributo removeAtributoFuncionalidad(AtributoFuncionalidad atributoFuncionalidad) {
        this.atributoFuncionalidads.remove(atributoFuncionalidad);
        atributoFuncionalidad.setAtributo(null);
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
            "}";
    }
}
