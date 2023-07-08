package mx.lania.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * A Proyecto.
 */
@Entity
@Table(name = "proyecto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Proyecto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @NotNull
    @Column(name = "id_proyecto_git_lab", nullable = false, unique = true)
    private String idProyectoGitLab;

    @Column(name = "enlace_git_lab")
    private String enlaceGitLab;

    @Column(name = "abierto", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean abierto;

    @CreationTimestamp
    @Column(name = "creado")
    private Instant creado;

    @UpdateTimestamp
    @Column(name = "modificado")
    private Instant modificado;

    @ManyToMany
    @JoinTable(
        name = "rel_proyecto__participantes",
        joinColumns = @JoinColumn(name = "proyecto_id"),
        inverseJoinColumns = @JoinColumn(name = "participantes_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> participantes = new HashSet<>();

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "proyecto" }, allowSetters = true)
    private Set<Configuracion> configuracions = new HashSet<>();

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "funcionalidads", "proyecto" }, allowSetters = true)
    private Set<Iteracion> iteracions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Proyecto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Proyecto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public String getEnlaceGitLab() {
        return enlaceGitLab;
    }

    public void setEnlaceGitLab(String enlaceGitLab) {
        this.enlaceGitLab = enlaceGitLab;
    }

    public boolean isAbierto() {
        return abierto;
    }

    public void setAbierto(boolean abierto) {
        this.abierto = abierto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdProyectoGitLab() {
        return this.idProyectoGitLab;
    }

    public Proyecto idProyectoGitLab(String idProyectoGitLab) {
        this.setIdProyectoGitLab(idProyectoGitLab);
        return this;
    }

    public void setIdProyectoGitLab(String idProyectoGitLab) {
        this.idProyectoGitLab = idProyectoGitLab;
    }

    public Instant getCreado() {
        return this.creado;
    }

    public Proyecto creado(Instant creado) {
        this.setCreado(creado);
        return this;
    }

    public void setCreado(Instant creado) {
        this.creado = creado;
    }

    public Instant getModificado() {
        return this.modificado;
    }

    public Proyecto modificado(Instant modificado) {
        this.setModificado(modificado);
        return this;
    }

    public void setModificado(Instant modificado) {
        this.modificado = modificado;
    }

    public Set<User> getParticipantes() {
        return this.participantes;
    }

    public void setParticipantes(Set<User> users) {
        this.participantes = users;
    }

    public Proyecto participantes(Set<User> users) {
        this.setParticipantes(users);
        return this;
    }

    public Proyecto addParticipantes(User user) {
        this.participantes.add(user);
        return this;
    }

    public Proyecto removeParticipantes(User user) {
        this.participantes.remove(user);
        return this;
    }

    public Set<Configuracion> getConfiguracions() {
        return this.configuracions;
    }

    public void setConfiguracions(Set<Configuracion> configuracions) {
        if (this.configuracions != null) {
            this.configuracions.forEach(i -> i.setProyecto(null));
        }
        if (configuracions != null) {
            configuracions.forEach(i -> i.setProyecto(this));
        }
        this.configuracions = configuracions;
    }

    public Proyecto configuracions(Set<Configuracion> configuracions) {
        this.setConfiguracions(configuracions);
        return this;
    }

    public Proyecto addConfiguracion(Configuracion configuracion) {
        this.configuracions.add(configuracion);
        configuracion.setProyecto(this);
        return this;
    }

    public Proyecto removeConfiguracion(Configuracion configuracion) {
        this.configuracions.remove(configuracion);
        configuracion.setProyecto(null);
        return this;
    }

    public Set<Iteracion> getIteracions() {
        return this.iteracions;
    }

    public void setIteracions(Set<Iteracion> iteracions) {
        if (this.iteracions != null) {
            this.iteracions.forEach(i -> i.setProyecto(null));
        }
        if (iteracions != null) {
            iteracions.forEach(i -> i.setProyecto(this));
        }
        this.iteracions = iteracions;
    }

    public Proyecto iteracions(Set<Iteracion> iteracions) {
        this.setIteracions(iteracions);
        return this;
    }

    public Proyecto addIteracion(Iteracion iteracion) {
        this.iteracions.add(iteracion);
        iteracion.setProyecto(this);
        return this;
    }

    public Proyecto removeIteracion(Iteracion iteracion) {
        this.iteracions.remove(iteracion);
        iteracion.setProyecto(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proyecto)) {
            return false;
        }
        return id != null && id.equals(((Proyecto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proyecto{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", idProyectoGitLab='" + getIdProyectoGitLab() + "'" +
            ", creado='" + getCreado() + "'" +
            ", modificado='" + getModificado() + "'" +
            "}";
    }
}
