
# Estudo de Caso V2

## 1. O que faremos nesta versão

- Trabalhar com logs
- Utilizar YAML para o arquivo de configuração
- Criar uma classe persistente para Municípios
- Utilizar Liquibase
- Utilizar uma interface `@Repository` para municípios
- Utilizar uma classe de serviços
- Criar um método Get para exibir todos os Municípios em formato JSON


## 2. Construindo a aplicação estudo-de-caso-v2 com o SpringBoot

### 2.1 Criando um database Postgresql

Antes de começarmos a construir a nossa aplicação "Estudo de Caso" vamos  criar um banco de dados no Postgresql usando a ferramenta PGAdmin4.

1. Abra o PGAdmin4

2. Selecione o Servidor de banco de dados postgres;

3. Para assegurar que você pode criar um banco de dados, primeiramente desconecte-se do servidor, Figura 1;

<p align="center">
  <img src="/Imagens/ServerDisconect.png" alt="Desconectando-se do servidor">
</p>
<p align="center">
   <strong>Figura 1- Desconectatndo-se do Servidor</strong> 
</p>

4. Conecte-se ao servidor postgres usando a senha do admininstrador, Figura 2;

<p align="center">
  <img src="/Imagens/ServerConect.png" alt="Conectando-se do servidor">
</p>
<p align="center">
   <strong>Figura 2- Conectando-se ao Servidor</strong> 
</p>

5. Crie uma `role` no banco de dados denominada `abim` com senha `abim`, Figura 3;


<p align="center">
  <img src="/Imagens/CreateRole.png" alt="Criando uma nova Role no servidor">
</p>
<p align="center">
   <strong>Figura 3-Criando uma nova Role no servidor</strong> 
</p>

No final, a sua SQL deverá ser igual à mostrada abaixo.

```sql
CREATE USER abim WITH
	LOGIN
	NOSUPERUSER
	CREATEDB
	NOCREATEROLE
	INHERIT
	NOREPLICATION
	CONNECTION LIMIT -1
	PASSWORD 'xxxxxx';

```
6. Crie um banco de dados cujo owner é `abim`, Figura 4 e com os privilégios conforme Figura 5.

<p align="center">
  <img src="/Imagens/CreateDataBase.png" alt="Criando um novo banco de bados no servidor">
</p>
<p align="center">
   <strong>Figura 4-Criando um novo banco de bados no servidor</strong> 
</p>



<p align="center">
  <img src="/Imagens/DataBasePrivileges.png" alt="Privilégios para a role abim no banco abim">
</p>
<p align="center">
   <strong>Figura 5-Privilégios para a role abim no banco abim</strong> 
</p>



### 2.2 Trabalhando com logs

::: :walking: Passo a passo :::  

1. Abra o Eclipse no workspace `GrupoDeEstudo/SpringBoot`;

2. Se o projeto estudo-de-caso-v1 não aparecer, adicione-o através de 'Maven|Import';

3. Copie o projeto estudo-de-caso-v1 para estudo-de-caso-v2, usando o 'Copy|Past' do próprio eclipse;

4. Altere o arquivo `pom.xml` conforme Listagem 1:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>br.com.abim.ec</groupId>
	<artifactId>estudo-de-caso-2</artifactId>
	<version>0.2.0-SNAPSHOT</version>
	<name>estudo-de-caso-v2</name>
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

> Observe atentamente as dependências desse projeto `spring data`, `spring web` e `spring teste`. Esses são os módulos que serão utilizados neste projeto respectivamente para: utilizar JPA, permitir RestFull e realizar testes unitários. Além dessas dependências foram incluídas uma dependência para `Apache Commons` para utilizarmos a classe `StringUtils` que, como o próprio nome já diz, contém diversos utilitários para `Strings`. Foi adicionada também uma dependência para utilizarmos o banco de dados `POSTGRESQL`.

> Observe finalmente que foi adicionado um build para o Liquibase.


5. Altere a classe `EstudoDeCasoApp.java` para o conteúdo da Listagem 2;

```java
package br.com.abim.ec;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class EstudoDeCasoApp {

    private static final Logger log = LoggerFactory.getLogger(EstudoDeCasoApp.class);

    private final Environment env;

    public EstudoDeCasoApp(Environment env) {
        this.env = env;
    }
    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EstudoDeCasoApp.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}{}\n\t" +
                "External: \t{}://{}:{}{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles());
    }
}
```

<p align="center">
   <strong>Listagem 2- Classe EstudoDeCasoApp.java</strong> 
</p>

::: :pushpin: Importante :::

> Observe que foi criado o método `private static void logApplicationStartup(Environment env)`.  Esse método faz o log de início da aplicação, mostrando o nome, endereço e a parta onde a aplicação está rodando.

> Observe também a declaração `private final Environment env;`. Essa é uma classe do `Spring` para obter as configurações do ambiente onde está sendo executada a aplicação, permitindo o acesso, por exemplo, às propriedades do arquivo de configuração.

### 2.3 Utilizando YAML

::: :walking: Passo a passo :::  

1. Crie um arquivo de configuração usando YAML(1). Arquivos do tipo yml são mais fáceis de ler e de escrever do que os arquivos de `.properties`. Para saber mais sobre YAML consulte o [Site oficial do YAML](https://yaml.org/spec/1.2/spec.html)

2. Na pasta `src\resources\config` altere a extensão de `.properties`para `.yml` e copie o conteúdo da Listagem 3

```yml
spring:
    application:
        name: estudo-de-caso-v2
    jpa:
        open-in-view: false
        hibernate:
            ddl-auto: none
            naming:
                physical-strategy: org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
                implicit-strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy 
        database: POSTGRESQL
        show-sql: true
        properties:
            hibernate.id.new_generator_mappings: true
            hibernate.dialect: org.hibernate.dialect.PostgreSQLDialect
            hibernate.temp.use_jdbc_metadata_defaults: false
    datasource:
        url: jdbc:postgresql://localhost:5432/abim
        username: abim
        password: abim
server:
    port: 8090
logging:
    level:
        ROOT: DEBUG
        br.com.abim.ec: DEBUG

```
<p align="center">
   <strong>Listagem 3- Arquivo configuration.yml</strong> 
</p>

::: :pushpin: Importante :::

> YAML usa tabs para controlar a hierarquia e usa `:` ao invés do sinal de `=`. Assim, por exemplo: 

```yml
spring:
    application:
        name: estudo-de-caso-v2
```
equivale a spring.application.name = estudo-de-caso-v2

> Nesse arquivo estão  sendo configurados o nome da aplicação, o JPA, o DataSource, a porta e o log. 


3. Crie um banco de dados no Postgresql com o nome abim, usuário e senha abim.

4. Crie seguinte estrutura de pastas: `src\resources\config\liquibase` e `src\resources\config\liquibase\changelog`

5. Na pasta `src\resources\config\liquibase` crie um arquivo denominado `liquibase.yml` com o conteúdo da Listagem 4

```yml
# Postgresql
driver : org.postgresql.Driver
url : jdbc:postgresql://localhost:5432/abim
username : abim
password : abim
```
<p align="center">
   <strong>Listagem 4- Arquivo Liquibase.yml</strong> 
</p>

### 2.4 Criar uma classe persistente para Municípios

::: :walking: Passo a passo :::  

1. Crie a seguinte estrutura de pacotes: 

<p align="center">
  <img src="/Imagens/EstruturaDePastasV2.png" alt="Estrutura de pacotes para a V2">
</p>
<p align="center">
   <strong>Figura 6- Estruturas de pacotes para a V2</strong> 
</p>

2. No pacote `br.com.abim.ec.domain.enumeration` crie o `enum` da Listagem 5;

```java
package br.com.abim.ec.domain.enumeration;

/**
 * The Estado enumeration.
 */
public enum Estado {
    MT, MS, SP, RJ, RS, RN, GO, SC, MA, TO, AM, PA, PR, MG, BA, SE, AL, RR, RO, AC, PI, PE, CE
}
```
<p align="center">
   <strong>Listagem 5- Enumeration Estado.java</strong> 
</p>

::: :+1: Boa Prática :::

> Como boa prática use um `enum` sempre que tiver valores estáticos (que variam muito pouco ao longo do tempo), evitando criar, neste caso, dados persistentes. 

3. No pacote `br.com.abim.ec.domain` crie a classe da Listagem 6;

```java
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
```
<p align="center">
   <strong>Listagem 6- Classe Município.java</strong> 
</p>


::: :pushpin: Importante :::

> A classe `Municipio.java` é uma classe persistente, ou seja, as instâncias dessa classe serão gravadas em banco de dados sob a forma de linhas de uma tabela que está em um banco de dados. A anotação  `@Entity` é que permite isso. Na verdade nós estamos usando JPA juntamente com o framework Hibernate. Por hora saiba que o Hibernate é um framework ORM (Object Relational Mapping). ORM faz o mapeamento (convesão) de instância de objetos para linhas em relações (tabelas) e a JPA é uma especificação java (Java API) para tratar o mapeamento objeto relacional. O Hibernate é uma das implementações da JPA, há outras como o EclipseLink.

<p align="center">
  <img src="/Imagens/ORM.png" alt="Convertendo Objetos em Relações">
</p>
<p align="center">
   <strong>Figura 7- Conversão de Objetos em Relações</strong> 
</p>
<p align="center">
	<font size="6">Fonte: https://gingsoft.com/?p=820 acesso em 28/11/2018</font>
</p>


> Observe também a anotação abaixo que mapeia o atributo `uf` para valores enumerados em `Estado.java` , usando a anotação `@Enumerated`:

```java
  @Enumerated(EnumType.STRING)
    @Column(name = "uf")
    private Estado uf;
```

### 2.5  Utilizar Liquibase

::: :walking: Passo a passo :::  

1. Na pasta `src/resources/config/liquibase`, crie mais dois arquivos.

 - O primeiro denominado `municpios.csv` com o conteúdo da Listagem 7

```csv
id;nome_municipio;uf
1000;Cuiabá;MT
1001;Várzea Grande;MT
1002;Rio de Janeiro;RJ
1003;São Paulo;SP
1004;Corumbá;MS
```
<p align="center">
   <strong>Listagem 7- municipios.csv</strong> 
</p>


 - O segundo denominado `master.xml` com o conteúdo da Listagem 8

```xml
<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd">

    <include file="config/liquibase/changelog/00000000000000_initial_schema.xml" relativeToChangelogFile="false"/>
    <include file="config/liquibase/changelog/20181223125423_added_entity_Municipio.xml" relativeToChangelogFile="false"/>
</databaseChangeLog>

```
<p align="center">
   <strong>Listagem 8- master.xml</strong> 
</p>

2. Na pasta `src/resources/config/liquibase/changelog`, crie  dois arquivos, Listagens 9 e 10.

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

    <changeSet id="00000000000000" author="jbbf">
        <createSequence sequenceName="hibernate_sequence" startValue="1000" incrementBy="50"/>
    </changeSet>
</databaseChangeLog>
```
<p align="center">
   <strong>Listagem 9 - 00000000000000_initial_schema.xml</strong> 
</p>


```xml
<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog
	xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
	xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.5.xsd
                        http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd">

	<property name="now" value="now()" dbms="h2" />
	<property name="now" value="current_timestamp" dbms="postgresql" />
	<property name="floatType" value="float4" dbms="postgresql, h2" />
	<property name="floatType" value="float" dbms="mysql, oracle, mssql" />
	<!-- Added the entity Municipio. -->
	<changeSet id="20181223125423-1" author="jbbf">
		<createTable tableName="municipio">
			<column name="id" type="bigint"
				autoIncrement="${autoIncrement}">
				<constraints primaryKey="true" nullable="false" />
			</column>
			<column name="nome_municipio" type="varchar(255)">
				<constraints nullable="true" />
			</column>
			<column name="uf" type="varchar(255)">
				<constraints nullable="true" />
			</column>
		</createTable>
		<loadData encoding="UTF-8"
			file="config/liquibase/municipios.csv" separator=";"
			tableName="municipio" />
	</changeSet>
</databaseChangeLog>
```
<p align="center">
   <strong>Listagem 10 - 20181223125423_added_entity_Municipio.xml</strong> 
</p>

3. Faça `Save All` e no prompt execute o maven.

```
mvn liquibase:update
```

4. Verifique no PGAdmin se as instruções do Liquibase foram executadas com sucesso.


### 2.6 Utilizar uma interface `@Repository` para municípios


> Como era de se esperar a anotação `@Repository` é a principal interface do `Spring Data Repository`. Ela recebe como parâmetro  a classe de domínio que será gerenciada (Município no nosso caso), bem como o tipo de identificação dessa classe (no nosso caso um id do tipo Long). Essa interface atua principalmente como uma interface de marcação para capturar os tipos com os quais trabalhar e para descobrir as interfaces que a estendem. Uma dessas interfaces é a `CrudRepository` que fornece funcionalidade CRUD (incluir, alterar, excluir e selecionar) sofisticada para a classe de entidade que está sendo gerenciada.(Tradução livre de 
[docs.spring.io](https://docs.spring.io/spring-data/data-commons/docs/2.1.3.RELEASE/reference/html/#repositories))

> Os repositórios são ferramentas poderosissimas do Spring pois permitem o uso de `Query Language` para estender os métodos `CRUD` inicialmente providos. Apenas para citar alguns exemplos:

```java
   //Seleciona os 10 primeiros`User` pelo último nome com retorno paginado
Page<User> queryFirst10ByLastname(String lastname, Pageable pageable);

   //Seleciona `Person` pelo email e pelo último nome 
List<Person> findByEmailAddressAndLastname(EmailAddress emailAddress, String lastname);

  // Usando o flag distinct na query
List<Person> findDistinctPeopleByLastnameOrFirstname(String lastname, String firstname);
List<Person> findPeopleDistinctByLastnameOrFirstname(String lastname, String firstname);

  // Usando ignoring case para uma propriedade específica
List<Person> findByLastnameIgnoreCase(String lastname);
  // Usando ignoring case para todas as  proppiedades
List<Person> findByLastnameAndFirstnameAllIgnoreCase(String lastname, String firstname);

  // Usando `static` ORDER BY para uma query
List<Person> findByLastnameOrderByFirstnameAsc(String lastname);
List<Person> findByLastnameOrderByFirstnameDesc(String lastname);


  //Usando uma propriedade (Cep) de uma propriedade (Endereco)
List<Person> findByAddress_ZipCode(ZipCode zipCode);


```
> Spring Data Repository ainda posui muitos outros recursos, tais como QBE . Para uma lista completa dos recursos providos consulte 
[docs.spring.io](https://docs.spring.io/spring-data/data-commons/docs/2.1.3.RELEASE/reference/html/#repositories)

::: :walking: Passo a passo :::  

1. No pacote `br.com.abim.ec.repository` crie a classe `MunicipioRepository.java` conforme Listagem 11

```java
package br.com.abim.ec.repository;

import br.com.abim.ec.domain.Municipio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
/**
 * Spring Data  repository for the Municipio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

}
```

<p align="center">
   <strong>Listagem 11 - MunicipioRepository.java</strong> 
</p>

::: :pushpin: Importante :::

> Observe que desta vez, estamos criando uma `interface` e não um `classe`. Essa interface é descendente de `JpaRepository` que, logicamente, é também uma interface (que usa `Generics`- observe a instrução `<Municipio, Long>` ). Você vai notar que não existe uma classe que implementa essa interface. Na verdade essa classe só vai existir em tempo de execução e será injetada pelo Spring (via Bean Factory) sempre que ele encontrar algo como:

```java
    @Autowired
    private MunicipioRepository municipioRepository;
```

> Essa é, essencialmente, a técnica denominada `programar por interfaces` que traz, dentre outros benefícios, o baixo acoplamento e a alta coesão. Para saber mais sobre a coesão consulte 
[Alta Coesão](http://www.dsc.ufcg.edu.br/~jacques/cursos/map/html/pat/altacoesao.htm).

> Veja também os padrões [GRASP](https://pt.wikipedia.org/wiki/GRASP_\(padr%C3%A3o_orientado_a_objetos\)) propostos por Craig Larman.

2. Pronto. Com isso já podemos usar os métodos providos por padrão e, se for necessário, poderemos estender a interface usando `Query Language`.

### 2.7  Utilizar uma classe de serviços
#### Razões para usar:

1. Fornece separação de interesses

Cada um no seu quadrado! A camada de serviço fornece modularidade de código, a lógica e as regras de negócios. Já a camada DAO é responsável apenas por interagir com o banco de dados. A camada de serviços depende da camada DAO.

2. Fornece Segurança

Se você fornecer uma camada de serviço, o acesso ao banco de dados fica mais protegido, ou seja, só é possível chegar ao banco de dados através da camada de serviços. Se o banco de dados não puder ser acessado diretamente pela aplicação `cliente` (e não houver um módulo DAO trivial atuando como o serviço), um invasor que assumiu o cliente não poderá ter acesso aos seus dados diretamente.

3. Baixo acoplamento, através da indireção

A camada de serviço também pode ser usada para prover o baixo acoplamento no aplicativo. Suponha que seu controlador tenha 50 métodos e, por sua vez, ele chame 20 métodos do DAO. Posteriormente, você decide alterar os métodos do DAO que atendem a esses controladores, você terá que alterar os 50 métodos no controlador. Se em vez disso, se você tiver 20 métodos de serviço chamando esses 20 métodos DAO, você precisará fazer alterações em apenas 20 métodos de serviço para apontar para um novo DAO.

::: :pushpin: Importante :::

> Na arquitetura em camadas proposta pelo SpringBoot, a camada `service` localiza-se entre a camada `repository` e `controller`

<p align="center">
  <img src="/Imagens/ArquiteturaSpring.png" alt="Arquitetura em camadas usada pelo Spring">
</p>
<p align="center">
   <strong>Figura 8- Arquitetura em camadas usada pelo Spring</strong> 
</p>

::: :walking: Passo a passo :::  

1. No pacote `br.com.abim.ec.service`, crie a interface denominada `MuniciapioService.java`, conforme Listagem 12.

```java
package br.com.abim.ec.service;

import br.com.abim.ec.domain.Municipio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

/**
 * Service Interface: Gerenciamento de  Municipios.
 */
public interface MunicipioService {

    /**
     * Grava um municipio.
     *
     * @param municipio the entity to save
     * @return the persisted entity
     */
    Municipio save(Municipio municipio);

    /**
     * Obtém todos os municipios.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Municipio> findAll(Pageable pageable);


    /**
     * Obtém um município pelo "id" .
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Municipio> findOne(Long id);

    /**
     * Exclui um municípo pelo  "id" .
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

```

<p align="center">
   <strong>Listagem 12 - MunicipioService.java</strong> 
</p>

2. No pacote `br.com.abim.ec.service.impl`, crie a classe denominada `MuniciapioServiceImpl.java`, conforme Listagem 13.

```java
package br.com.abim.ec.service.impl;

import br.com.abim.ec.service.MunicipioService;
import br.com.abim.ec.domain.Municipio;
import br.com.abim.ec.repository.MunicipioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Municipio.
 */
@Service
@Transactional
public class MunicipioServiceImpl implements MunicipioService {

    private final Logger log = LoggerFactory.getLogger(MunicipioServiceImpl.class);

    private MunicipioRepository municipioRepository;

    public MunicipioServiceImpl(MunicipioRepository municipioRepository) {
        this.municipioRepository = municipioRepository;
    }

    /**
     * Grava um municipio.
     *
     * @param municipio the entity to save
     * @return the persisted entity
     */
    @Override
    public Municipio save(Municipio municipio) {
        log.debug("Solicitado ao repositório para salvar um Municipio : {}", municipio);
        return municipioRepository.save(municipio);
    }

    /**
     * Obtém todos municipios.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Municipio> findAll(Pageable pageable) {
        log.debug("Solicitado ao repositório obter todos os Municipios");
        return municipioRepository.findAll(pageable);
    }


    /**
     * Obtém um municipio pelo id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Municipio> findOne(Long id) {
        log.debug("Solicitado ao repositório obter o município de id : {}", id);
        return municipioRepository.findById(id);
    }

    /**
     * Exclui o municipio pelo id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Solicitado ao repositório para excluir o município de id : {}", id);
        municipioRepository.deleteById(id);
    }
}

```
<p align="center">
   <strong>Listagem 13 - MunicipioServiceImpl.java</strong> 
</p>


::: :pushpin: Importante :::

> O parâmetro “readOnly” na anotação @Transactional especifica que nenhuma operação de DML poderá ser executada (Insert, Delete ou Update), ou seja, apenas consultas. Para um conhecimento mais amplo sobre a anotação `@Transactional` clique neste link [Anotação Transactional](https://pt.stackoverflow.com/questions/96778/qual-a-finalidade-da-anota%C3%A7%C3%A3o-transactionalreadonly-false).

> a instrução `log.debug` está intimamente associada com a configuração realizada em `application.yml`

```yml
logging:
    level:
        ROOT: DEBUG
        br.com.abim.ec: DEBUG
```


### 2.8 Criar um método Get para exibir todos os Municípios em formato JSON
::: :walking: Passo a passo :::  


1. No pacote `br.com.abim.ec.web.rest`  crie a classe `MunicipioResource.java`

```java
package br.com.abim.ec.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.abim.ec.domain.Municipio;
import br.com.abim.ec.service.MunicipioService;
import br.com.abim.ec.web.rest.util.PaginationUtil;

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
  
}

```
<p align="center">
   <strong>Listagem 14 - Classe MunicipioResource.java</strong> 
</p>

::: :pushpin: Importante :::

>Preste atenção no seguinte trecho de código: 

```java
private MunicipioService municipioService;

public MunicipioResource(MunicipioService municipioService) {
    this.municipioService = municipioService;
}
````
> O código acima permite ao Spring injetar uma instância de `municiouiServiceImpl` na classe  `MunicipioResource` e equivale a:

```java
    @Autowired
    private MunicipioService municipioService;
```

> Observe o trecho de código abaixo:
```java
1  @GetMapping("/municipios")
2    public ResponseEntity<List<Municipio>> getAllMunicipios(Pageable pageable) {
3        log.debug("REST request to get a page of Municipios");
4        Page<Municipio> page = municipioService.findAll(pageable);
5        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/municipios");
6        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
7    }

```
(1) - O método é anotado com `@GetMapping("/municipios")` que significa que o método será acionado sempre que uma requisição `http://localhost:8090/api/municipios` for feita.

(2) - O método retorna uma lista de municípios encapsulados dentre de uma `ResponseEntity`. Uma `ResponseEntity`, além do conteúdo (lista de municípios), contém uma cabeçalho e um `HttpStatus` (neste caso `HttpStatus.OK`)

(3) - Faz o registro da operação.

(4) - Invoca o método findAll da classe de serviços, obtendo uma relação de municípios paginada.

(5) - Prepara o cabeçalho para o `ResponseEntity`.

(6) - Retorna uma nova instância de `ResponseEntity`.


2. No pacote `br.com.abim.ec.web.rest.util`  crie a classe `PaginationUtil.java` para lidar com paginação  na aplicação cliente

```java
package br.com.abim.ec.web.rest.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Utility class for handling pagination.
 *
 * <p>
 * Pagination uses the same principles as the <a href="https://developer.github.com/v3/#pagination">GitHub API</a>,
 * and follow <a href="http://tools.ietf.org/html/rfc5988">RFC 5988 (Link header)</a>.
 */
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
   <strong>Listagem 15 - Classe PaginationUtil.java</strong> 
</p>


#### Desafio 1 :innocent:

Responda: Por que a uri `http://localhost:8090/api/municipios ` tem que usar `api`  ?

#### Desafio 2 :innocent:
 
```
- Usando o `curl`, selecione todos os municípios
```

(1)Para usar o `curl` no Windows, se você já instalou o GIT a partir do [git-scm.com](https://git-scm.com), basta adicionar a pasta `C:\Program Files\Git\mingw64\bin\` no path do Windows.  

no prompt, digite:

```
curl -X GET \
  http://localhost:8090/api/municipios 
```
#### Desafio 3:innocent:

Após executar a instrução acima,  você saberia dizer qual o formato do conteúdo retornado?

Passe agora para a implementação [V3](../estudo-de-caso-v3/README.md) do nosso Estudo de Caso.

