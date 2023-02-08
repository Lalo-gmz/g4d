package mx.lania.mca.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bitacora.
 */
@Entity
@Table(name = "bitacora")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bitacora implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tabla", nullable = false)
    private String tabla;

    @NotNull
    @Column(name = "accion", nullable = false)
    private String accion;

    @NotNull
    @Column(name = "creado", nullable = false)
    private Instant creado;

    @ManyToOne
    @JsonIgnoreProperties(value = { "funcionalidad", "proyecto", "rol", "bitacoras", "comentarios" }, allowSetters = true)
    private Usuario usuario;

    @ManyToOne
    @JsonIgnoreProperties(value = { "rols", "configuracions", "bitacoras", "usuarios", "iteracions" }, allowSetters = true)
    private Proyecto proyecto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bitacora id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTabla() {
        return this.tabla;
    }

    public Bitacora tabla(String tabla) {
        this.setTabla(tabla);
        return this;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getAccion() {
        return this.accion;
    }

    public Bitacora accion(String accion) {
        this.setAccion(accion);
        return this;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Instant getCreado() {
        return this.creado;
    }

    public Bitacora creado(Instant creado) {
        this.setCreado(creado);
        return this;
    }

    public void setCreado(Instant creado) {
        this.creado = creado;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Bitacora usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Proyecto getProyecto() {
        return this.proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Bitacora proyecto(Proyecto proyecto) {
        this.setProyecto(proyecto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bitacora)) {
            return false;
        }
        return id != null && id.equals(((Bitacora) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bitacora{" +
            "id=" + getId() +
            ", tabla='" + getTabla() + "'" +
            ", accion='" + getAccion() + "'" +
            ", creado='" + getCreado() + "'" +
            "}";
    }
}
