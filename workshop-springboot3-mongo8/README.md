# Workshop Spring Boot 3 & MongoDB 8 (DSPosts)

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-8.0-green.svg)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Uma API RESTful desenvolvida com **Java 21** e **Spring Boot 3.3.4**, integrada ao banco de dados NoSQL **MongoDB**. O repositório demonstra a modelagem de domínio orientado a documentos com suporte a objetos embutidos (*Embedded Objects*) e referências (*DBRef*), além de consultas avançadas com expressões regulares (Regex) e intervalos de datas.

---

## 📋 Sumário

- [Visão Geral](#-visão-geral)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura e Modelo de Dados](#arquitetura-e-modelo-de-dados)
- [Endpoints da API](#-endpoints-da-api)
- [Configuração e Execução](#-configuracao-e-execucao)
- [Testes e População Inicial](#-testes-e-população-inicial)
- [Coleção do Postman](#-coleção-do-postman)
- [Referências e Documentação](#-referências-e-documentação)
- [Licença](#-licença)

---

<a id="-visao-geral"></a>
## 🎯 Visão Geral

O projeto consiste na gestão de usuários e postagens de uma rede social fictícia (*DSPosts*). A aplicação permite realizar operações de **CRUD** para usuários, associar postagens a usuários com suporte a comentários embutidos, e efetuar buscas customizadas (busca por título e busca completa no texto/comentários por intervalo de datas).

### Principais Destaques:
- **Modelo Orientado a Documentos**: Uso de anotações do Spring Data MongoDB (`@Document`, `@Id`, `@DBRef`).
- **Objetos Aninhados / Embutidos**: Desempenho otimizado usando subdocumentos (`Author` e `Comment`) sem redundâncias complexas.
- **Tratamento de Exceções**: Manipulação global de erros com `@ControllerAdvice` e respostas padronizadas via `StandardError`.
- **Camada DTO (Data Transfer Object)**: Encapsulamento de entidades para tráfego limpo de dados na API.

---

<a id="tecnologias-utilizadas"></a>
## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Java 21
- **Framework**: Spring Boot 3.3.4
  - `spring-boot-starter-web` (APIs RESTful)
  - `spring-boot-starter-data-mongodb` (Integração NoSQL)
  - `spring-boot-starter-test` (Testes unitários e de integração)
- **Gerenciador de Dependências**: Apache Maven
- **Banco de Dados**: MongoDB
- **Testes de Endpoints**: Postman

---

<a id="arquitetura-e-modelo-de-dados"></a>
## 🏗️ Arquitetura e Modelo de Dados

### Estrutura de Camadas
```text
com.treinamento.workshopmongo
├── config                  # Instanciação de dados de teste (@Profile("test"))
├── controllers             # Endpoints REST (UserResource, PostController)
│   └── exceptions          # Manipulador global de exceções da API (ResourceExceptionHandler, StandardError)
├── models
│   ├── dto                 # Objetos de Transferência de Dados (UserDTO, PostDTO)
│   ├── embedded            # Subdocumentos (Author, Comment)
│   └── entities            # Entidades do MongoDB (User, Post)
├── repositories            # Interfaces Spring Data MongoDB com @Query
└── services                # Regras de negócio da aplicação
    └── exceptions          # Exceções customizadas da regra de negócio (ResourceNotFoundException)
```
### Relacionamento de Entidades
- **User** (`users` collection):
  - `id`: String (ObjectId)
  - `name`: String
  - `email`: String
  - `posts`: List<Post> (`@DBRef(lazy = true)`)
- **Post** (`posts` collection):
  - `id`: String (ObjectId)
  - `moment`: Instant
  - `title`: String
  - `body`: String
  - `author`: Author (Embedded Object)
  - `comments`: List<Comment> (Embedded Array)

---

## 🚀 Endpoints da API

### Usuários (`/users`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/users` | Retorna a lista de todos os usuários em formato `UserDTO`. |
| `GET` | `/users/{id}` | Busca um usuário por ID. |
| `GET` | `/users/{id}/posts` | Retorna todas as postagens pertencentes a um determinado usuário. |
| `POST` | `/users` | Cadastra um novo usuário. |
| `PUT` | `/users/{id}` | Atualiza o nome ou e-mail de um usuário existente. |
| `DELETE` | `/users/{id}` | Remove um usuário pelo ID. |

#### Exemplo de Payload - Cadastro/Atualização de Usuário (`POST` / `PUT`)
```json
{
  "name": "Ana Red",
  "email": "ana@gmail.com"
}
```
### Postagens (`/posts`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/posts/{id}` | Busca uma postagem específica por ID (incluindo autor e comentários embutidos). |
| `GET` | `/posts/titlesearch` | Realiza busca de postagens por trecho do título (utilizando Regex / busca case-insensitive). |
| `GET` | `/posts/fullsearch` | Realiza busca avançada no texto/título/comentários por termo dentro de um intervalo de datas. |

#### Parâmetros de Query da Busca por Título (`GET /posts/titlesearch`):
- `text`: Texto/termo a ser pesquisado no título da postagem (ex: `text=bom dia`).

#### Parâmetros de Query da Busca Completa (`GET /posts/fullsearch`):
- `text`: Texto/termo a ser pesquisado no título, corpo ou comentários (*opcional*, ex: `text=bo`).
- `start`: Data inicial no formato ISO 8601, ex: `2021-02-01T00:00:00Z` (*opcional*).
- `end`: Data final no formato ISO 8601, ex: `2021-02-15T23:59:59Z` (*opcional*).

---

<a id="-configuracao-e-execucao"></a>
## ⚙️ Configuração e Execução

### Pré-requisitos
- **Java JDK 21** instalado.
- **Maven 3.8+** instalado (ou utilizar o wrapper `./mvnw`).
- **MongoDB** em execução localmente na porta padrão `27017` ou via Docker.

### Executando com Docker (Opção Prática)

Para subir uma instância do MongoDB com volume persistente e acessar seu terminal bash:

```bash
# Executa o container do MongoDB 4.4.3 mapeando a porta e volume local
docker run -d -p 27017:27017 -v /data/db --name mongo1 mongo:4.4.3-bionic

# Acessa o terminal interativo (bash) do container
docker exec -it mongo1 bash
```
> 💡 **Dica (Windows):** Se estiver utilizando o Docker Desktop no Windows e precisar localizar onde os volumes persistentes ficam armazenados fisicamente, consulte essa thread no [Stack Overflow](https://stackoverflow.com/questions/43181654/locating-data-volumes-in-docker-desktop-windows).

### Configuração de Perfis
#### Perfil Ativo (`application.properties`).
O arquivo principal define o perfil `test` como padrão para execução local:

```properties
spring.profiles.active=test
```
#### Propriedades de Teste (`application-test.properties`).
O arquivo específico do perfil `test` contém a URI de conexão com o MongoDB:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/workshop_mongo
```
### Passos para Rodar a Aplicação
1. Clone o repositório:
   ```bash
   git clone [https://github.com/seu-usuario/workshop-springboot3-mongo8.git](https://github.com/seu-usuario/workshop-springboot3-mongo8.git)
   cd workshop-springboot3-mongo8
   ```
2. Compile o projeto e baixe as dependências:
   ```bash
   mvn clean install
   ```
3. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```
A API estará disponível em: `http://localhost:8080`

---

<a id="-testes-e-populacao-inicial"></a>
## 🧪 Testes e População Inicial

Quando a aplicação é iniciada sob o perfil `test` (`@Profile("test")`), a classe `TestConfig` limpa as coleções existentes e popula o banco de dados com dados fictícios para facilitar o teste imediato dos endpoints:

- **Usuários criados**: Maria Brown, Alex Green, Bob Grey.
- **Postagens criadas**: "Partiu viagem" e "Bom dia" associadas à Maria.
- **Comentários criados**: Comentários de Alex e Bob vinculados às postagens.

---

<a id="-colecao-do-postman"></a>
## 📬 Coleção do Postman

O repositório inclui o arquivo `DSPosts.postman_collection.json` localizado na raiz do projeto.

Para testar as requisições:
1. Abra o **Postman**.
2. Importe o arquivo `DSPosts.postman_collection.json`.
3. Configure a variável de ambiente ou coleção `{{host}}` com a URL base da aplicação (ex: `http://localhost:8080`).

---

<a id="-referencias-e-documentacao"></a>
## 📚 Referências e Documentação

- [Documentação Oficial do Spring Data MongoDB](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html)
- [Documentação do MongoDB - Query Operators](https://docs.mongodb.com/manual/reference/operator/query)

---

<a id="licenca"></a>
## 📄 Licença

Este projeto é disponibilizado sob a licença [MIT](LICENSE).      
