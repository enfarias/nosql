# Spring Boot 3 & NoSQL Masterclass

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-8.0-green.svg)](https://www.mongodb.com/)
[![Apache Cassandra](https://img.shields.io/badge/Cassandra-3.11.10-blue.svg)](https://cassandra.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Este repositório reúne microsserviços e estudos práticos de integração entre o ecossistema **Spring Boot 3 (Java 21)** e bancos de dados **NoSQL**, abordando dois paradigmas principais: **Orientado a Documentos (MongoDB)** e **Colunar / Wide-Column (Apache Cassandra)**.

---

<a id="sumario"></a>
## 📋 Sumário

- [Visão Geral dos Subprojetos](#-visão-geral-dos-subprojetos)
- [Estrutura do Repositório](#estrutura-do-repositório)
- [Comparativo entre os Projetos](#-comparativo-entre-os-projetos)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Como Executar](#-como-executar)
- [Licença](#-licença)

---

<a id="visao-geral-dos-subprojetos"></a>
## 🎯 Visão Geral dos Subprojetos

### 1. [`workshop-springboot3-mongo8`](./workshop-springboot3-mongo8/)
API RESTful (**DSPosts**) focada na modelagem de uma rede social orientada a documentos no **MongoDB**:
- **Domínio**: Gestão de **Usuários** (`users`) e **Postagens** (`posts`).
- **Modelagem de Dados**: Uso de subdocumentos embutidos (`Author`, `Comment`) para otimizar leituras sem redundância e referências ativas (`@DBRef`) para vinculação de postagens ao usuário.
- **Buscas Avançadas**: Endpoint de busca por texto com Regex (`/posts/titlesearch`) e busca combinada no corpo/comentários com intervalo de datas ISO 8601 (`/posts/fullsearch`).

### 2. [`workshop-springboot3-cassandra5`](./workshop-springboot3-cassandra5/)
API RESTful (**DSProducts**) focada em catálogo de e-commerce e modelagem colunar no **Apache Cassandra (v3.11)**:
- **Domínio**: Gestão de **Departamentos** (`departments`) e **Produtos** (`products`).
- **User Defined Types (UDT)**: Modelagem de propriedades dinâmicas de produtos (`Prop`) mapeadas com anotações `@UserDefinedType` e listas congeladas (`@Frozen`).
- **Consultas & Indexação**: Uso de consultas com permissão explícita de filtro (`ALLOW FILTERING`) por departamento e criação de **Índice Customizado SASI** (*SASIIndex*) para buscas textuais por descrição (`LIKE %termo%`).

---

<a id="estrutura-do-repositório"></a>
## 🏗️ Estrutura do Repositório

```text
nosql/
├── workshop-springboot3-mongo8/       # API DSPosts (Users & Posts + MongoDB 8.0)
│   ├── src/
│   ├── DSPosts.postman_collection.json
│   └── README.md                       # Documentação detalhada do MongoDB
├── workshop-springboot3-cassandra3/   # API DSProducts (Departments & Products + Cassandra 3.11)
│   ├── src/
│   ├── DSProducts.postman_collection.json
│   └── README.md                       # Documentação detalhada do Cassandra
└── README.md                           # Documentação principal (este arquivo)
```

<a id="comparativo-entre-os-projetos"></a>
### 📊 Comparativo entre os Projetos

| Característica | MongoDB (`workshop-springboot3-mongo8`) | Cassandra (`workshop-springboot3-cassandra3`) |
| :--- | :--- | :--- |
| **Aplicação Exemplo** | DSPosts (Rede Social) | DSProducts (Catálogo de E-commerce) |
| **Entidades Principais** | `User`, `Post`, `Author`, `Comment` | `Department`, `Product`, `Prop` (UDT) |
| **Tipo de NoSQL** | Documentos (BSON/JSON) | Colunar / Wide-Column (CQL) |
| **Estratégia de Relacionamento** | Objetos Embutidos (`Author`, `Comment`) e `@DBRef` | Tipos Customizados Congelados (`@Frozen Prop`) |
| **Recurso de Busca Textual** | Expressões Regulares (Regex) nativas | Índice Secundário SASI (*SASIIndex*) |
| **Coleção do Postman** | `DSPosts.postman_collection.json` | `DSProducts.postman_collection.json` |

<a id="tecnologias-utilizadas"></a>
## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Java 21
- **Framework**: Spring Boot 3.3.4 (Spring Web, Data MongoDB, Data Cassandra)
- **Bancos de Dados**: MongoDB 8.0 & Apache Cassandra 3.11.10 (via Docker)
- **Gerenciador de Dependências**: Apache Maven
- **Testes de API**: Postman

---

<a id="como-executar"></a>
## 🚀 Como Executar

Cada subprojeto é autosuficiente e possui seu próprio arquivo `README.md` com as instruções completas de execução, scripts de inicialização de bancos de dados no Docker e coleções do Postman:

1. **Para testar a API de Rede Social (MongoDB)**: Acesse a pasta [`workshop-springboot3-mongo8`](./workshop-springboot3-mongo8/).
2. **Para testar a API de E-commerce (Cassandra)**: Acesse a pasta [`workshop-springboot3-cassandra5`](./workshop-springboot3-cassandra5/).

---

<a id="licença"></a>
## 📄 Licença

Este projeto é disponibilizado sob a licença [MIT](LICENSE).
