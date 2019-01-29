
# Estudo de Caso V1

## O que vamos fazer neste tutorial
- Contextualização RestFull
- Configurar minimamente uma aplicação REST usando o Spring
- Construir uma aplicação RestFul 

## 1. Contextualização do RESTFUL

###  ::: :mortar_board: Novo conceito :::

Para contextualizar o RESTFUL é preciso contar um pouco de história. Prometo que serei o mais breve possível!

O  final da era dos mainframes( grandes computadores ) e dos chamados terminais 'burros' coincide (talvez tenha sido mais consequencia do que coincidência) com o surgimento das redes de computadores e o incremento na computação distribuída em substituição à grande capacidade de processamento oferecida pelos computadores centrais. Começam então a surgir as primeiras arquiteturas computacionais calcadas na computação distribuída. Uma das formas utilizadas à época era a RPC (sigla em inglês para Remote Procedure Call). Essa tecnologia mostrou-se indispensável, por exemplo, nas relações cliente-servidor já que nelas um computador cliente depende de um processamento ou de uma informação armazenados no servidor. Com isso surgiram diversas soluções e protocolos que implementavam as RPCs, como, por exemplo, Microsoft Distributed Component Object Model (DCOM), Object Management Group's Common Object Request Brokering Architecture (CORBA), Java's Remote Method Invocation (RMI) e DCE/RPC.[ARNT 2008]

A CORBA® é uma arquitetura mantida pela da OMG®  que é aberta e independente de fornecedor permitindo aos aplicativos trabalhar juntos em um ambiente distribuído. Nasceu em Outubro de 1991 e em Novembro de 2012 foi lançada a última versão (3.3). É uma especificação fantástica, que é independente de linguagem e de fornecedor. Ocorre, entretanto, que o desenvolvimento em CORBA® envolve um certo grau de complexidade que a maioria das aplicações não requer. Esse foi um dos motivos da perda de popularidade da CORBA®. 

Isso não aconteceu apenas com a CORBA®, atingiu o RPC em geral, incluindo   DCOM, Java-RMI, .NET Remoting e todos os outros também.


No fim dos anos 90, a web se popularizou e os diversos protocolos padronizados da Internet (HTTP, por exemplo) se estabeleceram. Com a criação e difusão do formato XML para transmissão de dados, começou a ser escrito um protocolo para RPC baseado em XML. A ele foi dado o nome SOAP (Simple Object Access Protocol). O SOAP é um protocolo de troca de mensagens originalmente elaborado por Dave Winer, Don Box, Bob Atkinson, and Mohsen Al- Ghosein em 1998, com o suporte da Microsoft.[ARNT 2008]

No SOAP, o conjunto de operações permitidas e o conjunto de tipos de dados permitidos são essencialmente ilimitados. Essa variedade de funcionalidades, a princípio uma vantagem,  acabou influenciando no declínio desse protocolo. A API SOAP é tão complicada para entender e usar que necessitou de ferramentas automatizadas na forma de bibliotecas de cliente WSDL e SOAP para sua utilização. Além disso, nem sempre é desejável expor os detalhes dos sistemas através de API voltadas para o público, muitas vezes o que se deseja é fornecer apenas abstrações que permitam a evolução sem a necessidade de quebrar ou modificar sua API. 

Foi aí que surgiu o REST. O REST é muito mais limitado que o SOAP, e isso é, justamente, o motivo de sua popularidade. Para fornecer uma definição simples, o REST é qualquer interface entre sistemas que usam HTTP para obter dados e gerar operações nesses dados em todos os formatos possíveis, como XML e JSON. Essa é uma alternativa cada vez mais popular a outros protocolos de troca de dados padrão. Às vezes, é preferível usar uma solução de processamento de dados mais simples, como REST. O que os desenvolvedores perceberam  é que o conjunto de regras a ser utilizado no projeto da API para torná-la simples, fácil de usar e manter é exatamente o conjunto de possibilidades que o REST introduz. Isso, efetivamente, neutralizou os benefícios dos SOAPs. Seria possível construir uma API simplificada com SOAP, mas nunca tão fácil de usar quanto o REST, então, na prática, todo mundo escolhe o REST. [Sebrechts 2016]

Além disso, a combinação REST + JSON ganhou popularidade especificamente por causa de sua simplicidade. Hoje não há projetos ou aplicativos que não tenham uma API REST para a criação de serviços profissionais baseados neste software. Twitter, YouTube, sistemas de identificação do Facebook… centenas de empresas geram negócios graças às APIs REST . Sem eles, qualquer crescimento horizontal seria praticamente impossível. Isso ocorre porque o REST é o padrão mais lógico, eficiente e difundido na criação de APIs para serviços da Internet.[BBVAOpen4U 2016]



** Referências ** :

ARNT 2008 - Leonardo Bentes Arnt - Engenharia da Computação e Informação - UFRJ, 2008. Disponível em https://www.gta.ufrj.br/grad/08_1/soap/Page3.html

Sebrechts 2016 - Joeri Sebrechts, 2016. Disponível em https://softwareengineering.stackexchange.com/questions/336565/what-is-the-present-day-significance-of-soap

BBVAOpen4U, 2016 - BBVAOpen4U, 2016. Disponível em https://bbvaopen4u.com/en/actualidad/rest-api-what-it-and-what-are-its-advantages-project-development

## 2. Como funciona o RESTFUL no Spring (resumidamente)

1. Um cliente qualquer (pode ser um bowser, um mobile), executando qualquer linguagem (javaScript, TypeScript, Java, .net), faz uma requisição para o servidor. Uma requisição pode utilizar qualquer dos métodos HTTP (GET, POST, DELETE, PUT etc);

2. A requisição é enviada a um método REST que contenha o `RequestMapping` apropriado (`@PostMapping` , `@GetMapping` etc), conforme o método HTTP utilizado na requisição;

3. O método REST recebe a requisição e encaminha para um método de serviços;

4. O método de serviços solicita o recurso a um sistema subjacente. Um recurso pode ser uma instância de um objeto, um arquivo PDF, um arquivo de mídia etc. que esteja armazenado no servidor de banco ou em outro local qualquer (pode ser outro servidor);

5. O recurso solicitado é então devolvido ao método REST, e transformado em um tipo de resposta solicitada pelo `request`. A respota é, então,  empacotada sob a forma de um `ResponseEntity`

6. O `ResponseEntity` é retornado pelo método REST com o conteúdo propriamente dito, o cabeçalho e o tipo de resposta. Se o conteúdo for um objeto ou um array de objetos ele estará no formato JSON(Java Script Object Notation). O conteúdo, entretanto, pode ser uma imagem , um HTML, um PDF etc. 

7. O `ResponseEntity` é recebido e tratado no cliente que o solicitou. O tratamento que será dado depende do cliente e do tipo de conteúdo recebido, podendo ser uma renderização de dados em um formulário, o download de um arquivo, ou até a renderização de uma mensagem de erro, no caso de uma solicitação mal sucedida.

Vide Figura 1.

<p align="center">
  <img src="/Imagens/RESTExample.png" alt="Esquema de funcionamento do RESTFUL">
</p>
<p align="center">
   <strong>Figura 1- Esquema de funcionamento do RESTFUL</strong> 
</p>


## 3. Construindo uma primeira aplicação RestFul com o SpringBoot

A partir de agora, nós vamos construir uma aplicação completa de ponta a ponta. Vamos denominá-la, simplesmente, de `Estudo de Caso`. Então mãos à obra!

::: :walking: Passo a passo :::  

1. Crie uma  pasta `GrupoDeEstudo/SpringBoot/estudo-de-caso-v1`;

2. Abra o Eclipse e crie um projeto Maven: `File|New|Other|Maven Project` com o mesmo nome da pasta.  

Veja como fica a estrutura do projeto, Figura 2. 

<p align="center">
  <img src="/Imagens/EstruturaDePastas.png" alt="Novo Projeto Maven">
</p>
<p align="center">
   <strong>Figura 2- Estrutura do projeto estudo-de-caso-v1</strong> 
</p>

3. Altere o arquivo `pom.xml` conforme abaixo:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>br.com.abim.ec</groupId>
	<artifactId>estudo-de-caso-3</artifactId>
	<version>0.1.0-SNAPSHOT</version>
	<name>estudo-de-caso-v1</name>
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
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>

```

::: :pushpin: Importante :::

> Observe atentamente as dependências desse projeto  `spring-boot-starter-web` e `spring-boot-starter-test`. Esses são os módulos que serão utilizados neste projeto respectivamente para: utilizar  RestFull e realizar testes unitários.


4. Crie a estrutura de pacotes conforme a Figura 3

<p align="center">
  <img src="/Imagens/EstruturaDePacotesV1.png" alt="Estrutura de pacotes">
</p>
<p align="center">
   <strong>Figura 3- Estrutura de pacotes</strong> 
</p>

5. No pacote `br.com.abim.ec` crie a classe `EstudoDeCasoApp.java` conforme a Listagem 1


```java
package br.com.abim.ec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EstudoDeCasoApp {

    private static final Logger log = LoggerFactory.getLogger(EstudoDeCasoApp.class);
    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EstudoDeCasoApp.class);
        app.run(args);
    }
}

```
<p align="center">
   <strong>Listagem 1- Classe EstudoDeCasoApp.java</strong> 
</p>

::: :pushpin: Importante :::

> Observe a anotação `@SpringBootApplication` ela é uma diretiva que informa que essa é a classe principal do projeto, que contém o método `main` e que inicia toda a aplicação.

6. No pacote `br.com.abim.ec.web.rest` crie a classe `HelloResource.java` conforme a Listagem 2


```java
package br.com.abim.ec.web.rest;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloResource {

    @RequestMapping("/")
    public String index() {
        return "Olá eu sou o seu primeiro Spring Boot com Rest!";
    }

}
```
<p align="center">
   <strong>Listagem 2- Classe HelloResource.java</strong> 
</p>

::: :pushpin: Importante :::


>Como vocês podem observar pela anotação `@RestController`, esta é uma classe que disponibiliza serviços REST.

Nesse tipo de classe é possível usar diversas anotações. Exemplo:


| Anotação      | Significado   											     | 
| ------------- |:----------------------------------------------------------------------------------------------------------| 
| @CrossOrigin    |Informa quais recursos são permitidos que  sejam acessados por uma página web de um domínio diferente. | 
| @RestController   |Essa anotação faz com que cada método da classe serialize automaticamente objetos que serão enviados como retorno como um HttpResponse. O objeto e os seus dados são gravados diretamente na resposta HTTP usando JSON ou XML. | 
| @RequestMapping   | @RequestMapping é uma das anotações mais comuns usadas nos aplicativos Spring Web. Esta anotação mapeia solicitações HTTP (1)(GET, POST, DELETE, PUT etc) para métodos existentes no `controller`  REST.  | 
| @Autowired    |A anotação `@Autowired` informa ao Spring onde e como injetar uma instância de um `bean`. Pode ser usado em métodos setter, no construtor, em uma propriedade ou em métodos com nomes arbitrários e / ou vários argumentos.| 
| @PostMapping   | Mapeia as requisições contendo métodos POST para os métodos que estejam  com essa anotação  | 
| @GetMapping   | Idem para métodos GET  | 
| @PutMapping   | Idem para métodos PUT  | 
| @DeleteMapping   | Idem para métodos DELETE  | 


<p align="center">
   <strong>Tabela 1- Principais anotações do Spring em classes REST</strong> 
</p>

(1) - Para saber mais sobre cada um dos métodos HTTP, clique neste link [Métodos HTTP](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Methods)


7. Para finalizar, na pasta `src/main/resources/` crie um arquivo denominado `application.properties` que irá configurar(2) oa porta onde a sua aplicação irá responder e também no nome da aplicação. Vide Listagem 3. 


```properties
spring.application.name=estudo-de-caso-v1
server.port=8090
```
<p align="center">
   <strong>Listagem 3- Configuração application.properties</strong> 
</p>

(2)-Para saber mais sobre essas configurações, clique neste link [propriedades mais comuns do Spring](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)



#### Desafio 1 :innocent:
 
```
- Execute a aplicação via eclipse e digite na url 
```
`http://localhost:8090`

Agora responda: Por que essa requisição foi endereçada para o método `public String index()` ?

#### Desafio 2 :innocent:
 

- Interprete os Headers mostrados na Figura 4


<p align="center">
  <img src="/Imagens/Headers.png" alt="Headers do protocolo HTTP">
</p>
<p align="center">
   <strong>Figura 4- Headers do protocolo HTTP Reques/Response</strong> 
</p>

::: :white_check_mark: Resumo :::

Nesta versão você aprendeu:

- Por que usar o REST;
- O que é o REST;
- A sequencia de chamadas quando se utiliza a arquitetura REST;
- Como criar uma aplicação simples usando REST;
- Os principais métodos HTTP;
- As principais anotações de uma classe REST.


Então, o que achou do nosso primeiro REST? Se gostou passe agora para a implementação [V2](../estudo-de-caso-v2/README.md) do nosso Estudo de Caso.
