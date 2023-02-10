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
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "id_git_lab", nullable = false, unique = true)
    private String idGitLab;

    @Column(name = "token_identificacion")
    private String tokenIdentificacion;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "estatusFuncionalidad", "iteracion", "etiquetas", "usuarios", "atributos", "comentarios" },
        allowSetters = true
    )
    private Funcionalidad funcionalidad;

    @ManyToOne
    @JsonIgnoreProperties(value = { "rols", "configuracions", "bitacoras", "usuarios", "iteracions" }, allowSetters = true)
    private Proyecto proyecto;

    @ManyToOne
    @JsonIgnoreProperties(value = { "proyecto", "usuarios", "permisos" }, allowSetters = true)
    private Rol rol;

    @OneToMany(mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "usuario", "proyecto" }, allowSetters = true)
    private Set<Bitacora> bitacoras = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "funcionalidad", "usuario" }, allowSetters = true)
    private Set<Comentario> comentarios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Usuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Usuario nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdGitLab() {
        return this.idGitLab;
    }

    public Usuario idGitLab(String idGitLab) {
        this.setIdGitLab(idGitLab);
        return this;
    }

    public void setIdGitLab(String idGitLab) {
        this.idGitLab = idGitLab;
    }

    public String getTokenIdentificacion() {
        return this.tokenIdentificacion;
    }

    public Usuario tokenIdentificacion(String tokenIdentificacion) {
        this.setTokenIdentificacion(tokenIdentificacion);
        return this;
    }

    public void setTokenIdentificacion(String tokenIdentificacion) {
        this.tokenIdentificacion = tokenIdentificacion;
    }

    public Funcionalidad getFuncionalidad() {
        return this.funcionalidad;
    }

    public void setFuncionalidad(Funcionalidad funcionalidad) {
        this.funcionalidad = funcionalidad;
    }

    public Usuario funcionalidad(Funcionalidad funcionalidad) {
        this.setFuncionalidad(funcionalidad);
        return this;
    }

    public Proyecto getProyecto() {
        return this.proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Usuario proyecto(Proyecto proyecto) {
        this.setProyecto(proyecto);
        return this;
    }

    public Rol getRol() {
        return this.rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Usuario rol(Rol rol) {
        this.setRol(rol);
        return this;
    }

    public Set<Bitacora> getBitacoras() {
        return this.bitacoras;
    }

    public void setBitacoras(Set<Bitacora> bitacoras) {
        if (this.bitacoras != null) {
            this.bitacoras.forEach(i -> i.setUsuario(null));
        }
        if (bitacoras != null) {
            bitacoras.forEach(i -> i.setUsuario(this));
        }
        this.bitacoras = bitacoras;
    }

    public Usuario bitacoras(Set<Bitacora> bitacoras) {
        this.setBitacoras(bitacoras);
        return this;
    }

    public Usuario addBitacora(Bitacora bitacora) {
        this.bitacoras.add(bitacora);
        bitacora.setUsuario(this);
        return this;
    }

    public Usuario removeBitacora(Bitacora bitacora) {
        this.bitacoras.remove(bitacora);
        bitacora.setUsuario(null);
        return this;
    }

    public Set<Comentario> getComentarios() {
        return this.comentarios;
    }

    public void setComentarios(Set<Comentario> comentarios) {
        if (this.comentarios != null) {
            this.comentarios.forEach(i -> i.setUsuario(null));
        }
        if (comentarios != null) {
            comentarios.forEach(i -> i.setUsuario(this));
        }
        this.comentarios = comentarios;
    }

    public Usuario comentarios(Set<Comentario> comentarios) {
        this.setComentarios(comentarios);
        return this;
    }

    public Usuario addComentario(Comentario comentario) {
        this.comentarios.add(comentario);
        comentario.setUsuario(this);
        return this;
    }

    public Usuario removeComentario(Comentario comentario) {
        this.comentarios.remove(comentario);
        comentario.setUsuario(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return id != null && id.equals(((Usuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", idGitLab='" + getIdGitLab() + "'" +
            ", tokenIdentificacion='" + getTokenIdentificacion() + "'" +
            "}";
    }
}
