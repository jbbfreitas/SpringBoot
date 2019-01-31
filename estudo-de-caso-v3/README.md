# Estudo de Caso V3

## 1. O que faremos nesta versão

- Criar uma classe persistente para Departamento
- Atualizar o Liquibase
- Criar uma interface `@Repository` para Departamento
- Utilizar uma classe de serviços para Departamento
- Complementar a classe MunicipioResource com outros métodos do CRUD
- Criar a classe DepartamentoResource
- Tratamento de erro e classes úteis em REST

## 2. Construindo a aplicação estudo-de-caso-v2 com o SpringBoot

### 2.1 -  Criar uma classe persistente para Departamento
::: :walking: Passo a passo :::  

1. Abra o Eclipse no workspace `GrupoDeEstudo/SpringBoot`;

2. Se o projeto estudo-de-caso-v2 não aparecer, adicione-o através de 'Maven|Import';

3. Copie o projeto estudo-de-caso-v2 para estudo-de-caso-v3, usando o 'Copy|Past' do próprio eclipse;

4. Altere o arquivo `pom.xml` conforme Listagem 1:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>br.com.abim.ec</groupId>
	<artifactId>estudo-de-caso-3</artifactId>
	<version>0.3.0-SNAPSHOT</version>
	<name>estudo-de-caso-v3</name>
	<description>Estudo de caso usando Spring Boot</description>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.1.0.RELEASE</version>
		<relativePath /> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>

		<!-- Spring data -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<!-- Postgresql -->
		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<version>9.4-1203-jdbc42</version>
		</dependency>


		<!-- https://mvnrepository.com/artifact/org.apache.commons/commons-lang3 -->
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
			<version>3.0</version>
		</dependency>

        <dependency>
            <groupId>org.zalando</groupId>
            <artifactId>problem-spring-web</artifactId>
            <version>0.24.0-RC.0</version>
        </dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
			<plugin>
				<groupId>org.liquibase</groupId>
				<artifactId>liquibase-maven-plugin</artifactId>
				<version>3.4.2</version>
				<configuration>
					<propertyFile>config/liquibase/liquibase.yml</propertyFile>
					<changeLogFile>config/liquibase/master.xml</changeLogFile>
				</configuration>
			</plugin>

		</plugins>
	</build>


</project>
```

<p align="center">
   <strong>Listagem 1- Arquivo pom.xml da aplicação</strong> 
</p>
::: :pushpin: Importante :::

>Além do nome da aplicação e da versão, foi adicionada a depeência `problem-spring-web` que será utilizada em 2.7-Fazer tratamento de erro no REST .

5. No pacote `br.com.abim.ec.domain` cria a classe `Departamento.java` conforme Listagem 2. Essa classe tem diversas anotações importantes que serão detalhadas logo abaixo.

```java
package br.com.abim.ec.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Departamento.
 */
@Entity
@Table(name = "departamento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Departamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "hibernate_sequence")
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
    @JsonIgnoreProperties("")
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

```

<p align="center">
   <strong>Listagem 2- Classe Departamento.java</strong> 
</p>

::: :pushpin: Importante :::


> Observe as anotações abaixo e entenda o significado de cada uma delas.
```java
1 @NotNull
2    @Size(min = 10, max = 50)
3    @Column(name = "nome_departamento", length = 50, nullable = false)
4    private String nomeDepartamento;
```

(1) -`@NotNul` esta anotação faz parte do pacote `javax.validation.constraints` e informa ao java para lançar um exceção sempre que o valor do atributo anotado for nulo.

(2) - `@Size(min = 10, max = 50)` esta anotação também faz parte do pacote `javax.validation.constraints` e é uma restrição de tamanho mínimo e máximo para valores que serão associados ao atributo. Neste caso o valor deve estar entre  10 caracteres e 50 caracteres.

(3) - `@Column(name = "nome_departamento", length = 50, nullable = false)`esta é uma anotação que está no pactoe `javax.persistence` e que é específica para a coluna no banco de dados.

> Observe também as anotações abaixo e entenda o significado de cada uma delas.

```java
1    @ManyToOne
2    @JsonIgnoreProperties("")
3    private Municipio municipio;
```

(1) -`@ManyToOne` - Significa que muitas instâncias de Departamento estão associadas a um Município

(2) - `@JsonIgnoreProperties` Faz parte do pacote `com.fasterxml.jackson.annotation` e serve para ignorar as propriedades listadas tanto no processo de serialização (transformação em JSON) quanto no processo de deserialização (transformação em objetos java). No caso em tela nenhuma propriedade será ignorada.

(3) - Referência à classe Município.

6. Na pasta `src/resources/config/liquibase`, altere o arquivo `master.xml` para ficar de acordo com o Listagem 3.

```xml
<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd">

    <include file="config/liquibase/changelog/00000000000000_initial_schema.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20181223125423_added_entity_Municipio.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20181223130816_added_entity_Departamento.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20181223130816_added_entity_constraints_Departamento.xml" relativeToChangelogFile="false"/>
</databaseChangeLog>

```
<p align="center">
   <strong>Listagem 3- master.xml</strong> 
</p>

>Esse arquivo agora inclui duas novoas linhas: uma para a criação da entidade `Departamento` e outra  para a `constraint` que associa o `Departamento` ao `Municipio`.

7. Na pasta `src/resources/config/liquibase/changelog`, crie  dois arquivos, Listagens 4 e 5.

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
        Added the entity Departamento.
    -->
    <changeSet id="20181223130816-1" author="jbbf">
        <createTable tableName="departamento">
            <column name="id" type="bigint" autoIncrement="${autoIncrement}">
                <constraints primaryKey="true" nullable="false"/>
            </column>
            <column name="nome_departamento" type="varchar(50)">
                <constraints nullable="false" />
            </column>

            <column name="sigla_departamento" type="varchar(5)">
                <constraints nullable="false" />
            </column>

            <column name="cnpj" type="varchar(14)">
                <constraints nullable="true" />
            </column>

            <column name="municipio_id" type="bigint">
                <constraints nullable="true" />
            </column>
        </createTable>
        
    </changeSet>
</databaseChangeLog>

```
<p align="center">
   <strong>Listagem 4- 20181223130816_added_entity_Departamento.xml</strong> 
</p>

```xml
<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd">
    <!--
        Added the constraints for entity Departamento.
    -->
    <changeSet id="20181223130816-2" author="jbbf">
        
        <addForeignKeyConstraint baseColumnNames="municipio_id"
                                 baseTableName="departamento"
                                 constraintName="fk_departamento_municipio_id"
                                 referencedColumnNames="id"
                                 referencedTableName="municipio"/>

    </changeSet>
</databaseChangeLog>

```
<p align="center">
   <strong>Listagem 5- 20181223130816_added_entity_constraints_Departamento.xml</strong> 
</p>

### 2.3 -   Criar uma interface `@Repository` para Departamento


> Como era de se esperar a anotação `@Repository` é a principal interface do `Spring Data Repository`. Ela recebe como parâmetro  a classe de domínio que será gerenciada (Departamento no nosso caso), bem como o tipo de identificação dessa classe (no nosso caso um id do tipo Long). Essa interface atua principalmente como uma interface de marcação para capturar os tipos com os quais trabalhar e para descobrir as interfaces que a estendem. Uma dessas interfaces é a `CrudRepository` que fornece funcionalidade CRUD (incluir, alterar, excluir e selecionar) sofisticada para a classe de entidade que está sendo gerenciada.(Tradução livre de 
[docs.spring.io](https://docs.spring.io/spring-data/data-commons/docs/2.1.3.RELEASE/reference/html/#repositories))

> Os repositórios são ferramentas poderosissimas do Spring pois permitem o uso de `Query Language` para estender os métodos `CRUD` inicialmente providos. 

::: :walking: Passo a passo :::  

1. No pacote `br.com.abim.ec.repository` crie a classe `DepartamentoRepository.java` conforme Listagem 6.

```java
package br.com.abim.ec.repository;

import br.com.abim.ec.domain.Departamento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Departamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

}
```
<p align="center">
   <strong>Listagem 6- Interface DepartamentoRepository.java</strong> 
</p>

::: :pushpin: Importante :::

> Observe que desta vez, estamos criando uma `interface` e não um `classe`. Essa interface é descendente de `JpaRepository` que, logicamente, é também uma interface (que usa `Generics`- observe a instrução `<Departamento, Long>` ). Você vai notar que não existe uma classe que implementa essa interface. Na verdade essa classe só vai existir em tempo de execução e será injetada pelo Spring (via Bean Factory) sempre que ele encontrar algo como:

```java
    @Autowired
    private DepartamentoRepository departamentoRepository;
```

> Essa é, essencialmente, a técnica denominada `programar por interfaces` que traz, dentre outros benefícios, o baixo acoplamento e a alta coesão. Para saber mais sobre a coesão consulte 
[Alta Coesão](http://www.dsc.ufcg.edu.br/~jacques/cursos/map/html/pat/altacoesao.htm).

> Veja também os padrões [GRASP](https://pt.wikipedia.org/wiki/GRASP_\(padr%C3%A3o_orientado_a_objetos\)) propostos por Craig Larman.

2. Pronto. Com isso já podemos usar os métodos providos por padrão e, se for necessário, poderemos estender a interface usando `Query Language`.

#### Desafio 1 :innocent:
 
```
- Baseando-se nos exemplos providos em V2, crie uma `Query` para encontrar um Departamento pelo Nome.
```
### 2.4 -   Utilizar uma classe de serviços para Departamento


#### Lembra-se que  enumeramos em V2 as razões para usar a camada de serviços:

1. Fornece separação de interesses
2. Fornece Segurança
3. Baixo acoplamento, através da indireção


::: :pushpin: Importante :::

> É importante lembrar também que na arquitetura em camadas proposta pelo SpringBoot, a camada `service` localiza-se entre a camada `repository` e `controller`

::: :walking: Passo a passo :::  

1. No pacote `br.com.abim.ec.service`, crie a interface denominada `DepartamentoService.java`, conforme Listagem 7 e a classe que a implementa, Listagem 8.

```java
package br.com.abim.ec.service;

import br.com.abim.ec.domain.Departamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Departamento.
 */
public interface DepartamentoService {

    /**
     * Save a departamento.
     *
     * @param departamento the entity to save
     * @return the persisted entity
     */
    Departamento save(Departamento departamento);

    /**
     * Get all the departamentos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Departamento> findAll(Pageable pageable);


    /**
     * Get the "id" departamento.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Departamento> findOne(Long id);

    /**
     * Delete the "id" departamento.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
```
<p align="center">
   <strong>Listagem 7- Interface DepartamentoService.java</strong> 
</p>

#### Desafio 2 :innocent:
 
```
- Inclua um método para encontrar um Departamento pelo Nome, conforme sugerido no Desafio 1.
```

```java
package br.com.abim.ec.service.impl;

import br.com.abim.ec.service.DepartamentoService;
import br.com.abim.ec.domain.Departamento;
import br.com.abim.ec.repository.DepartamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Departamento.
 */
@Service
@Transactional
public class DepartamentoServiceImpl implements DepartamentoService {

    private final Logger log = LoggerFactory.getLogger(DepartamentoServiceImpl.class);

    private DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    /**
     * Save a departamento.
     *
     * @param departamento the entity to save
     * @return the persisted entity
     */
    @Override
    public Departamento save(Departamento departamento) {
        log.debug("Request to save Departamento : {}", departamento);
        return departamentoRepository.save(departamento);
    }

    /**
     * Get all the departamentos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Departamento> findAll(Pageable pageable) {
        log.debug("Request to get all Departamentos");
        return departamentoRepository.findAll(pageable);
    }


    /**
     * Get one departamento by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Departamento> findOne(Long id) {
        log.debug("Request to get Departamento : {}", id);
        return departamentoRepository.findById(id);
    }

    /**
     * Delete the departamento by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Departamento : {}", id);
        departamentoRepository.deleteById(id);
    }
}

```
<p align="center">
   <strong>Listagem 8- Classe DepartamentoServiceImpl.java</strong> 
</p>

#### Desafio 3:innocent:
 
```
- Implemente o método para encontrar um Departamento pelo Nome, criado no Desafio 2.
```

### 2.6 -   Complementar a classe MunicipioResource com outros métodos do CRUD

> Lembre-se que em V2 fizemos apenas um método na classe `MunicípioResource`? Agora vamos completar essa classe com os demais métdos, vide Listagem 9.

```java
package br.com.abim.ec.web.rest;

import br.com.abim.ec.domain.Municipio;
import br.com.abim.ec.service.MunicipioService;
import br.com.abim.ec.web.rest.errors.BadRequestAlertException;
import br.com.abim.ec.web.rest.util.HeaderUtil;
import br.com.abim.ec.web.rest.util.PaginationUtil;
import br.com.abim.ec.web.rest.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Municipio.
 */
@RestController
@RequestMapping("/api")
public class MunicipioResource {

    private final Logger log = LoggerFactory.getLogger(MunicipioResource.class);

    private static final String ENTITY_NAME = "municipio";

    private MunicipioService municipioService;

    public MunicipioResource(MunicipioService municipioService) {
        this.municipioService = municipioService;
    }

    /**
     * POST  /municipios : Create a new municipio.
     *
     * @param municipio the municipio to create
     * @return the ResponseEntity with status 201 (Created) and with body the new municipio, or with status 400 (Bad Request) if the municipio has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/municipios")
    public ResponseEntity<Municipio> createMunicipio(@RequestBody Municipio municipio) throws URISyntaxException {
        log.debug("REST request to save Municipio : {}", municipio);
        if (municipio.getId() != null) {
            throw new BadRequestAlertException("A new municipio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Municipio result = municipioService.save(municipio);
        return ResponseEntity.created(new URI("/api/municipios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /municipios : Updates an existing municipio.
     *
     * @param municipio the municipio to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated municipio,
     * or with status 400 (Bad Request) if the municipio is not valid,
     * or with status 500 (Internal Server Error) if the municipio couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/municipios")
    public ResponseEntity<Municipio> updateMunicipio(@RequestBody Municipio municipio) throws URISyntaxException {
        log.debug("REST request to update Municipio : {}", municipio);
        if (municipio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Municipio result = municipioService.save(municipio);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, municipio.getId().toString()))
            .body(result);
    }

    /**
     * GET  /municipios : get all the municipios.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of municipios in body
     */
    @GetMapping("/municipios")
    public ResponseEntity<List<Municipio>> getAllMunicipios(Pageable pageable) {
        log.debug("REST request to get a page of Municipios");
        Page<Municipio> page = municipioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/municipios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /municipios/:id : get the "id" municipio.
     *
     * @param id the id of the municipio to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the municipio, or with status 404 (Not Found)
     */
    @GetMapping("/municipios/{id}")
    public ResponseEntity<Municipio> getMunicipio(@PathVariable Long id) {
        log.debug("REST request to get Municipio : {}", id);
        Optional<Municipio> municipio = municipioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(municipio);
    }

    /**
     * DELETE  /municipios/:id : delete the "id" municipio.
     *
     * @param id the id of the municipio to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/municipios/{id}")
    public ResponseEntity<Void> deleteMunicipio(@PathVariable Long id) {
        log.debug("REST request to delete Municipio : {}", id);
        municipioService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
````
<p align="center">
   <strong>Listagem 9- Classe MunicipioResource.java</strong> 
</p>


> Vamos comentar as instruções mais importantes nesta classe:
```java
1    @PostMapping("/municipios")
2    public ResponseEntity<Municipio> createMunicipio(@RequestBody Municipio municipio) throws URISyntaxException {
        log.debug("REST request to save Municipio : {}", municipio);
        if (municipio.getId() != null) {
3            throw new BadRequestAlertException("A new municipio cannot already have an ID", ENTITY_NAME, "idexists");
        }
4        Municipio result = municipioService.save(municipio);
5        return ResponseEntity.created(new URI("/api/municipios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
```


(1)- `@PostMapping("/municipios")` - Essa anotação sinaliza que esse método será o responsável por tratar as requisições que oriundas do cliente que estejam utilizando o método `HTTP.POST` e que tenham como URL algo como `http://<servidor>/api/municipios`

::: :pushpin: Importante :::

> Observe que cada método da classe Resource tem dois indicadores que o selecionam como o método que será utilizado: o método HTTP da requisição (POST, GET, DELETE) e a URL. Esses dois indicadores devem ser unívocos para cada contexto da aplicação.

(2)- `public ResponseEntity<Municipio> createMunicipio(@RequestBody Municipio municipio) throws URISyntaxException {`
Este método retorna um `ResponseEntity`, já comentado em V2, e tem no seu parâmetro a anotação `@RequestBody` que sinaliza o corpo da requisição (body) contém uma instância da classe `Município`. A conversão de um objeto JSON (Município) para um objeto java é realiza implicitamente pela anotação. Esse método também lança (throws) uma exceção do tipo `URISyntaxException`, que como o próprio nome indica será lançada sempre que houver problemas de sintaxe na URI( __Uniform Resource Identifier__)

(3)- Como esse método está criando uma nova instância de `Municipio` o `id` tem que ser nulo, visto que esse identificado deverá ser provido pelo banco de dados. Se o `ìd` não for nulo, significa que é uma atualização de uma instância já existente, por isso o lançanento da exceção `BadRequestAlertException`. Veja mais adiante os detalhes dessa exceção.

(4)-Invoca o método `save()` da camada de serviços. Aqui é importante notar a independência de camadas. Observe que a camada `Controller` (classe MunicipioResource), desconhece completamente a camada DAO(Repository), tendo como dependência apenas a camada de serviços.

(5)-Esse retorno é um pouco complexo e por isso será explicado em partes:

- Primeiramente é invocado o método `created()` da classe estática `ResponseEntity`. 
- Esse método recebe como parâmetro uma URI e retorna um `builder`(Padrão Factory Method). 
- O `builder` retornado, por sua vez, invoca o método `headers` para ajustar o cabeçalho do `ResponseEntity`.
- Em seguida, o retorno do método `headers` invoca o método `body` para ajustar o conteúdo do retorno. 

::: :pushpin: Importante :::

> Como pode ser facilmente verificado existe um `pipeline` de métodos indicando que em (5) temos uma implementação que usa o [`Decorator Pattern`](https://www.hojjatk.com/2012/11/decorator-design-pattern.html) bem como o [`Chain of Responsability Pattern`](https://www.hojjatk.com/2012/11/chain-of-responsibility-pipeline-design.html). 

>Parece meio confuso, não é mesmo? Não se preocupe com isso ainda, no fundo o que temos aqui é o retorno para uma solicitação sob a forma de um `ResponseEntity` contendo o cabeçalho e um corpo. O tratamento que será dado a esse retorno fica por conta do cliente (Angular!).

Os outros métodos da classe `MunicipioResource` são muito semelhantes variando apenas na anotação referente ao método HTTP e, claro, a implementação conforme cada caso. Como não há nada de novo aqui, deixo para o leitor examinar, por conta própria, cada um desses métodos.

### 2.7 -   Criar a classe DepartamentoResource

Esta classe é muito semelhante ("__mutatis mutandis__") à recém criada `MunicipioResource`  e, por isso, vou me limitar a trazer a Listagem 10.

```java
package br.com.abim.ec.web.rest;

import br.com.abim.ec.domain.Departamento;
import br.com.abim.ec.service.DepartamentoService;
import br.com.abim.ec.web.rest.errors.BadRequestAlertException;
import br.com.abim.ec.web.rest.util.HeaderUtil;
import br.com.abim.ec.web.rest.util.PaginationUtil;
import br.com.abim.ec.web.rest.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Departamento.
 */
@RestController
@RequestMapping("/api")
public class DepartamentoResource {

    private final Logger log = LoggerFactory.getLogger(DepartamentoResource.class);

    private static final String ENTITY_NAME = "departamento";

    private DepartamentoService departamentoService;

    public DepartamentoResource(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    /**
     * POST  /departamentos : Create a new departamento.
     *
     * @param departamento the departamento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new departamento, or with status 400 (Bad Request) if the departamento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/departamentos")
    public ResponseEntity<Departamento> createDepartamento(@Valid @RequestBody Departamento departamento) throws URISyntaxException {
        log.debug("REST request to save Departamento : {}", departamento);
        if (departamento.getId() != null) {
            throw new BadRequestAlertException("A new departamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Departamento result = departamentoService.save(departamento);
        return ResponseEntity.created(new URI("/api/departamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /departamentos : Updates an existing departamento.
     *
     * @param departamento the departamento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated departamento,
     * or with status 400 (Bad Request) if the departamento is not valid,
     * or with status 500 (Internal Server Error) if the departamento couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/departamentos")
    public ResponseEntity<Departamento> updateDepartamento(@Valid @RequestBody Departamento departamento) throws URISyntaxException {
        log.debug("REST request to update Departamento : {}", departamento);
        if (departamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Departamento result = departamentoService.save(departamento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, departamento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /departamentos : get all the departamentos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of departamentos in body
     */
    @GetMapping("/departamentos")
    public ResponseEntity<List<Departamento>> getAllDepartamentos(Pageable pageable) {
        log.debug("REST request to get a page of Departamentos");
        Page<Departamento> page = departamentoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/departamentos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /departamentos/:id : get the "id" departamento.
     *
     * @param id the id of the departamento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the departamento, or with status 404 (Not Found)
     */
    @GetMapping("/departamentos/{id}")
    public ResponseEntity<Departamento> getDepartamento(@PathVariable Long id) {
        log.debug("REST request to get Departamento : {}", id);
        Optional<Departamento> departamento = departamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departamento);
    }

    /**
     * DELETE  /departamentos/:id : delete the "id" departamento.
     *
     * @param id the id of the departamento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/departamentos/{id}")
    public ResponseEntity<Void> deleteDepartamento(@PathVariable Long id) {
        log.debug("REST request to delete Departamento : {}", id);
        departamentoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}


```

<p align="center">
   <strong>Listagem 10- Classe DepartamentoResource.java</strong> 
</p>


### 2.8 -   Tratamento de erro e classes úteis em REST

Como havíamos prometido anteriormente, vamos falar sobre o tratamento de erros que podem surgir na execução dos métodos das classes `Resource` e também sobre 3 classes utilitárias que irão facilitar o nosso trabalho.

Primeiro a classe tratamento de erros.

1. No pacote `br.com.abim.ec.web.rest.errors` crie a classe da Listagem 11.

```java

package br.com.abim.ec.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BadRequestAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;
    public static final URI DEFAULT_TYPE = URI.create("http://localhost:8090" + "/problem-with-message");


    public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
        this(DEFAULT_TYPE, defaultMessage, entityName, errorKey);
    }

    public BadRequestAlertException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(type, defaultMessage, Status.BAD_REQUEST, null, null, null, getAlertParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }
}

```
<p align="center">
   <strong>Listagem 11- Classe BadRequestAlertException.java</strong> 
</p

>Essa é, basicamente, uma classe para tratamento de erros que interage com o usuário, lançando uma mensagem que informa o ocorrido, como por exemplo em : 

```java
throw new BadRequestAlertException("Um novo município não pode possuir um ID não nulo", ENTITY_NAME, "idexists");
```

`idexists` é apenas uma chave para a mensagem que está associada a ela, neste caso `Um novo município não pode possuir um ID não nulo`. É interessante termos códigos de erros associados para uma rápida identificação, pelos Analistas, do erro que ocorreu.

Agora as classes utilitárias.

2. No pacote `br.com.abim.ec.web.rest.util` crie as classes das Listagens 12,13 e 14.

```java
package br.com.abim.ec.web.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 */
public final class HeaderUtil {

    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private static final String APPLICATION_NAME = "estudoDeCasoApp";

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-alert", message);
        headers.add("X-" + APPLICATION_NAME + "-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".deleted", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-error", "error." + errorKey);
        headers.add("X-" + APPLICATION_NAME + "-params", entityName);
        return headers;
    }
}

```
<p align="center">
   <strong>Listagem 12- Classe HeaderUtil.java</strong> 
</p>

```java
package br.com.abim.ec.web.rest.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;


public final class PaginationUtil {

    private PaginationUtil() {
    }

    public static <T> HttpHeaders generatePaginationHttpHeaders(Page<T> page, String baseUrl) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        String link = "";
        if ((page.getNumber() + 1) < page.getTotalPages()) {
            link = "<" + generateUri(baseUrl, page.getNumber() + 1, page.getSize()) + ">; rel=\"next\",";
        }
        // prev link
        if ((page.getNumber()) > 0) {
            link += "<" + generateUri(baseUrl, page.getNumber() - 1, page.getSize()) + ">; rel=\"prev\",";
        }
        // last and first link
        int lastPage = 0;
        if (page.getTotalPages() > 0) {
            lastPage = page.getTotalPages() - 1;
        }
        link += "<" + generateUri(baseUrl, lastPage, page.getSize()) + ">; rel=\"last\",";
        link += "<" + generateUri(baseUrl, 0, page.getSize()) + ">; rel=\"first\"";
        headers.add(HttpHeaders.LINK, link);
        return headers;
    }

    private static String generateUri(String baseUrl, int page, int size) {
        return UriComponentsBuilder.fromUriString(baseUrl).queryParam("page", page).queryParam("size", size).toUriString();
    }
}

```

<p align="center">
   <strong>Listagem 13- Classe PaginationUtil.java</strong> 
</p>

```java

package br.com.abim.ec.web.rest.util;
import java.util.Optional;
import org.springframework.http.*;
/**
 * Utility class for ResponseEntity creation.
 */
public final class ResponseUtil {

    private ResponseUtil() {
    }

    /**
     * Wrap the optional into a {@link ResponseEntity} with an {@link HttpStatus#OK} status, or if it's empty, it
     * returns a {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND}.
     *
     * @param            type of the response
     * @param maybeResponse response to return if present
     * @return response containing {@code maybeResponse} if present or {@link HttpStatus#NOT_FOUND}
     */
    public static  ResponseEntity wrapOrNotFound(Optional maybeResponse) {
        return wrapOrNotFound(maybeResponse, null);
    }

    /**
     * Wrap the optional into a {@link ResponseEntity} with an {@link HttpStatus#OK} status with the headers, or if it's
     * empty, it returns a {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND}.
     *
     * @param            type of the response
     * @param maybeResponse response to return if present
     * @param header        headers to be added to the response
     * @return response containing {@code maybeResponse} if present or {@link HttpStatus#NOT_FOUND}
     */
    public static  ResponseEntity wrapOrNotFound(Optional maybeResponse, HttpHeaders header) {
        return (ResponseEntity) maybeResponse.map(response -> ResponseEntity.ok().headers(header).body(response))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

```

<p align="center">
   <strong>Listagem 14- Classe ResponseUtil.java</strong> 
</p>

>Não vamos nos preocupar muito agora com essas 3 classes. Basta por hora saber que são classes utilitárias que são utilizadas na resposta, permitindo que alguma informação seja capturada pelo cliente Angular. Podemos adiantar que são informações que ajudarão no processo de paginação, e na comunicação com o usuário final. 

Passe agora para a implementação [V4](../estudo-de-caso-v4/README.md) do nosso Estudo de Caso.

