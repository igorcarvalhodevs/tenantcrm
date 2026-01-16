# TenantCRM

[Português](./README.pt-BR.md)

TenantCRM is a SaaS-ready, multi-tenant CRM backend built with Java and Spring Boot. It provides JWT authentication, tenant isolation, and workspace management with a clean architecture designed for real-world scalability and productization.

This repository follows a **monorepo** approach:
- `backend/` — Spring Boot REST API (current)
- `frontend/` — Web UI (planned)

---

## Highlights

- JWT authentication (Bearer token)
- Multi-tenant architecture (logical isolation per tenant)
- Users associated with tenants
- Workspace management per tenant
- Standardized REST API responses
- Proper error semantics (400 / 401 / 403 / 409)
- PostgreSQL + Flyway migrations
- Designed to evolve into a commercial SaaS (RBAC, billing, invitations, audit logs)

---

## Architecture (High level)

```text
Client (Web/Mobile)
   |
   |  Authorization: Bearer <JWT>
   v
Spring Boot REST API
   - Auth (login)
   - Tenant context (token -> tenantId)
   - Workspaces
   - Exception handling
   |
   v
PostgreSQL
   - Flyway migrations
```

---

## Tech Stack

- Java 25 (works with Java 21+)
- Spring Boot 4
- Spring Security
- JWT
- PostgreSQL
- Flyway
- Hibernate / JPA
- Maven
- Docker Compose (local DB)

---

## Getting Started (Local)

### Requirements

- Java 21+ (or 25)
- Maven
- Docker + Docker Compose
- Postman (or any HTTP client)

### 1) Start PostgreSQL

From the backend/ folder:

```bash
docker compose up -d
```
Default DB:

- Host: localhost

- Port: 5432

- Database: tenantcrm

- User: tenantcrm

- Password: tenantcrm

### 2) Run the API

From the backend/ folder:

```bash
mvn spring-boot:run
```
API base URL:

```text
http://localhost:8080
```

---

## Authentication (JWT)

### POST /auth/login
```json
{
  "tenantSlug": "acme",
  "email": "owner@acme.com",
  "password": "Admin@123"
}
```

Response:
```json
{
  "tokenType": "Bearer",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Use the token in every protected request:
```text
Authorization: Bearer <token>
```

---

## Workspaces
### POST /workspaces

Create a workspace for the tenant inferred from the JWT.
```json
{
  "name": "Sales"
}
```
Expected responses:

- 201 Created — created successfully
- 409 Conflict — workspace already exists for this tenant
- 401 Unauthorized — missing/invalid token

### GET /workspaces

List workspaces for the tenant inferred from the JWT.

---

## Security & Secrets (Important)

This repository does not include real secrets.

- .env, secret keys, keystores, and environment-specific configs must remain local and are ignored via .gitignore.
- Use *.example files for templates (e.g., .env.example) if needed.

If you intend to deploy, use a secret manager (recommended) and CI/CD environment variables.

---

## Roadmap

### Backend

- RBAC (roles & permissions)
- Refresh tokens
- Audit fields (createdBy, updatedBy), audit log
- Pagination, filtering, sorting
- Soft delete
- OpenAPI / Swagger
- Rate limiting & security hardening

### Frontend

- React/Next.js UI (login, dashboard, workspaces)
- Tenant-aware navigation
- White-label theme support

### Product

- Plans & billing
- User invitations
- Multi-language support
- Cloud deployment baseline (Docker/K8s)

---

### License

MIT

---

## Author

Igor Carvalho