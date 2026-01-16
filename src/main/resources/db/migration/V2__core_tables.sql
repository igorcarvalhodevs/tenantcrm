-- V2: core tables (multi-tenant by tenant_id)

-- Postgres extensions (uuid)
create extension if not exists "pgcrypto";

create table tenants (
  id uuid primary key default gen_random_uuid(),
  name varchar(255) not null,
  slug varchar(100) not null unique,
  active boolean not null default true,
  created_at timestamp not null default now()
);

create table users (
  id uuid primary key default gen_random_uuid(),
  tenant_id uuid not null,
  name varchar(255) not null,
  email varchar(255) not null,
  password_hash varchar(255) not null,
  role varchar(50) not null,
  active boolean not null default true,
  created_at timestamp not null default now(),

  constraint fk_users_tenant
    foreign key (tenant_id) references tenants(id)
);

create unique index ux_users_tenant_email on users(tenant_id, email);
create index ix_users_tenant_id on users(tenant_id);

