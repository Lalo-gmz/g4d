package mx.lania.mca.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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

    @OneToMany(mappedBy = "proyecto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "proyecto", "usuarios", "permisos" }, allowSetters = true)
    private Set<Rol> rols = new HashSet<>();

    @OneToMany(mappedBy = "proyecto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "proyecto" }, allowSetters = true)
    private Set<Configuracion> configuracions = new HashSet<>();

    @OneToMany(mappedBy = "proyecto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "usuario", "proyecto" }, allowSetters = true)
    private Set<Bitacora> bitacoras = new HashSet<>();

    @OneToMany(mappedBy = "proyecto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "funcionalidad", "proyecto", "rol", "bitacoras", "comentarios" }, allowSetters = true)
    private Set<Usuario> usuarios = new HashSet<>();

    @OneToMany(mappedBy = "proyecto")
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

    public Set<Rol> getRols() {
        return this.rols;
    }

    public void setRols(Set<Rol> rols) {
        if (this.rols != null) {
            this.rols.forEach(i -> i.setProyecto(null));
        }
        if (rols != null) {
            rols.forEach(i -> i.setProyecto(this));
        }
        this.rols = rols;
    }

    public Proyecto rols(Set<Rol> rols) {
        this.setRols(rols);
        return this;
    }

    public Proyecto addRol(Rol rol) {
        this.rols.add(rol);
        rol.setProyecto(this);
        return this;
    }

    public Proyecto removeRol(Rol rol) {
        this.rols.remove(rol);
        rol.setProyecto(null);
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

    public Set<Bitacora> getBitacoras() {
        return this.bitacoras;
    }

    public void setBitacoras(Set<Bitacora> bitacoras) {
        if (this.bitacoras != null) {
            this.bitacoras.forEach(i -> i.setProyecto(null));
        }
        if (bitacoras != null) {
            bitacoras.forEach(i -> i.setProyecto(this));
        }
        this.bitacoras = bitacoras;
    }

    public Proyecto bitacoras(Set<Bitacora> bitacoras) {
        this.setBitacoras(bitacoras);
        return this;
    }

    public Proyecto addBitacora(Bitacora bitacora) {
        this.bitacoras.add(bitacora);
        bitacora.setProyecto(this);
        return this;
    }

    public Proyecto removeBitacora(Bitacora bitacora) {
        this.bitacoras.remove(bitacora);
        bitacora.setProyecto(null);
        return this;
    }

    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        if (this.usuarios != null) {
            this.usuarios.forEach(i -> i.setProyecto(null));
        }
        if (usuarios != null) {
            usuarios.forEach(i -> i.setProyecto(this));
        }
        this.usuarios = usuarios;
    }

    public Proyecto usuarios(Set<Usuario> usuarios) {
        this.setUsuarios(usuarios);
        return this;
    }

    public Proyecto addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.setProyecto(this);
        return this;
    }

    public Proyecto removeUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
        usuario.setProyecto(null);
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
            "}";
    }
}
