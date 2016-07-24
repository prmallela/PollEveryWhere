
create table user
( id int primary key auto_increment
, email varchar(255) not null unique
, password varchar(255) not null default ''
, created timestamp not null default now()
);

