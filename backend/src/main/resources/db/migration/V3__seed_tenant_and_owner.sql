-- TenantCRM seed (demo)
-- tenant: acme
-- user: owner@acme.com
-- pass: Admin@123  (bcrypt já gerado)

with inserted_tenant as (
  insert into tenants (slug, name, active, created_at)
  values ('acme', 'Acme Ltda', true, now())
  returning id
)
insert into users (tenant_id, name, email, password_hash, role, active, created_at)
select
  t.id,
  'Owner',
  'owner@acme.com',
  '$2a$10$u.fVkZL84W.m1yvFN39OvOFkeTLOiaL/3cxd40vzUXgTkbDb6d19i',
  'OWNER',
  true,
  now()
from inserted_tenant t;

