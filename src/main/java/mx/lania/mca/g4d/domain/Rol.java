package mx.lania.mca.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
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

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "rols", "configuracions", "bitacoras", "usuarios", "iteracions" }, allowSetters = true)
    private Proyecto proyecto;

    @OneToMany(mappedBy = "rol")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "funcionalidad", "proyecto", "rol", "bitacoras", "comentarios" }, allowSetters = true)
    private Set<Usuario> usuarios = new HashSet<>();

    @OneToMany(mappedBy = "rol")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "rol" }, allowSetters = true)
    private Set<Permiso> permisos = new HashSet<>();

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

    public Proyecto getProyecto() {
        return this.proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Rol proyecto(Proyecto proyecto) {
        this.setProyecto(proyecto);
        return this;
    }

    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        if (this.usuarios != null) {
            this.usuarios.forEach(i -> i.setRol(null));
        }
        if (usuarios != null) {
            usuarios.forEach(i -> i.setRol(this));
        }
        this.usuarios = usuarios;
    }

    public Rol usuarios(Set<Usuario> usuarios) {
        this.setUsuarios(usuarios);
        return this;
    }

    public Rol addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.setRol(this);
        return this;
    }

    public Rol removeUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
        usuario.setRol(null);
        return this;
    }

    public Set<Permiso> getPermisos() {
        return this.permisos;
    }

    public void setPermisos(Set<Permiso> permisos) {
        if (this.permisos != null) {
            this.permisos.forEach(i -> i.setRol(null));
        }
        if (permisos != null) {
            permisos.forEach(i -> i.setRol(this));
        }
        this.permisos = permisos;
    }

    public Rol permisos(Set<Permiso> permisos) {
        this.setPermisos(permisos);
        return this;
    }

    public Rol addPermiso(Permiso permiso) {
        this.permisos.add(permiso);
        permiso.setRol(this);
        return this;
    }

    public Rol removePermiso(Permiso permiso) {
        this.permisos.remove(permiso);
        permiso.setRol(null);
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
