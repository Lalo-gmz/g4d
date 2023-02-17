package mx.lania.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Rol.
 */
@Entity
@Table(name = "rol")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "rol")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "proyecto", "rol" }, allowSetters = true)
    private Set<ParticipacionProyecto> participacionProyectos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rol id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Rol nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<ParticipacionProyecto> getParticipacionProyectos() {
        return this.participacionProyectos;
    }

    public void setParticipacionProyectos(Set<ParticipacionProyecto> participacionProyectos) {
        if (this.participacionProyectos != null) {
            this.participacionProyectos.forEach(i -> i.setRol(null));
        }
        if (participacionProyectos != null) {
            participacionProyectos.forEach(i -> i.setRol(this));
        }
        this.participacionProyectos = participacionProyectos;
    }

    public Rol participacionProyectos(Set<ParticipacionProyecto> participacionProyectos) {
        this.setParticipacionProyectos(participacionProyectos);
        return this;
    }

    public Rol addParticipacionProyecto(ParticipacionProyecto participacionProyecto) {
        this.participacionProyectos.add(participacionProyecto);
        participacionProyecto.setRol(this);
        return this;
    }

    public Rol removeParticipacionProyecto(ParticipacionProyecto participacionProyecto) {
        this.participacionProyectos.remove(participacionProyecto);
        participacionProyecto.setRol(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rol)) {
            return false;
        }
        return id != null && id.equals(((Rol) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rol{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
