create table workspaces (
  id uuid primary key default gen_random_uuid(),
  tenant_id uuid not null,
  name varchar(255) not null,
  created_at timestamp not null default now(),

  constraint fk_workspaces_tenant
    foreign key (tenant_id) references tenants(id)
);

create index ix_workspaces_tenant_id on workspaces(tenant_id);
create unique index ux_workspaces_tenant_name on workspaces(tenant_id, name);
