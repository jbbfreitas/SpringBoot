package br.com.abim.ec.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import br.com.abim.ec.domain.enumeration.Estado;

/**
 * Municipio.
 */
@Entity
@Table(name = "municipio")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Municipio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "hibernate_sequence")
    private Long id;
    

    @Column(name = "nome_municipio")
    private String nomeMunicipio;

    @Enumerated(EnumType.STRING)
    @Column(name = "uf")
    private Estado uf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeMunicipio() {
        return nomeMunicipio;
    }

    public Municipio nomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = nomeMunicipio;
        return this;
    }

    public void setNomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = nomeMunicipio;
    }

    public Estado getUf() {
        return uf;
    }

    public Municipio uf(Estado uf) {
        this.uf = uf;
        return this;
    }

    public void setUf(Estado uf) {
        this.uf = uf;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Municipio municipio = (Municipio) o;
        if (municipio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), municipio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Municipio{" +
            "id=" + getId() +
            ", nomeMunicipio='" + getNomeMunicipio() + "'" +
            ", uf='" + getUf() + "'" +
            "}";
    }
}