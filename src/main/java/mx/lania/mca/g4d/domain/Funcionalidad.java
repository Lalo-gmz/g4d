package mx.lania.mca.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @Column(name = "creado")
    private Instant creado;

    @Column(name = "modificado")
    private Instant modificado;

    @ManyToOne
    @JsonIgnoreProperties(value = { "funcionalidads" }, allowSetters = true)
    private EstatusFuncionalidad estatusFuncionalidad;

    @ManyToOne
    @JsonIgnoreProperties(value = { "funcionalidads", "proyecto" }, allowSetters = true)
    private Iteracion iteracion;

    @OneToMany(mappedBy = "funcionalidad")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "funcionalidad" }, allowSetters = true)
    private Set<Etiqueta> etiquetas = new HashSet<>();

    @OneToMany(mappedBy = "funcionalidad")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "funcionalidad", "proyecto", "rol", "bitacoras", "comentarios" }, allowSetters = true)
    private Set<Usuario> usuarios = new HashSet<>();

    @OneToMany(mappedBy = "funcionalidad")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "funcionalidad" }, allowSetters = true)
    private Set<Atributo> atributos = new HashSet<>();

    @OneToMany(mappedBy = "funcionalidad")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "funcionalidad", "usuario" }, allowSetters = true)
    private Set<Comentario> comentarios = new HashSet<>();

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

    public void setUrlGitLab(String urlGitLab) {
        this.urlGitLab = urlGitLab;
    }

    public LocalDate getFechaEntrega() {
        return this.fechaEntrega;
    }

    public Funcionalidad fechaEntrega(LocalDate fechaEntrega) {
        this.setFechaEntrega(fechaEntrega);
        return this;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
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

    public EstatusFuncionalidad getEstatusFuncionalidad() {
        return this.estatusFuncionalidad;
    }

    public void setEstatusFuncionalidad(EstatusFuncionalidad estatusFuncionalidad) {
        this.estatusFuncionalidad = estatusFuncionalidad;
    }

    public Funcionalidad estatusFuncionalidad(EstatusFuncionalidad estatusFuncionalidad) {
        this.setEstatusFuncionalidad(estatusFuncionalidad);
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

    public Set<Etiqueta> getEtiquetas() {
        return this.etiquetas;
    }

    public void setEtiquetas(Set<Etiqueta> etiquetas) {
        if (this.etiquetas != null) {
            this.etiquetas.forEach(i -> i.setFuncionalidad(null));
        }
        if (etiquetas != null) {
            etiquetas.forEach(i -> i.setFuncionalidad(this));
        }
        this.etiquetas = etiquetas;
    }

    public Funcionalidad etiquetas(Set<Etiqueta> etiquetas) {
        this.setEtiquetas(etiquetas);
        return this;
    }

    public Funcionalidad addEtiqueta(Etiqueta etiqueta) {
        this.etiquetas.add(etiqueta);
        etiqueta.setFuncionalidad(this);
        return this;
    }

    public Funcionalidad removeEtiqueta(Etiqueta etiqueta) {
        this.etiquetas.remove(etiqueta);
        etiqueta.setFuncionalidad(null);
        return this;
    }

    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        if (this.usuarios != null) {
            this.usuarios.forEach(i -> i.setFuncionalidad(null));
        }
        if (usuarios != null) {
            usuarios.forEach(i -> i.setFuncionalidad(this));
        }
        this.usuarios = usuarios;
    }

    public Funcionalidad usuarios(Set<Usuario> usuarios) {
        this.setUsuarios(usuarios);
        return this;
    }

    public Funcionalidad addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.setFuncionalidad(this);
        return this;
    }

    public Funcionalidad removeUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
        usuario.setFuncionalidad(null);
        return this;
    }

    public Set<Atributo> getAtributos() {
        return this.atributos;
    }

    public void setAtributos(Set<Atributo> atributos) {
        if (this.atributos != null) {
            this.atributos.forEach(i -> i.setFuncionalidad(null));
        }
        if (atributos != null) {
            atributos.forEach(i -> i.setFuncionalidad(this));
        }
        this.atributos = atributos;
    }

    public Funcionalidad atributos(Set<Atributo> atributos) {
        this.setAtributos(atributos);
        return this;
    }

    public Funcionalidad addAtributo(Atributo atributo) {
        this.atributos.add(atributo);
        atributo.setFuncionalidad(this);
        return this;
    }

    public Funcionalidad removeAtributo(Atributo atributo) {
        this.atributos.remove(atributo);
        atributo.setFuncionalidad(null);
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
            ", fechaEntrega='" + getFechaEntrega() + "'" +
            ", creado='" + getCreado() + "'" +
            ", modificado='" + getModificado() + "'" +
            "}";
    }
}
