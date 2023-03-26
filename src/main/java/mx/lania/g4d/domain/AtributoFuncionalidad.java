package mx.lania.g4d.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AtributoFuncionalidad.
 */
@Entity
@Table(name = "atributo_funcionalidad")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AtributoFuncionalidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "marcado")
    private Boolean marcado;

    @Column(name = "para_gitlab")
    private Boolean paraGitLab;

    @Column(name = "valor")
    private String valor;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "users", "estatusFuncionalidad", "iteracion", "prioridad", "etiquetas", "atributoFuncionalidads", "comentarios", "bitacoras",
        },
        allowSetters = true
    )
    private Funcionalidad funcionalidad;

    @ManyToOne
    @JsonIgnoreProperties(value = { "atributoFuncionalidads" }, allowSetters = true)
    private Atributo atributo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AtributoFuncionalidad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getMarcado() {
        return this.marcado;
    }

    public AtributoFuncionalidad marcado(Boolean marcado) {
        this.setMarcado(marcado);
        return this;
    }

    public void setMarcado(Boolean marcado) {
        this.marcado = marcado;
    }

    public Boolean getParaGitLab() {
        return paraGitLab;
    }

    public void setParaGitLab(Boolean paraGitLab) {
        this.paraGitLab = paraGitLab;
    }

    public String getValor() {
        return this.valor;
    }

    public AtributoFuncionalidad valor(String valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Funcionalidad getFuncionalidad() {
        return this.funcionalidad;
    }

    public void setFuncionalidad(Funcionalidad funcionalidad) {
        this.funcionalidad = funcionalidad;
    }

    public AtributoFuncionalidad funcionalidad(Funcionalidad funcionalidad) {
        this.setFuncionalidad(funcionalidad);
        return this;
    }

    public Atributo getAtributo() {
        return this.atributo;
    }

    public void setAtributo(Atributo atributo) {
        this.atributo = atributo;
    }

    public AtributoFuncionalidad atributo(Atributo atributo) {
        this.setAtributo(atributo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AtributoFuncionalidad)) {
            return false;
        }
        return id != null && id.equals(((AtributoFuncionalidad) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AtributoFuncionalidad{" +
            "id=" + getId() +
            ", marcado='" + getMarcado() + "'" +
            ", valor='" + getValor() + "'" +
            "}";
    }
}
