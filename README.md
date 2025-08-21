# Backend do Sistema de Help Desk

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

Este é o repositório do backend para a aplicação de helpdesk disponível no meu perfil do github "Helpdesk-Frontend", uma API RESTful completa construída com Java e o ecossistema Spring para dar suporte a um sistema de gerenciamento de tickets.

## Funcionalidades Principais

* **Segurança Robusta:** Implementação de autenticação e autorização utilizando **Spring Security**. As senhas são armazenadas de forma segura com criptografia **BCrypt**.
* **Controle de Acesso por Perfil (RBAC):** Acesso aos endpoints restrito por perfis de usuário (`CLIENTE`, `TECNICO`, `ADMINISTRADOR`), utilizando segurança a nível de método (`@PreAuthorize`).
* **Operações CRUD Completas:** Endpoints para Criar, Ler, Atualizar e Deletar (CRUD) os principais recursos da aplicação:
    * **Tickets:** Gestão completa do ciclo de vida de um chamado.
    * **Usuários:** Área administrativa para gerenciamento de contas.
    * **Comentários:** Sistema para interação e histórico nos tickets.
* **Regras de Negócio:** Lógica implementada na camada de serviço para garantir a integridade dos dados, como:
    * Um cliente só pode visualizar seus próprios tickets.
    * Um usuário não pode ser deletado se possuir tickets associados.
    * Tickets fechados não podem ser alterados.
* **Persistência de Dados com JPA:** Uso do Spring Data JPA e Hibernate para mapeamento objeto-relacional e comunicação com o banco de dados.

## Tecnologias Utilizadas

* **Java 21:** Versão da linguagem utilizada no projeto.
* **Spring Boot 3:** Framework principal para a construção da aplicação.
* **Spring Security 6:** Para gerenciamento de autenticação e autorização.
* **Spring Data JPA / Hibernate:** Para persistência de dados e comunicação com o banco.
* **PostgreSQL:** Banco de dados relacional para armazenamento dos dados.
* **Maven:** Gerenciador de dependências e build do projeto.
* **Lombok:** Para redução de código boilerplate nas entidades e DTOs.

## Como Executar o Projeto Localmente

Siga os passos abaixo para rodar o backend na sua máquina.

### Pré-requisitos

* **JDK 21** (ou superior) instalado.
* **Maven** instalado e configurado nas variáveis de ambiente.
* Um servidor **PostgreSQL** ativo e rodando localmente ou em um container.

### Instalação e Execução

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/helpdesk-api.git](https://github.com/seu-usuario/helpdesk-api.git)
    ```

2.  **Navegue até a pasta do projeto:**
    ```bash
    cd helpdesk-api
    ```

3.  **Configure o Banco de Dados:**
    * Crie um banco de dados no PostgreSQL chamado `helpdesk_db`.
    * Abra o arquivo `src/main/resources/application.properties`.
    * Altere as seguintes propriedades com suas credenciais do PostgreSQL:
      ```properties
      spring.datasource.username=seu_usuario_do_postgres
      spring.datasource.password=sua_senha_do_postgres
      ```

4.  **Execute a aplicação com o Maven:**
    ```bash
    mvn spring-boot:run
    ```

5.  **Servidor no ar:** A API estará disponível em `http://localhost:8080`.

## Endpoints da API

Abaixo estão os principais endpoints disponíveis.

| Método | URL | Proteção | Descrição |
|---|---|---|---|
| **Usuários** | | | |
| `POST` | `/usuarios` | `Pública` | Cria um novo usuário (cliente, técnico ou admin). |
| `GET` | `/usuarios` | `ADMINISTRADOR` | Lista todos os usuários do sistema. |
| `GET` | `/usuarios/{id}` | `ADMINISTRADOR` | Busca um usuário por ID. |
| `PUT` | `/usuarios/{id}` | `ADMINISTRADOR` | Atualiza os dados de um usuário. |
| `DELETE` | `/usuarios/{id}` | `ADMINISTRADOR` | Deleta um usuário. |
| **Tickets** | | | |
| `POST` | `/tickets` | `CLIENTE` | Cliente logado cria um novo ticket. |
| `GET` | `/tickets` | `TECNICO`, `ADMINISTRADOR` | Lista todos os tickets do sistema. |
| `GET` | `/tickets/{id}` | `TECNICO`, `ADMINISTRADOR` ou Dono do Ticket | Busca um ticket por ID. |
| `PUT` | `/tickets/{id}` | `TECNICO`, `ADMINISTRADOR` | Atualiza o status ou prioridade de um ticket. |
| `DELETE` | `/tickets/{id}` | `TECNICO`, `ADMINISTRADOR` | Deleta um ticket. |
| `PATCH` | `/tickets/{id}/atribuir`| `TECNICO`, `ADMINISTRADOR` | Atribui o ticket ao técnico logado. |
| **Comentários** | | | |
| `POST` | `/tickets/{id}/comentarios` | `TECNICO`, `ADMINISTRADOR` ou Dono do Ticket | Adiciona um comentário a um ticket. |
| `GET` | `/tickets/{id}/comentarios` | `TECNICO`, `ADMINISTRADOR` ou Dono do Ticket | Lista todos os comentários de um ticket. |

