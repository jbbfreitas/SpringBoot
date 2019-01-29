# Estudo de Caso Testes

## 1. O que você fará nesta versão

- Criar testes de integração (camada Controller) para Municipio, Departamento e Empregado usando Mockito.


## 2. Preparando a infraestrutura


::: :walking: Passo a passo :::  

 1.   Abra o Eclipse no workspace GrupoDeEstudo/SpringBoot;

 2.   Se o projeto estudo-de-caso-v4 não aparecer, adicione-o através de 'Maven|Import';

 3.   Copie o projeto estudo-de-caso-v4 para estudo-de-caso-Testes, usando o 'Copy|Past' do próprio eclipse;

 4. Crie a estrutura de pastas conforme a Figura 1 no projeto `estudo-de-caso-Testes`

<p align="center">
  <img src="/Imagens/EstruturaDePastasTestes.png" alt="Estrutura de pastas para os testes">
</p>
<p align="center">
   <strong>Figura 1- Estrutura de pastas para os testes</strong> 
</p>

5. Altere o arquivo `pom.xml` conforme Listagem 1:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>br.com.abim.ec</groupId>
	<artifactId>estudo-de-caso-4</artifactId>
	<version>0.4.0-SNAPSHOT</version>
	<name>estudo-de-caso-v4</name>
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

		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>test</scope>
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

````


<p align="center">
   <strong>Listagem 1- Arquivo pom.xml da aplicação de Testes</strong> 
</p>

> Observe a inclusão da dependência para o `H2 DataBase` bem como a utilização do escopo de testes

```xml
		<dependency>
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>test</scope>
		</dependency>

```

5. Na pasta `config` crie o arquivo `application.yml`, conforme Listagem 2

```yml
spring:
    application:
        name: estudo-de-caso-v4
    cache:
        type: simple
    jpa:
        open-in-view: false
        hibernate:
            ddl-auto: none
            naming:
                physical-strategy: org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
                implicit-strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy 
        database: H2
        show-sql: true
        properties:
            hibernate.dialect: org.hibernate.dialect.H2Dialect
            hibernate.temp.use_jdbc_metadata_defaults: false
            hibernate.id.new_generator_mappings: true
            hibernate.connection.provider_disables_autocommit: true
            hibernate.cache.use_second_level_cache: false
            hibernate.cache.use_query_cache: false
            hibernate.generate_statistics: true
            hibernate.hbm2ddl.auto: create
    datasource:
        type: com.zaxxer.hikari.HikariDataSource
        url: jdbc:h2:mem:EstudoDeCaso;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
        username: 
        password: 
server:
    port: 10344
    address: localhost
logging:
    level:
        ROOT: ERROR
        br.com.abim.ec: ERROR

```
<p align="center">
   <strong>Listagem 2- Arquivo application.yml </strong> 
</p>


## 3. Construindo os testes de integração propriamente ditos

::: :walking: Passo a passo :::  


1. Na pasta `src/test/java` e pacote  `br.com.abim.ec.web.rest` crie a classe `MunicipioResourceIntTest.java` conforme Listagem 3. Não se preocupe que iremos explicar cada uma das instruções importantes.


```java
package br.com.abim.ec.web.rest;

import static br.com.abim.ec.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import br.com.abim.ec.EstudoDeCasoApp;
import br.com.abim.ec.domain.Municipio;
import br.com.abim.ec.domain.enumeration.Estado;
import br.com.abim.ec.repository.MunicipioRepository;
import br.com.abim.ec.service.MunicipioService;
/**
 * Test class for the MunicipioResource REST controller.
 *
 * @see MunicipioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EstudoDeCasoApp.class)
public class MunicipioResourceIntTest {

    private static final String DEFAULT_NOME_MUNICIPIO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_MUNICIPIO = "BBBBBBBBBB";
    private static final Estado DEFAULT_UF = Estado.MT;
    private static final Estado UPDATED_UF = Estado.SP;

    @Autowired
    private MunicipioRepository municipioRepository;
    
    @Autowired
    private MunicipioService municipioService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;


    @Autowired
    private EntityManager em;

    private MockMvc restMunicipioMockMvc;

    private Municipio municipio;

    @Before
    public void setup() {
        final MunicipioResource municipioResource = new MunicipioResource(municipioService);
        this.restMunicipioMockMvc = MockMvcBuilders.standaloneSetup(municipioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Municipio createEntity(EntityManager em) {
        Municipio municipio = new Municipio()
            .nomeMunicipio(DEFAULT_NOME_MUNICIPIO)
            .uf(DEFAULT_UF);
        return municipio;
    }

    @Before
    public void initTest() {
        municipio = createEntity(em);
    }

    @Test
    @Transactional
    public void createMunicipio() throws Exception {
        int databaseSizeBeforeCreate = municipioRepository.findAll().size();

        // Create the Municipio
        restMunicipioMockMvc.perform(post("/api/municipios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(municipio)))
            .andExpect(status().isCreated());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeCreate + 1);
        Municipio testMunicipio = municipioList.get(municipioList.size() - 1);
        assertThat(testMunicipio.getNomeMunicipio()).isEqualTo(DEFAULT_NOME_MUNICIPIO);
        assertThat(testMunicipio.getUf()).isEqualTo(DEFAULT_UF);
    }


    @Test
    @Transactional
    public void getAllMunicipios() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get all the municipioList
        restMunicipioMockMvc.perform(get("/api/municipios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(municipio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeMunicipio").value(hasItem(DEFAULT_NOME_MUNICIPIO.toString())))
            .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF.toString())));
    }
    
    @Test
    @Transactional
    public void getMunicipio() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get the municipio
        restMunicipioMockMvc.perform(get("/api/municipios/{id}", municipio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(municipio.getId().intValue()))
            .andExpect(jsonPath("$.nomeMunicipio").value(DEFAULT_NOME_MUNICIPIO.toString()))
            .andExpect(jsonPath("$.uf").value(DEFAULT_UF.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMunicipio() throws Exception {
        // Get the municipio
        restMunicipioMockMvc.perform(get("/api/municipios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMunicipio() throws Exception {
        // Initialize the database
        municipioService.save(municipio);

        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();

        // Update the municipio
        Municipio updatedMunicipio = municipioRepository.findById(municipio.getId()).get();
        // Disconnect from session so that the updates on updatedMunicipio are not directly saved in db
        em.detach(updatedMunicipio);
        updatedMunicipio
            .nomeMunicipio(UPDATED_NOME_MUNICIPIO)
            .uf(UPDATED_UF);

        restMunicipioMockMvc.perform(put("/api/municipios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMunicipio)))
            .andExpect(status().isOk());

        // Validate the Municipio in the database
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeUpdate);
        Municipio testMunicipio = municipioList.get(municipioList.size() - 1);
        assertThat(testMunicipio.getNomeMunicipio()).isEqualTo(UPDATED_NOME_MUNICIPIO);
        assertThat(testMunicipio.getUf()).isEqualTo(UPDATED_UF);
    }


    @Test
    @Transactional
    public void deleteMunicipio() throws Exception {
        // Initialize the database
        municipioService.save(municipio);

        int databaseSizeBeforeDelete = municipioRepository.findAll().size();

        // Get the municipio
        restMunicipioMockMvc.perform(delete("/api/municipios/{id}", municipio.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Municipio> municipioList = municipioRepository.findAll();
        assertThat(municipioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Municipio.class);
        Municipio municipio1 = new Municipio();
        municipio1.setId(1L);
        Municipio municipio2 = new Municipio();
        municipio2.setId(municipio1.getId());
        assertThat(municipio1).isEqualTo(municipio2);
        municipio2.setId(2L);
        assertThat(municipio1).isNotEqualTo(municipio2);
        municipio1.setId(null);
        assertThat(municipio1).isNotEqualTo(municipio2);
    }
}

```
<p align="center">
   <strong>Listagem 3- Classe MunicipioResourceIntTest.java</strong> 
</p>

::: :pushpin: Comentários acerca das instruções na classe :::

> Observe o seguinte trecho de código:

```java
45 private static final String DEFAULT_NOME_MUNICIPIO = "AAAAAAAAAA";
46 private static final String UPDATED_NOME_MUNICIPIO = "BBBBBBBBBB";
47 private static final Estado DEFAULT_UF = Estado.MT;
48 private static final Estado UPDATED_UF = Estado.SP;
```

> Nas 4 linhas acima temos o modificador `private static final` que na linguagem Java siginifica que estamos declarando constantes. Então temos aqui 4 constantes que serão utilizadas nos testes com municípios.

> Vamos detalhar as variáveis de instância usadas na classe:

```java
    @Autowired
 51   private MunicipioRepository municipioRepository;
    
    @Autowired
 54   private MunicipioService municipioService;

    @Autowired
 57   private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
 60   private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
 64    private EntityManager em;

 66   private MockMvc restMunicipioMockMvc;

 68   private Municipio municipio;

```

> As linhas (51) e (54) são as injeções de instância de `MunicipioRepository` e `MunicipioService` respectivamente. Essas duas classes serão utilizadas para testes o CRUD de Município.

> A linha (57) injeta uma instância de `MappingJackson2HttpMessageConverter`. Essa classe, que faz parte do Spring,  converte de/para JSON usando a biblioteca  Jackson 2.x. É importante frisar que a comunicação `backend` (Java) com o `frontend` (Angular) é realizada através de JSON. O `backend` transforma os objetos java em JSON antes de enviá-los ao `frontend` e, da mesma forma, converte de JSON para objetos java as mensagens que chegam ao `backend`.


<p align="center">
  <img src="/Imagens/MapeamentoJsonJava.png" alt="Mapeamento JSON/Java">
</p>
<p align="center">
   <strong>Figura 2- Mapemamento JSON/Java</strong> 
</p>

> A linha (60) injeta uma instância da classe `PageableHandlerMethodArgumentResolver`. Essa classe permite que os métodos utilezem o parâmetro `pageable` conforme se visto na linha 74.

> Em (66) é declarada uma variável do tipo `MockMvc`. A classe `MockMvc` é fornecida pelo Spring e fornece suporte para testes encapsulando os beans da aplicação.

Antes de começar os testes é preciso fazer os ajustes iniciais. Observe atentamente o trecho de código abaixo:

```java
 70  @Before
 71   public void setup() {
 72       final MunicipioResource municipioResource = new MunicipioResource(municipioService);
 73       this.restMunicipioMockMvc = MockMvcBuilders.standaloneSetup(municipioResource)
 74           .setCustomArgumentResolvers(pageableArgumentResolver)
 75           .setConversionService(createFormattingConversionService())
 76           .setMessageConverters(jacksonMessageConverter).build();
 77   }
```

> A anotação `@Before`(70) garante que o trecho de código será executado antes dos testes iniciarem.

> As lihas (73)..(75) configuram o `MockMvc` para, ao final, invocar o método `build()` (76) que retorna uma instância de `MockMvc`.

> O primeiro teste desta classe encontra-se no seguimento de código abaixo. Este teste verifica se a criação de um novo município será bem sucedida. Na linha (100) a variável `databaseSizeBeforeCreate` armazena o número de instâncias de municípios existentes antes da criação de um novo municipio.

> A linha (103) cria o município da conversão do objeto java `municipio` em um objeto JSON, invocando o método `HTTP.Post`

> As linhas (109) até (113) é que são os testes propriamente ditos, verificando se a criação foi bem sucedida. O interessante desse método é que ele está testando as 3 camadas: Controller, Service e DAO.
```java
 97 @Test
 98   @Transactional
 99   public void createMunicipio() throws Exception {
 100       int databaseSizeBeforeCreate = municipioRepository.findAll().size();

        // Create the Municipio
 103       restMunicipioMockMvc.perform(post("/api/municipios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
 105        .content(TestUtil.convertObjectToJsonBytes(municipio)))
            .andExpect(status().isCreated());

        // Validate the Municipio in the database
 109       List<Municipio> municipioList = municipioRepository.findAll();
           assertThat(municipioList).hasSize(databaseSizeBeforeCreate + 1);
            Municipio testMunicipio = municipioList.get(municipioList.size() - 1);
 112        assertThat(testMunicipio.getNomeMunicipio()).isEqualTo(DEFAULT_NOME_MUNICIPIO);
 113        assertThat(testMunicipio.getUf()).isEqualTo(DEFAULT_UF);
    }

```
> Os demais testes seguem as mesmas linhas de raciocínio utilizando outros métodos HTTP.

#### Desafio 1 :innocent:

Execute os testes usando o Maven.
