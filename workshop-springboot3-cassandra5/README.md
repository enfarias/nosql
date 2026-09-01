# Workshop Spring Boot 3 & Cassandra 3 (DSProducts)

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Apache Cassandra](https://img.shields.io/badge/Cassandra-3.11.10-blue.svg)](https://cassandra.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Uma API RESTful desenvolvida com **Java 21** e **Spring Boot 3.3.4**, integrada ao banco de dados NoSQL **Apache Cassandra**. O repositório demonstra a modelagem de dados colunar orientada a keyspaces e tabelas, com suporte a tipos definidos pelo usuário (*User Defined Types - UDT*) usando listas congeladas (`@Frozen`), consultas com permissão explícita de filtro (`ALLOW FILTERING`) e busca textual customizada via índice SASI (*SASIIndex*).

---

## 📋 Sumário

- [Visão Geral](#-visão-geral)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura e Modelo de Dados](#arquitetura-e-modelo-de-dados)
- [Endpoints da API](#-endpoints-da-api)
- [Configuração e Execução](#configuracao-e-execucao)
- [Testes e População Inicial](#-testes-e-população-inicial)
- [Coleção do Postman](#-coleção-do-postman)
- [Referências e Documentação](#-referências-e-documentação)
- [Licença](#-licença)

---

## 🎯 Visão Geral

O projeto consiste no gerenciamento de um catálogo de produtos e departamentos de um e-commerce fictício (*DSProducts*). A aplicação permite realizar operações completas de **CRUD** para departamentos, além de buscas otimizadas de produtos por departamento e consultas por trechos na descrição.

### Principais Destaques:
- **Criação Automática de Keyspace**: Configuração programática (`CassandraConfig`) que provisiona o keyspace `productsdb` via Spring Data Cassandra com suporte a `CREATE_IF_NOT_EXISTS`.
- **User Defined Types (UDT)**: Modelagem de propriedades dinâmicas de produtos (`Prop`) utilizando anotações `@UserDefinedType` e listas congeladas `@Frozen Prop`.
- **Filtros e Consultas Avancadas**: Consultas com `@AllowFiltering` para filtros por coluna de partição/departamento e consultas SQL/CQL customizadas usando a anotação `@Query`.
- **Índice Customizado SASI**: Suporte a buscas por subcadeias de caracteres (`LIKE %termo%`) na coluna de descrição do produto.
- **Tratamento Global de Exceções**: Manipulação de erros com `@ControllerAdvice` e respostas padronizadas em `StandardError` para recursos não encontrados (`404 Not Found`).

---

<a id="tecnologias-utilizadas"></a>
## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Java 21
- **Framework**: Spring Boot 3.3.4
  - `spring-boot-starter-web` (APIs RESTful)
  - `spring-boot-starter-data-cassandra` (Integração NoSQL com Apache Cassandra)
  - `spring-boot-starter-test` (Módulo de testes)
- **Gerenciador de Dependências**: Apache Maven
- **Banco de Dados**: Apache Cassandra 3.11.10 (executado via Docker)
- **Testes de Endpoints**: Postman

---

<a id="arquitetura-e-modelo-de-dados"></a>
## 🏗️ Arquitetura e Modelo de Dados

### Estrutura de Camadas
```text
com.devsuperior.workshopcassandra
├── config                  # Configuração do Cassandra (CassandraConfig) e Carga de dados (@Profile("test"))
├── controllers             # Endpoints REST (DepartmentController, ProductController)
│   └── exceptions          # Manipulador global de exceções da API (ResourceExceptionHandler, StandardError)
├── model
│   ├── dto                 # Objetos de Transferência de Dados (DepartmentDTO, ProductDTO)
│   ├── embedded            # Tipos Customizados / UDT (Prop)
│   ├── entities            # Tabelas do Cassandra (Department, Product)
│   └── enums               # Enumerações do domínio (PropType)
├── repositories            # Interfaces Spring Data Cassandra com @AllowFiltering e @Query
└── services                # Regras de negócio da aplicação
    └── exceptions          # Exceções customizadas da regra de negócio (ResourceNotFoundException)
```
### Estrutura das Tabelas (`productsdb` keyspace)

1. **Department** (Tabela `departments`):
   - `id`: `UUID` (`@PrimaryKey`)
   - `name`: `String`

2. **Product** (Tabela `products`):
   - `id`: `UUID` (`@PrimaryKey`)
   - `department`: `String`
   - `price`: `Double`
   - `moment`: `Instant`
   - `name`: `String`
   - `description`: `String`
   - `props`: `List<@Frozen Prop>` (Lista de UDTs congelados)

3. **User Defined Type (UDT)** (`prop`):
   - `name`: `String`
   - `value`: `String`
   - `type`: `PropType` Enum (`PRODUCT`, `CONDITION`)

---

<a id="endpoints-da-api"></a>
## 🚀 Endpoints da API

### Departamentos (`/departments`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/departments` | Retorna a lista de todos os departamentos. |
| `GET` | `/departments/{id}` | Busca um departamento por ID (`UUID`). |
| `POST` | `/departments` | Cadastra um novo departamento. |
| `PUT` | `/departments/{id}` | Atualiza o nome de um departamento existente. |
| `DELETE` | `/departments/{id}` | Remove um departamento pelo ID. |

---

### Produtos (`/products`)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/products/{id}` | Busca um produto por ID (`UUID`). |
| `GET` | `/products?department={nome}` | Filtra produtos pelo nome do departamento (`ALLOW FILTERING`). |
| `GET` | `/products/description?text={termo}` | Busca produtos por trecho na descrição usando índice SASI. |

---

<a id="configuracao-e-execucao"></a>
## ⚙️ Configuração e Execução

### Pré-requisitos
- **Java JDK 21** instalado.
- **Maven 3.8+** instalado (ou utilizar o wrapper `./mvnw`).
- **Docker** em execução localmente.

---

### Executando o Apache Cassandra via Docker

1. Suba o container do Cassandra 3.11.10 com volume persistente e mapeamento da porta padrão (`9042`):

```bash
# Cria e executa o container com volume persistente
docker run -d --name cassandra1 -p 9042:9042 -v cassandra_data:/var/lib/cassandra cassandra:3.11.10
```
2. Para interagir com o container, escolha uma das formas de acesso abaixo:

Opção A: Acesso direto ao terminal CQLSH (Recomendado)
```Bash
# Conecta diretamente ao terminal interativo do Cassandra
docker exec -it cassandra1 cqlsh
```

Opção B: Acesso ao terminal Bash interno
```bash
# Acessa o shell Linux dentro do container
docker exec -it cassandra1 bash

# Uma vez dentro do container, inicie o CLI do Cassandra:
cqlsh
```

> 💡 **Dica (Windows):** Se estiver utilizando o Docker Desktop no Windows e precisar localizar onde os volumes persistentes ficam armazenados fisicamente, consulte essa thread no [Stack Overflow](https://stackoverflow.com/questions/43181654/locating-data-volumes-in-docker-desktop-windows).

### Configuração do Índice Customizado SASI (Obrigatório para busca por descrição)

A consulta do endpoint `/products/description?text={termo}` utiliza a cláusula `LIKE` no Cassandra, a qual exige a criação de um índice **SASI (SSTable-Attached Secondary Index)** na coluna `description`.

Acesse a CLI `cqlsh` do container:
```bash
docker exec -it cassandra1 cqlsh
```

Conecte-se ao keyspace do projeto e crie o índice customizado executando o script SQL/CQL abaixo:

```sql
USE productsdb;

CREATE CUSTOM INDEX products_description_idx ON products (description) 
USING 'org.apache.cassandra.index.sasi.SASIIndex' 
WITH OPTIONS = {
  'mode': 'CONTAINS', 
  'analyzer_class': 'org.apache.cassandra.index.sasi.analyzer.NonTokenizingAnalyzer',
  'case_sensitive': 'false'
};
```

> 💡 **Teste via CQLSH:**
> ```sql
> SELECT * FROM products WHERE description LIKE '%ameaça%';
> ```

---

### Configuração de Perfis

#### Perfil Ativo (`application.properties`)
O arquivo principal define o perfil `test` como padrão para execução local:

```properties
spring.profiles.active=test
```

#### Propriedades de Teste (`application-test.properties`)
O arquivo do perfil `test` contém os pontos de contato e a porta para a conexão com o Cassandra:

```properties
spring.data.cassandra.contact-points=localhost
spring.data.cassandra.keyspace-name=productsdb
spring.data.cassandra.port=9042
spring.data.cassandra.local-datacenter=datacenter1
```

---

### Passos para Rodar a Aplicação

1. Acesse o repositório no seu ambiente de desenvolvimento:
   ```bash
   cd nosql
   ```
2. Compile o projeto e baixe as dependências:
   ```bash
   mvn clean install
   ```   
3. Execute a aplicação Spring Boot:
   ```bash
   mvn spring-boot:run
   ```
A API estará disponível em: `http://localhost:8080`

---

<a id="testes-e-populacao-inicial"></a>
## 🧪 Testes e População Inicial

Quando a aplicação é iniciada no perfil `test` (`@Profile("test")`), a classe `TestConfig` limpa as tabelas existentes e inicializa dados de teste:

- **Departamentos criados**: *Livros*, *Computadores*, *Jogos*.
- **Produtos criados**:
  - *O Senhor dos anéis* (com UDTs de Páginas e Edição)
  - *O Código da Vinci* (com UDTs de Páginas e Edição)
  - *PC Gamer* (com UDTs de Memória, CPU e Garantia)
  - *Desktop PC* (com UDTs de Memória, CPU e Garantia)
  - *The Last of Us 2* (com UDT de Ano)

---

<a id="colecao-do-postman"></a>
## 📬 Coleção do Postman

O repositório inclui o arquivo `DSProducts.postman_collection.json` localizado na raiz do projeto.

Para testar as requisições:
1. Abra o **Postman**.
2. Importe o arquivo `DSProducts.postman_collection.json`.
3. Configure a variável de ambiente ou coleção `{{host}}` com a URL base da aplicação (`http://localhost:8080`).

---

<a id="referencias-e-documentacao"></a>
## 📚 Referências e Documentação

- [Documentação Oficial do Apache Cassandra](https://cassandra.apache.org/doc/latest)
- [Documentação Oficial do Spring Data Cassandra](https://docs.spring.io/spring-data/cassandra/docs/current/reference/html)
- [Tutorial Apache Cassandra + Spring Boot (YouTube)](https://www.youtube.com/watch?v=s1xc1HVsRk0&list=PLalrWAGybpB-L1PGA-NfFu2uiWHEsdscD)
- [Projeto Exemplo Spring Boot Cassandra CRUD (GitHub)](https://github.com/rahul-ghadge/spring-boot-cassandra-crud)

---

<a id="licenca"></a>
## 📄 Licença

Este projeto é disponibilizado sob a licença [MIT](LICENSE).
