
-- When altering, make sure not to invalidate
-- existing data!

alter table user add column
  ( first_name  varchar(80));

alter table user add column
  ( last_name varchar(80) not null default '');

