-- Схема пользователя.
-- Является еденицей разграничения прав доступа к информации в банке данных.

create table schemata(
  id uuid not null,
  origin timestamp  not null default current_timestamp,
  name varchar(1024) not null,
  access timestamp not null default current_timestamp,
  enable boolean not null default true,

  constraint "schemata_pk" primary key (id),
  constraint "schemata_unique" unique (name)
);


-- создаем две схемы по умолчанию
insert into schemata(id, name) values ('3d1520f7-87a5-4c31-ae07-b8774b1fc1f0', 'admin');
insert into schemata(id, name) values ('4bc059da-66db-4684-a7ba-b0d8656c1c96', 'pudgy');
insert into schemata(id, name) values ('916e7f18-efb8-4a1a-8b5b-275c319b88d8', 'test');


-- end of file
