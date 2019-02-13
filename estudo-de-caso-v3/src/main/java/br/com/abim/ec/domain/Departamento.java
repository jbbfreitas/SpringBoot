package br.com.abim.ec.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Departamento.
 */
@Entity
@Table(name = "departamento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@JsonIgnoreProperties({"municipio"})
public class Departamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator",sequenceName="hibernate_sequence")

    private Long id;

    @NotNull
    @Size(min = 10, max = 50)
    @Column(name = "nome_departamento", length = 50, nullable = false)
    private String nomeDepartamento;

    @NotNull
    @Size(min = 5, max = 5)
    @Column(name = "sigla_departamento", length = 5, nullable = false)
    private String siglaDepartamento;

    @Size(min = 14, max = 14)
    @Column(name = "cnpj", length = 14)
    private String cnpj;

    @ManyToOne
//    @JsonIgnore()
    private Municipio municipio;

     public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeDepartamento() {
        return nomeDepartamento;
    }

    public Departamento nomeDepartamento(String nomeDepartamento) {
        this.nomeDepartamento = nomeDepartamento;
        return this;
    }

    public void setNomeDepartamento(String nomeDepartamento) {
        this.nomeDepartamento = nomeDepartamento;
    }

    public String getSiglaDepartamento() {
        return siglaDepartamento;
    }

    public Departamento siglaDepartamento(String siglaDepartamento) {
        this.siglaDepartamento = siglaDepartamento;
        return this;
    }

    public void setSiglaDepartamento(String siglaDepartamento) {
        this.siglaDepartamento = siglaDepartamento;
    }

    public String getCnpj() {
        return cnpj;
    }

    public Departamento cnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public Departamento municipio(Municipio municipio) {
        this.municipio = municipio;
        return this;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Departamento departamento = (Departamento) o;
        if (departamento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), departamento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Departamento{" +
            "id=" + getId() +
            ", nomeDepartamento='" + getNomeDepartamento() + "'" +
            ", siglaDepartamento='" + getSiglaDepartamento() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            "}";
    }
}
