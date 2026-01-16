# TenantCRM

[English](./README.md)

TenantCRM é um backend de CRM **multi-tenant** pronto para evoluir como SaaS, desenvolvido em Java + Spring Boot. Possui autenticação JWT, isolamento lógico por tenant e gestão de workspaces, com arquitetura pensada para escalar, vender e evoluir para front-end web/mobile.

Este repositório segue um modelo **monorepo**:
- `backend/` — API REST Spring Boot (atual)
- `frontend/` — UI Web (planejado)

---

## Principais funcionalidades

- Autenticação com JWT (Bearer token)
- Arquitetura multi-tenant (isolamento lógico por tenant)
- Usuários vinculados a tenants
- Gestão de Workspaces por tenant
- API REST padronizada
- Tratamento correto de erros (400 / 401 / 403 / 409)
- PostgreSQL + migrações com Flyway
- Base para produto SaaS (RBAC, billing, convites, auditoria)

---

## Arquitetura (visão geral)

```text
Cliente (Web/Mobile)
   |
   |  Authorization: Bearer <JWT>
   v
API REST (Spring Boot)
   - Auth (login)
   - Contexto de tenant (token -> tenantId)
   - Workspaces
   - Tratamento de exceções
   |
   v
PostgreSQL
   - Flyway migrations
```

---

## Stack

- Java 25 (funciona com Java 21+)
- Spring Boot 4
- Spring Security
- JWT
- PostgreSQL
- Flyway
- Hibernate / JPA
- Maven
- Docker Compose (DB local)

---

## Como rodar localmente

Pré-requisitos

- Java 21+ (ou 25)
- Maven
- Docker + Docker Compose
- Postman (ou outro cliente HTTP)

## 1) Subir o PostgreSQL

Na pasta backend/:
```bash
docker compose up -d
```

Banco padrão:

- Host: localhost
- Porta: 5432
- Database: tenantcrm
- User: tenantcrm
- Password: tenantcrm (dev only)

## 2) Rodar a API

Na pasta backend/:
```bash
mvn spring-boot:run
```
Base URL:
```text
http://localhost:8080
```

---

## Autenticação (JWT)

POST /auth/login
```json
{
  "tenantSlug": "acme",
  "email": "demo@acme.com",
  "password": "ChangeMe123!"
}
``` 
Nota: As credenciais acima são apenas seed/demo para ambiente local.

Resposta:

```json
{
  "tokenType": "Bearer",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```
Use o token nas requisições protegidas:
```text
Authorization: Bearer <token>
```

---

## Workspaces

POST /workspaces

Cria workspace para o tenant inferido a partir do JWT.
```json
{
  "name": "Sales"
}
```
Respostas esperadas:

- 201 Created — criado com sucesso
- 409 Conflict — workspace já existe para este tenant
- 401 Unauthorized — token ausente/inválido
- GET /workspaces
- Lista workspaces do tenant inferido pelo JWT.

---

## Segurança e segredos (importante)

Este repositório não contém segredos reais.

- As credenciais de banco e o segredo JWT exibidos são **exclusivos para ambiente de desenvolvimento local**.
- Em produção, devem ser utilizados **variáveis de ambiente ou um gerenciador de segredos**.
- Arquivos sensíveis como `.env`, keystores e configurações específicas de ambiente estão ignorados via `.gitignore`.

Os valores apresentados servem apenas para facilitar a execução local do projeto.

---

## Roadmap

Backend

    - RBAC (perfis e permissões)
    - Refresh token
    - Auditoria (createdBy, updatedBy) e trilha de auditoria
    - Paginação, filtros e ordenação
    - Soft delete
    - OpenAPI / Swagger
    - Rate limit e hardening

## Frontend

    - UI React/Next.js (login, dashboard, workspaces)
    - Navegação tenant-aware
    - Tema white-label

## Produto

    - Planos e billing
    - Convite de usuários
    - Multi-idioma
    - Base de deploy em cloud (Docker/K8s)

---

## Licença

MIT

---

Autor

Igor Carvalho