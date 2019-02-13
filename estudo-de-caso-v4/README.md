# Estudo de Caso V4

## 1. O que você fará nesta versão

- Criar uma classe persistente para Empregado
- Atualizar o Liquibase
- Criar uma interface `@Repository` para Empregado
- Utilizar uma classe de serviços para Empregado
- Criar a classe EmpregadoResource

## 2. Construindo a aplicação estudo-de-caso-v4 com o SpringBoot

::: :walking: Passo a passo :::  

    Abra o Eclipse no workspace GrupoDeEstudo/SpringBoot;

    Se o projeto estudo-de-caso-v3 não aparecer, adicione-o através de 'Maven|Import';

    Copie o projeto estudo-de-caso-v3 para estudo-de-caso-v4, usando o 'Copy|Past' do próprio eclipse;

#### Desafio 1 :innocent:

### 2.1 -  Criar uma classe persistente para Empregado com base no diagrama UML da Figura 1


<p align="center">
  <img src="/Imagens/UMLEmpregado.png" alt="Diagrama parcial da Classe Empregado">
</p>
<p align="center">
   <strong>Figura 1- Diagrama parcial da Classe Empregado</strong> 
</p>


### 2.2 -  Atualizar o Liquibase

1. Na pasta `src/resources/config/liquibase`, altere o arquivo `master.xml` para ficar de acordo com o Listagem 1.

```xml
<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd">

    <include file="config/liquibase/changelog/00000000000000_initial_schema.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20181223125423_added_entity_Municipio.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20181223130816_added_entity_Departamento.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20181223134100_added_entity_Empregado.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20181223130816_added_entity_constraints_Departamento.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20181223134100_added_entity_constraints_Empregado.xml" relativeToChangelogFile="false"/>
</databaseChangeLog>


```
<p align="center">
   <strong>Listagem 1- master.xml</strong> 
</p>

>Esse arquivo agora inclui duas novas linhas: uma para a criação da entidade `Empregado` e outra  para a `constraint` que associa o `Empregado` ao `Departamento`.

2. Na pasta `src/resources/config/liquibase/changelog`, crie  dois arquivos, Listagens 2 e 3.

```xml
<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd
                        http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd">

    <property name="now" value="now()" dbms="h2"/>

    <property name="now" value="current_timestamp" dbms="postgresql"/>

    <property name="floatType" value="float4" dbms="postgresql, h2"/>
    <property name="floatType" value="float" dbms="mysql, oracle, mssql"/>

    <!--
        Added the entity Empregado.
    -->
    <changeSet id="20181223134100-1" author="jbbf">
        <createTable tableName="empregado">
            <column name="id" type="bigint" autoIncrement="${autoIncrement}">
                <constraints primaryKey="true" nullable="false"/>
            </column>
            <column name="nome_empregado" type="varchar(80)">
                <constraints nullable="false" />
            </column>

            <column name="data_nascimento" type="date">
                <constraints nullable="false" />
            </column>

            <column name="cpf" type="varchar(11)">
                <constraints nullable="false" />
            </column>

            <column name="data_admissao" type="date">
                <constraints nullable="false" />
            </column>

            <column name="data_demissao" type="date">
                <constraints nullable="true" />
            </column>

            <column name="data_obito" type="date">
                <constraints nullable="true" />
            </column>

            <column name="municipio_nascimento_id" type="bigint">
                <constraints nullable="true" />
            </column>

            <column name="municipio_residencial_id" type="bigint">
                <constraints nullable="false" />
            </column>

            <column name="departamento_id" type="bigint">
                <constraints nullable="false" />
            </column>
        </createTable>
        
    </changeSet>
</databaseChangeLog>

```
<p align="center">
   <strong>Listagem 2- 20181223134100_added_entity_Empregado.xml</strong> 
</p>

```xml
<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd">
    <!--
        Added the constraints for entity Empregado.
    -->
    <changeSet id="20181223134100-2" author="jbbf">
        
        <addForeignKeyConstraint baseColumnNames="municipio_nascimento_id"
                                 baseTableName="empregado"
                                 constraintName="fk_empregado_municipio_nascimento_id"
                                 referencedColumnNames="id"
                                 referencedTableName="municipio"/>

        <addForeignKeyConstraint baseColumnNames="municipio_residencial_id"
                                 baseTableName="empregado"
                                 constraintName="fk_empregado_municipio_residencial_id"
                                 referencedColumnNames="id"
                                 referencedTableName="municipio"/>

        <addForeignKeyConstraint baseColumnNames="departamento_id"
                                 baseTableName="empregado"
                                 constraintName="fk_empregado_departamento_id"
                                 referencedColumnNames="id"
                                 referencedTableName="departamento"/>

    </changeSet>
</databaseChangeLog>


```
<p align="center">
   <strong>Listagem 3- 20181223134100_added_entity_constraints_Empregado.xml</strong> 
</p>

#### Desafio 2 :innocent:

### 2.3 -   Criar uma interface `@Repository` para Empregado

#### Desafio 3 :innocent:

### 2.4 -   Utilizar uma classe de serviços para Empregado

#### Desafio 4 :innocent:

### 2.5 -   Criar a classe EmpregadoResource

## 3. -  Atualizar o Liquibase para retirar constraint notnull

1. Na pasta `src/resources/config/liquibase`, altere o arquivo `master.xml` para ficar de acordo com o Listagem 4.

```xml
<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd">

    <include file="config/liquibase/changelog/00000000000000_initial_schema.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20181223125423_added_entity_Municipio.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20181223130816_added_entity_Departamento.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20181223134100_added_entity_Empregado.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20181223130816_added_entity_constraints_Departamento.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20181223134100_added_entity_constraints_Empregado.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20190213200300_added_entity_remove_constraints_Empregado.xml" relativeToChangelogFile="false"/>
 
</databaseChangeLog>



```
<p align="center">
   <strong>Listagem 4- master.xml</strong> 
</p>

>Esse arquivo agora inclui uma nova linha para retirar a constraint da entidade `Empregado`.

2. Na pasta `src/resources/config/liquibase/changelog`, crie  um novo arquivo, Listagem 5.

```xml
<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog
	xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd">
	<changeSet author="jbbf"
		id="201902132000300-2">
		<dropNotNullConstraint columnDataType="date" columnName="data_nascimento" schemaName="public" tableName="empregado" />
	</changeSet>
	<changeSet author="jbbf"
		id="201902132005300-2">
		<dropNotNullConstraint columnDataType="date" columnName="data_admissao" schemaName="public" tableName="empregado" />
		<dropNotNullConstraint columnDataType="bigint" columnName="municipio_residencial_id" schemaName="public" tableName="empregado" />
		<dropNotNullConstraint columnDataType="bigint" columnName="departamento_id" schemaName="public" tableName="empregado" />
		<dropNotNullConstraint columnDataType="varchar(11)" columnName="cpf" schemaName="public" tableName="empregado" />
	</changeSet>


</databaseChangeLog>


```
<p align="center">
   <strong>Listagem 5- 20190213200300_added_entity_remove_constraints_Empregado.xml</strong> 
</p>

2. No pacote  `br.com.abim.ec.domain`, altere a classe `Empregado`, Listagem 6.


```java
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
//comentário
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



```
<p align="center">
   <strong>Listagem 6- Empregado.java</strong> 
</p>


3. Se apliacação estiver sendo executada, interrompa-a.
4. Execute o liquibase para refletir as alterações no banco de dados.

> mvn liquibase:update

5. Verifique no Postgres se as alterações surtiram o efeito desejado