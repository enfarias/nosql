# Workshop Spring Boot 3 WebFlux & MongoDB 8 (DSPosts)

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-8.0.3-green.svg)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Uma API RESTful não-bloqueante e reativa desenvolvida com **Java 21**, **Spring Boot 3.4.5** e **Spring WebFlux**, integrada ao banco de dados NoSQL **MongoDB Atlas (v8.0.3)** via **Spring Data MongoDB Reactive**. O repositório demonstra a modelagem de dados reativa baseada em documentos (*Posts* e *Users*), incorporação de objetos embutidos (*Embedded Objects*), streaming de dados via Project Reactor (`Mono` e `Flux`) e consultas reativas customizadas com expressões regulares (*JSON Queries*).

---

## 📋 Sumário

- [Visão Geral](#-visão-geral)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura e Modelo de Dados](#arquitetura-e-modelo-de-dados)
- [Endpoints da API](#-endpoints-da-api)
- [Configuração e Execução](#configuracao-e-execucao)
- [Testes e População Inicial](#-testes-e-população-inicial)
- [Coleção do Postman](#-coleção-do-postman)
- [Arquitetura de Tratamento de Erros](#-arquitetura-de-tratamento-de-erros)
- [Referências e Documentação](#-referências-e-documentação)
- [Licença](#-licença)

---

## 🎯 Visão Geral

O projeto consiste no gerenciamento de uma rede social simplificada (*DSPosts*). A aplicação permite realizar operações completas de **CRUD** reativo para usuários e postagens, além de buscas otimizadas de posts por título e buscas completas contendo filtros por texto, intervalo de datas e comentários.

### Principais Destaques:
- **Programação Reativa Não-Bloqueante**: Utilização do Spring WebFlux e Project Reactor (`Mono` e `Flux`) sobre o servidor de alto desempenho Netty.
- **Modelagem de Dados Orientada a Documentos**: Uso de anotações `@Document`, objetos embutidos (`Author`, `Comment`) e serialização de dados com MongoDB Atlas.
- **Consultas Reativas Customizadas**: Métodos de repositório reativos estendendo `ReactiveMongoRepository` utilizando anotações `@Query` com regex do MongoDB (`$regex`, `$options: 'i'`) e combinações lógicas (`$and`, `$or`).
- **Pipelining Reativo na Carga Inicial**: Encadeamento reativo sem bloqueios na classe `TestConfig` utilizando operadores como `.then()`, `.thenMany()`, `Flux.defer()` e `.flatMapMany()`.
- **Tratamento Global de Exceções Reativo**: Manipulação de erros com `@ControllerAdvice` adaptado para WebFlux com `ServerHttpRequest` e respostas padronizadas via `CustomError`.

---

<a id="tecnologias-utilizadas"></a>
## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Java 21
- **Framework**: Spring Boot 3.4.5
  - `spring-boot-starter-webflux` (API Reativa não-bloqueante e servidor Netty)
  - `spring-boot-starter-data-mongodb-reactive` (Integração NoSQL reativa com MongoDB)
  - `spring-boot-starter-test` & `reactor-test` (Testes unitários e reativos)
- **Gerenciador de Dependências**: Apache Maven
- **Banco de Dados**: MongoDB Atlas v8.0.3 (Cloud) / MongoDB Local (opcional)
- **Testes de Endpoints**: Postman

---

<a id="arquitetura-e-modelo-de-dados"></a>
## 🏗️ Arquitetura e Modelo de Dados

### Modelo Conceitual do Domínio

![Modelo de Domínio](assets/model-spring-mongodb.png)

---

### Estrutura de Camadas
```text
com.treinamento.workshopmongo
├── config                  # Carga de dados reativa (@Profile("test")) (TestConfig)
├── controllers             # Endpoints REST Reativos (UserController, PostController)
│   ├── handlers            # Manipulador global de exceções da API (ControllerExceptionHandler)
│   │   └── dto             # Objetos de Transferência de Dados para erros HTTP (CustomError)
│   └── util                # Utilitários para parsing de parâmetros de URL (URL)
├── dto                     # Objetos de Transferência de Dados do Domínio (UserDTO, PostDTO, AuthorDTO, CommentDTO)
├── models
│   ├── embedded            # Objetos Embutidos / Denormalizados (Author, Comment)
│   └── entities            # Documentos do MongoDB (User, Post)
├── repositories            # Interfaces Spring Data Reactive MongoDB (UserRepository, PostRepository)
└── services                # Regras de negócio reativas
    └── exceptions          # Exceções customizadas da regra de negócio (ResourceNotFoundException)
```
### Estrutura dos Documentos (`workshop_mongo` database)

1. **User** (Coleção `users`):
   - `id`: `String` (`@Id`)
   - `name`: `String`
   - `email`: `String`

2. **Post** (Coleção `posts`):
   - `id`: `String` (`@Id`)
   - `moment`: `Instant`
   - `title`: `String`
   - `body`: `String`
   - `author`: `Author` (Objeto Embutido)
   - `comments`: `List<Comment>` (Lista de Objetos Embutidos)

3. **Objetos Embutidos (Embedded Objects)**:
   - **Author**: `id` (`String`), `name` (`String`)
   - **Comment**: `text` (`String`), `moment` (`Instant`), `author` (`Author`)

---

<a id="endpoints-da-api"></a>
## 🚀 Endpoints da API

### Usuários (`/users`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/users` | Retorna um `Flux<UserDTO>` com todos os usuários. |
| `GET` | `/users/{id}` | Busca um usuário por ID, retornando um `Mono<UserDTO>`. |
| `POST` | `/users` | Cadastra um novo usuário. |
| `PUT` | `/users/{id}` | Atualiza as informações de um usuário existente. |
| `DELETE` | `/users/{id}` | Remove um usuário pelo ID. |

---

### Posts (`/posts`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/posts/{id}` | Busca um post por ID, retornando um `Mono<PostDTO>`. |
| `GET` | `/posts/user/{id}` | Retorna o `Flux<PostDTO>` contendo todos os posts vinculados a um usuário. |
| `GET` | `/posts/titlesearch?text={termo}` | Busca posts cujo título contenha o texto informado (Ignora maiúsculas/minúsculas). |
| `GET` | `/posts/fullsearch?text={termo}&start={dataInicio}&end={dataFim}` | Realiza busca avançada por texto (no título, corpo ou comentários) e intervalo de datas. |

---

<a id="configuracao-e-execucao"></a>
## ⚙️ Configuração e Execução
### Pré-requisitos
- **Java JDK 21** instalado.
- **Maven 3.8+** instalado (ou utilizar o wrapper `./mvnw`).
- Conta ativa no **MongoDB Atlas** (ou instância local do MongoDB 8+ executando na porta `27017`).

---

### Configuração de Perfis

#### Perfil Ativo (`application.properties`)
O arquivo principal define o perfil `test` como padrão para execução:

```properties
spring.application.name=workshop-springboot3-webflux-mongo8
spring.profiles.active=test
```
#### Propriedades de Teste (`application-test.properties`)
O projeto utiliza por padrão a conexão com o **MongoDB Atlas (v8.0.3)** em nuvem. A senha da base deve ser informada via variável de ambiente `MONGO_ATLAS_PASSWORD`:

```properties
# Conexão MongoDB Atlas (Padrão)
spring.data.mongodb.uri=mongodb+srv://edsonney_db_user:${MONGO_ATLAS_PASSWORD}@aws-cloud-webflux-mongo.98dtigw.mongodb.net/workshop_mongo?retryWrites=true&w=majority&appName=aws-cloud-webflux-mongo

# Conexão Local (Caso queira utilizar MongoDB via Docker/Local)
# spring.data.mongodb.uri=mongodb://localhost:27017/workshop_mongo
```
> 💡 **Nota:** Para rodar localmente com o arquivo de ambiente sem expor senhas, defina a variável no terminal antes de executar o projeto:
> - **Linux/macOS:** `export MONGO_ATLAS_PASSWORD="sua_senha_aqui"`
> - **Windows (PowerShell):** `$env:MONGO_ATLAS_PASSWORD="sua_senha_aqui"`

---

### Passos para Rodar a Aplicação

1. Acesse o diretório do projeto:
   ```bash
   cd workshop-springboot3-webflux-mongo8
   ```
      
2. Compile o projeto e baixe as dependências:
   ```bash
   mvn clean install
   ```
   
3. Execute a aplicação Spring Boot:
   ```bash
   mvn spring-boot:run
   ```
   
A API reativa estará disponível em: `http://localhost:8080`

---

<a id="testes-e-populacao-inicial"></a>
## 🧪 Testes e População Inicial

Quando a aplicação é iniciada no perfil `test` (`@Profile("test")`), a classe `TestConfig` limpa as coleções existentes e popula o banco de dados reativamente:

- **Usuários criados**: *Maria Brown*, *Alex Green*, *Bob Grey*.
- **Posts criados**:
  - *Partiu viagem* (Autor: Maria Brown, Comentários de Alex e Bob)
  - *Bom dia* (Autor: Maria Brown, Comentário de Alex)

---

<a id="colecao-do-postman"></a>
## 📬 Coleção do Postman

O repositório inclui a coleção oficial do Postman pronta para importação e testes dos endpoints reativos:

1. Importe o arquivo **`DSPosts.webflux.postman_collection.json`** (localizado na raiz do projeto) para o seu Postman.
2. Crie ou selecione um ambiente no Postman com a variável `host` configurada:
   - `host`: `http://localhost:8080`
3. A coleção já contém todas as requisições configuradas para **User** e **Post**, incluindo exemplos de payloads JSON para cadastro/atualização e parâmetros de busca para consultas reativas (`titlesearch` e `fullsearch`).

---

<a id="referencias-e-documentacao"></a>
## 📚 Referências e Documentação

- [Documentação Oficial do Spring WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Documentação Oficial do Project Reactor](https://projectreactor.io/docs/core/release/reference/)
- [Documentação Oficial do Spring Data MongoDB Reactive](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#reactive)
- [Documentação do MongoDB Atlas Cloud](https://www.mongodb.com/docs/atlas/)

---

<a id="licenca"></a>
## 📄 Licença

Este projeto é disponibilizado sob a licença [MIT](LICENSE).
