package mx.lania.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Etiqueta.
 */
@Entity
@Table(name = "etiqueta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Etiqueta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "color")
    private String color;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "users", "estatusFuncionalidad", "iteracion", "prioridad", "etiquetas", "atributoFuncionalidads", "comentarios" },
        allowSetters = true
    )
    private Funcionalidad funcionalidad;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Etiqueta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Etiqueta nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return this.color;
    }

    public Etiqueta color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Funcionalidad getFuncionalidad() {
        return this.funcionalidad;
    }

    public void setFuncionalidad(Funcionalidad funcionalidad) {
        this.funcionalidad = funcionalidad;
    }

    public Etiqueta funcionalidad(Funcionalidad funcionalidad) {
        this.setFuncionalidad(funcionalidad);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etiqueta)) {
            return false;
        }
        return id != null && id.equals(((Etiqueta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etiqueta{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", color='" + getColor() + "'" +
            "}";
    }
}
