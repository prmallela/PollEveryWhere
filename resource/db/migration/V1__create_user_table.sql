
create table user
( id int primary key auto_increment
, email varchar(255) not null unique
, password varchar(255) not null default ''
, created timestamp not null default now()
);


create table registration
( id int primary key auto_increment
, fname varchar(50) not null
, lname varchar(50) not null
, email varchar(255) not null unique
, password varchar(255) not null
, created timestamp not null default now()
);

