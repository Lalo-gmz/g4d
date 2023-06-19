package mx.lania.g4d.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * A Funcionalidad.
 */
@Entity
@Table(name = "funcionalidad")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Funcionalidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "url_git_lab")
    private String urlGitLab;

    @Column(name = "enlace_git_lab")
    private String enlaceGitLab;

    @Column(name = "prioridad")
    private String prioridad;

    @CreationTimestamp
    @Column(name = "creado")
    private Instant creado;

    @UpdateTimestamp
    @Column(name = "modificado")
    private Instant modificado;

    @ManyToMany
    @JoinTable(
        name = "rel_funcionalidad__user",
        joinColumns = @JoinColumn(name = "funcionalidad_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    @Column(name = "estatus_funcionalidad")
    private String estatusFuncionalidad;

    @ManyToOne
    @JsonIgnoreProperties(value = { "funcionalidads", "proyecto" }, allowSetters = true)
    private Iteracion iteracion;

    // pasar a string

    @OneToMany(mappedBy = "funcionalidad")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "funcionalidad", "atributo" }, allowSetters = true)
    private Set<AtributoFuncionalidad> atributoFuncionalidads = new HashSet<>();

    @OneToMany(mappedBy = "funcionalidad")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "funcionalidad", "user" }, allowSetters = true)
    private Set<Comentario> comentarios = new HashSet<>();

    @OneToMany(mappedBy = "funcionalidad")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "funcionalidad" }, allowSetters = true)
    private Set<Bitacora> bitacoras = new HashSet<>();

    public Funcionalidad() {}

    @JsonCreator
    public Funcionalidad(
        @JsonProperty("id") Long id,
        @JsonProperty("nombre") String nombre,
        @JsonProperty("descripcion") String descripcion,
        @JsonProperty("urlGitLab") String urlGitLab,
        @JsonProperty("prioridad") String prioridad,
        @JsonProperty("creado") Instant creado,
        @JsonProperty("modificado") Instant modificado,
        @JsonProperty("users") Set<User> users,
        @JsonProperty("estatusFuncionalidad") String estatusFuncionalidad,
        @JsonProperty("iteracion") Iteracion iteracion,
        @JsonProperty("atributoFuncionalidads") Set<AtributoFuncionalidad> atributoFuncionalidads,
        @JsonProperty("comentarios") Set<Comentario> comentarios,
        @JsonProperty("bitacoras") Set<Bitacora> bitacoras
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.urlGitLab = urlGitLab;
        this.prioridad = prioridad;
        this.creado = creado;
        this.modificado = modificado;
        this.users = users;
        this.estatusFuncionalidad = estatusFuncionalidad;
        this.iteracion = iteracion;
        this.atributoFuncionalidads = atributoFuncionalidads;
        this.comentarios = comentarios;
        this.bitacoras = bitacoras;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Funcionalidad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstatusFuncionalidad() {
        return estatusFuncionalidad;
    }

    public void setEstatusFuncionalidad(String estatusFuncionalidad) {
        this.estatusFuncionalidad = estatusFuncionalidad;
    }

    public Funcionalidad nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Funcionalidad descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlGitLab() {
        return this.urlGitLab;
    }

    public Funcionalidad urlGitLab(String urlGitLab) {
        this.setUrlGitLab(urlGitLab);
        return this;
    }

    public String getEnlaceGitLab() {
        return enlaceGitLab;
    }

    public void setEnlaceGitLab(String enlaceGitLab) {
        this.enlaceGitLab = enlaceGitLab;
    }

    public void setUrlGitLab(String urlGitLab) {
        this.urlGitLab = urlGitLab;
    }

    public Instant getCreado() {
        return this.creado;
    }

    public Funcionalidad creado(Instant creado) {
        this.setCreado(creado);
        return this;
    }

    public void setCreado(Instant creado) {
        this.creado = creado;
    }

    public Instant getModificado() {
        return this.modificado;
    }

    public Funcionalidad modificado(Instant modificado) {
        this.setModificado(modificado);
        return this;
    }

    public void setModificado(Instant modificado) {
        this.modificado = modificado;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Funcionalidad users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Funcionalidad addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Funcionalidad removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public Iteracion getIteracion() {
        return this.iteracion;
    }

    public void setIteracion(Iteracion iteracion) {
        this.iteracion = iteracion;
    }

    public Funcionalidad iteracion(Iteracion iteracion) {
        this.setIteracion(iteracion);
        return this;
    }

    public Set<AtributoFuncionalidad> getAtributoFuncionalidads() {
        return this.atributoFuncionalidads;
    }

    public void setAtributoFuncionalidads(Set<AtributoFuncionalidad> atributoFuncionalidads) {
        if (this.atributoFuncionalidads != null) {
            this.atributoFuncionalidads.forEach(i -> i.setFuncionalidad(null));
        }
        if (atributoFuncionalidads != null) {
            atributoFuncionalidads.forEach(i -> i.setFuncionalidad(this));
        }
        this.atributoFuncionalidads = atributoFuncionalidads;
    }

    public Funcionalidad atributoFuncionalidads(Set<AtributoFuncionalidad> atributoFuncionalidads) {
        this.setAtributoFuncionalidads(atributoFuncionalidads);
        return this;
    }

    public Funcionalidad addAtributoFuncionalidad(AtributoFuncionalidad atributoFuncionalidad) {
        this.atributoFuncionalidads.add(atributoFuncionalidad);
        atributoFuncionalidad.setFuncionalidad(this);
        return this;
    }

    public Funcionalidad removeAtributoFuncionalidad(AtributoFuncionalidad atributoFuncionalidad) {
        this.atributoFuncionalidads.remove(atributoFuncionalidad);
        atributoFuncionalidad.setFuncionalidad(null);
        return this;
    }

    public Set<Comentario> getComentarios() {
        return this.comentarios;
    }

    public void setComentarios(Set<Comentario> comentarios) {
        if (this.comentarios != null) {
            this.comentarios.forEach(i -> i.setFuncionalidad(null));
        }
        if (comentarios != null) {
            comentarios.forEach(i -> i.setFuncionalidad(this));
        }
        this.comentarios = comentarios;
    }

    public Funcionalidad comentarios(Set<Comentario> comentarios) {
        this.setComentarios(comentarios);
        return this;
    }

    public Funcionalidad addComentario(Comentario comentario) {
        this.comentarios.add(comentario);
        comentario.setFuncionalidad(this);
        return this;
    }

    public Funcionalidad removeComentario(Comentario comentario) {
        this.comentarios.remove(comentario);
        comentario.setFuncionalidad(null);
        return this;
    }

    public Set<Bitacora> getBitacoras() {
        return this.bitacoras;
    }

    public void setBitacoras(Set<Bitacora> bitacoras) {
        if (this.bitacoras != null) {
            this.bitacoras.forEach(i -> i.setFuncionalidad(null));
        }
        if (bitacoras != null) {
            bitacoras.forEach(i -> i.setFuncionalidad(this));
        }
        this.bitacoras = bitacoras;
    }

    public Funcionalidad bitacoras(Set<Bitacora> bitacoras) {
        this.setBitacoras(bitacoras);
        return this;
    }

    public Funcionalidad addBitacora(Bitacora bitacora) {
        this.bitacoras.add(bitacora);
        bitacora.setFuncionalidad(this);
        return this;
    }

    public Funcionalidad removeBitacora(Bitacora bitacora) {
        this.bitacoras.remove(bitacora);
        bitacora.setFuncionalidad(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Funcionalidad)) {
            return false;
        }
        return id != null && id.equals(((Funcionalidad) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Funcionalidad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", urlGitLab='" + getUrlGitLab() + "'" +
            ", creado='" + getCreado() + "'" +
            ", modificado='" + getModificado() + "'" +
            "}";
    }
}
