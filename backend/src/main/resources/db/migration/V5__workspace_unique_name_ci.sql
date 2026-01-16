-- garante unicidade por tenant (case-insensitive)
create unique index if not exists ux_workspaces_tenant_name_ci
on workspaces (tenant_id, lower(name));
