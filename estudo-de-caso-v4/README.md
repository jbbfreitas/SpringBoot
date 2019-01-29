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

