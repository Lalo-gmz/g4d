package mx.lania.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

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
    @Column(name = "accion", nullable = false)
    private String accion;

    @CreationTimestamp
    @NotNull
    @Column(name = "creado", nullable = false)
    private Instant creado;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "users", "estatusFuncionalidad", "iteracion", "prioridad", "etiquetas", "atributoFuncionalidads", "comentarios", "bitacoras",
        },
        allowSetters = true
    )
    private Funcionalidad funcionalidad;

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

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Bitacora user(User user) {
        this.setUser(user);
        return this;
    }

    public Funcionalidad getFuncionalidad() {
        return this.funcionalidad;
    }

    public void setFuncionalidad(Funcionalidad funcionalidad) {
        this.funcionalidad = funcionalidad;
    }

    public Bitacora funcionalidad(Funcionalidad funcionalidad) {
        this.setFuncionalidad(funcionalidad);
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
            ", accion='" + getAccion() + "'" +
            ", creado='" + getCreado() + "'" +
            "}";
    }
}
