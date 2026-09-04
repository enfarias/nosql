# Spring Boot 3 & NoSQL Masterclass

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-8.0.3-green.svg)](https://www.mongodb.com/)
[![Apache Cassandra](https://img.shields.io/badge/Cassandra-3.11.10-blue.svg)](https://cassandra.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Este repositório reúne microsserviços e estudos práticos de integração entre o ecossistema **Spring Boot 3 (Java 21)** e bancos de dados **NoSQL**, abordando duas abordagens de persistência (**Imperativa** e **Reativa**) e dois paradigmas principais: **Orientado a Documentos (MongoDB)** e **Colunar / Wide-Column (Apache Cassandra)**.

---

## 📋 Sumário

- [Visão Geral dos Subprojetos](#-visão-geral-dos-subprojetos)
- [Estrutura do Repositório](#estrutura-do-repositório)
- [Matriz Comparativa dos Projetos](#-matriz-comparativa-dos-projetos)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Como Executar](#-como-executar)
- [Licença](#-licença)

---

<a id="visão-geral-dos-subprojetos"></a>
## 🎯 Visão Geral dos Subprojetos

### 1. [`workshop-springboot3-mongo8`](./workshop-springboot3-mongo8/)
API RESTful imperativa (**DSPosts**) focada na modelagem de uma rede social orientada a documentos no **MongoDB**:
- **Domínio**: Gestão de **Usuários** (`users`) e **Postagens** (`posts`).
- **Modelagem de Dados**: Uso de subdocumentos embutidos (`Author`, `Comment`) para otimizar leituras sem redundância e referências ativas (`@DBRef`) para vinculação de postagens ao usuário.
- **Buscas Avançadas**: Endpoint de busca por texto com Regex (`/posts/titlesearch`) e busca combinada no corpo/comentários com intervalo de datas ISO 8601 (`/posts/fullsearch`).

### 2. [`workshop-springboot3-cassandra5`](./workshop-springboot3-cassandra5/)
API RESTful imperativa (**DSProducts**) focada em catálogo de e-commerce e modelagem colunar no **Apache Cassandra (v3.11)**:
- **Domínio**: Gestão de **Departamentos** (`departments`) e **Produtos** (`products`).
- **User Defined Types (UDT)**: Modelagem de propriedades dinâmicas de produtos (`Prop`) mapeadas com anotações `@UserDefinedType` e listas congeladas (`@Frozen`).
- **Consultas & Indexação**: Uso de consultas com permissão explícita de filtro (`ALLOW FILTERING`) por departamento e criação de **Índice Customizado SASI** (*SASIIndex*) para buscas textuais por descrição (`LIKE %termo%`).

### 3. [`workshop-springboot3-webflux-mongo8`](./workshop-springboot3-webflux-mongo8/)
API RESTful **não-bloqueante e reativa** (**DSPosts**) desenvolvida com **Spring WebFlux** e **MongoDB Atlas (v8.0.3)**:
- **Arquitetura Reativa**: Comunicação não-bloqueante orientada a eventos sobre servidor **Netty** utilizando **Project Reactor** (`Mono` e `Flux`).
- **Spring Data Reactive MongoDB**: Repositórios reativos (`ReactiveMongoRepository`) e pipelines de inicialização de dados sem bloqueio na thread principal.
- **Tratamento de Erros e Consultas Reativas**: Consultas JSON/Regex reativas otimizadas e manipulador de exceções adaptado para WebFlux via `@ControllerAdvice` e `ServerHttpRequest`.

---

<a id="estrutura-do-repositório"></a>
## 🏗️ Estrutura do Repositório

```text
nosql/
├── workshop-springboot3-mongo8/          # API DSPosts (Imperativo + MongoDB 8.0)
│   ├── src/
│   ├── DSPosts.postman_collection.json
│   └── README.md                         # Documentação detalhada
├── workshop-springboot3-cassandra3/      # API DSProducts (Imperativo + Cassandra 3.11)
│   ├── src/
│   ├── DSProducts.postman_collection.json
│   └── README.md                         # Documentação detalhada
├── workshop-springboot3-webflux-mongo8/  # API DSPosts (Reativo + WebFlux + MongoDB Atlas 8.0.3)
│   ├── src/
│   ├── DSPosts.webflux.postman_collection.json
│   └── README.md                         # Documentação detalhada
└── README.md                             # Documentação principal
```

<a id="matriz-comparativa-dos-projetos"></a>
## 📊 Matriz Comparativa dos Projetos

| Característica | `workshop-springboot3-mongo8` | `workshop-springboot3-cassandra3` | `workshop-springboot3-webflux-mongo8` |
| :--- | :--- | :--- | :--- |
| **Aplicação Exemplo** | DSPosts (Rede Social) | DSProducts (E-commerce) | DSPosts (Rede Social Reativa) |
| **Paradigma / Modelo** | Imperativo (Servlet / Tomcat) | Imperativo (Servlet / Tomcat) | **Reativo (WebFlux / Netty)** |
| **Tipo de NoSQL** | Documentos (BSON/JSON) | Colunar / Wide-Column (CQL) | Documentos (BSON/JSON) |
| **Banco de Dados** | MongoDB 8.0 (Local/Docker) | Apache Cassandra 3.11.10 (Docker) | **MongoDB Atlas v8.0.3 (Cloud)** |
| **Encapsulamento / Tipos** | Objetos Embutidos + `@DBRef` | UDT Congelados (`@Frozen Prop`) | Objetos Embutidos Denormalizados |
| **Streaming / Fluxo** | Coleções Java (`List<T>`) | Coleções Java (`List<T>`) | **Project Reactor (`Mono<T>`, `Flux<T>`)** |
| **Recurso de Busca** | Expressões Regulares (Regex) | Índice Secundário SASI (`LIKE`) | Regex Reativo / JSON Queries |
| **Coleção Postman** | `DSPosts.postman_collection.json` | `DSProducts.postman_collection.json` | `DSPosts.webflux.postman_collection.json` |

---

<a id="tecnologias-utilizadas"></a>
## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Java 21
- **Framework Principal**: Spring Boot 3 (v3.3.4 & v3.4.5)
- **Módulos do Spring**:
  - `spring-boot-starter-web` (REST Imperativo)
  - `spring-boot-starter-webflux` (REST Reativo)
  - `spring-boot-starter-data-mongodb` & `spring-boot-starter-data-mongodb-reactive`
  - `spring-boot-starter-data-cassandra`
- **Bancos de Dados NoSQL**:
  - **MongoDB** (Local v8.0 / Cloud Atlas v8.0.3)
  - **Apache Cassandra** (v3.11.10 via Docker)
- **Gerenciador de Dependências**: Apache Maven
- **Testes de API**: Postman

---

<a id="como-executar"></a>
## 🚀 Como Executar

Cada subprojeto é autossuficiente e possui seu próprio arquivo `README.md` com as instruções completas de execução, scripts de inicialização no Docker e coleções do Postman:

1. **Rede Social Imperativa (MongoDB Local)**: Acesse a pasta [`workshop-springboot3-mongo8`](./workshop-springboot3-mongo8/).
2. **Catálogo de E-commerce (Cassandra Docker)**: Acesse a pasta [`workshop-springboot3-cassandra5`](./workshop-springboot3-cassandra5/).
3. **Rede Social Reativa (WebFlux + MongoDB Cloud)**: Acesse a pasta [`workshop-springboot3-webflux-mongo8`](./workshop-springboot3-webflux-mongo8/).

---

<a id="licenca"></a>
## 📄 Licença

Este projeto é disponibilizado sob a licença [MIT](LICENSE).
