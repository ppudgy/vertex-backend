-- В id находится аббревиатура языка - ru, ru_RU, en en_US - по какомуто стандарту
create table language(
  id varchar(32) not null,
  name varchar(255) not null,

  constraint language_pk primary key(id),
  constraint language_unique unique(id)
);

insert into language (id, name) values('ru_RU', 'Русский');
insert into language (id, name) values('en_GB', 'English');

--  Темы текстов
create table topic(
  id uuid not null,
  schemata uuid not null,
  name varchar(1024) not null,
  checked boolean,

  constraint topic_pk primary key (id),
  constraint topic_unique unique (schemata, name),
  constraint topic_schemata foreign key (schemata) references schemata(id)
);

------------------------------------------------------------------------------------------------------------------ Цель
-- Цели
create table purpose(
  id uuid not null,
  schemata uuid not null,
  name varchar(1024) not null,
  color varchar(10),
  active boolean default false,
  constraint purpose_pk primary key (id),
  constraint purpose_schemata foreign key (schemata) references schemata(id),
  constraint purpose_unique unique (schemata, name)
);

insert into purpose(id, schemata, name, color, active)values('3d1520f7-87a5-4c31-ae07-b8774b1fc1f0', '916e7f18-efb8-4a1a-8b5b-275c319b88d8','home','green',true);
insert into purpose(id, schemata, name, color, active)values('4bc059da-66db-4684-a7ba-b0d8656c1c96', '916e7f18-efb8-4a1a-8b5b-275c319b88d8','work','red',true);
insert into purpose(id, schemata, name, color, active)values('4dbf84f4-ca50-4eeb-88ed-ff07ffedce87', '916e7f18-efb8-4a1a-8b5b-275c319b88d8','hobby','yellow',true);

-- Отношения между персонами
create table personrelationtype(
  id uuid not null,
  schemata uuid not null,
  name varchar(1024) not null,

  constraint personrelationtype_pk primary key (id),
  constraint personrelationtype_unique unique (schemata, name)
);
-- end of file
