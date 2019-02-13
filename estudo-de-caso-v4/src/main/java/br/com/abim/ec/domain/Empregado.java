package br.com.abim.ec.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Empregado.
 */
@Entity
@Table(name = "empregado")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Empregado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator",sequenceName="hibernate_sequence")
    private Long id;

    @NotNull
    @Size(min = 10, max = 80)
    @Column(name = "nome_empregado", length = 80, nullable = false)
    private String nomeEmpregado;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Size(min = 11, max = 11)
    @Column(name = "cpf", length = 11 )
    private String cpf;

    @Column(name = "data_admissao")
    private LocalDate dataAdmissao;

    @Column(name = "data_demissao")
    private LocalDate dataDemissao;

    @Column(name = "data_obito")
    private LocalDate dataObito;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Municipio municipioNascimento;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Municipio municipioResidencial;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Departamento departamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeEmpregado() {
        return nomeEmpregado;
    }

    public Empregado nomeEmpregado(String nomeEmpregado) {
        this.nomeEmpregado = nomeEmpregado;
        return this;
    }

    public void setNomeEmpregado(String nomeEmpregado) {
        this.nomeEmpregado = nomeEmpregado;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public Empregado dataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
        return this;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public Empregado cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public Empregado dataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
        return this;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public LocalDate getDataDemissao() {
        return dataDemissao;
    }

    public Empregado dataDemissao(LocalDate dataDemissao) {
        this.dataDemissao = dataDemissao;
        return this;
    }

    public void setDataDemissao(LocalDate dataDemissao) {
        this.dataDemissao = dataDemissao;
    }

    public LocalDate getDataObito() {
        return dataObito;
    }

    public Empregado dataObito(LocalDate dataObito) {
        this.dataObito = dataObito;
        return this;
    }

    public void setDataObito(LocalDate dataObito) {
        this.dataObito = dataObito;
    }

    public Municipio getMunicipioNascimento() {
        return municipioNascimento;
    }

    public Empregado municipioNascimento(Municipio municipio) {
        this.municipioNascimento = municipio;
        return this;
    }

    public void setMunicipioNascimento(Municipio municipio) {
        this.municipioNascimento = municipio;
    }

    public Municipio getMunicipioResidencial() {
        return municipioResidencial;
    }

    public Empregado municipioResidencial(Municipio municipio) {
        this.municipioResidencial = municipio;
        return this;
    }

    public void setMunicipioResidencial(Municipio municipio) {
        this.municipioResidencial = municipio;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public Empregado departamento(Departamento departamento) {
        this.departamento = departamento;
        return this;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
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
        Empregado empregado = (Empregado) o;
        if (empregado.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), empregado.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Empregado{" +
            "id=" + getId() +
            ", nomeEmpregado='" + getNomeEmpregado() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", dataAdmissao='" + getDataAdmissao() + "'" +
            ", dataDemissao='" + getDataDemissao() + "'" +
            ", dataObito='" + getDataObito() + "'" +
            "}";
    }
}
