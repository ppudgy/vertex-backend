-- Файл создания базы данных проекта "apex"
-- project: apex
-- file: person.psql.sql
-- user: pudgy
-- time: 4.04.2015

create table person(
    id          uuid not null,
    schemata    uuid not null,
    origin     timestamp not null default current_timestamp,
    name        varchar(256) not null,
    sername     varchar(256) not null,
    family      varchar(256) not null,
    birthday    timestamp not null,
    sex         varchar(5) not null default 'men',
    checked     boolean,

    constraint person_pk primary key (id),
    constraint person_schema_fk foreign key (schemata) references schemata(id),
    constraint person_unique unique (schemata, name, sername, family, birthday),

    constraint person_sex_check check( sex in ('men', 'women'))
);

-- отношение  многие ко многим лицо - лицо
create table personperson(
    id uuid not null,
    leftperson uuid not null,
    rightperson uuid not null,
    type uuid not null,
    origin timestamp not null default current_timestamp,

    constraint PERSONPERSON_PK primary key (id),
    constraint personperson_unique unique  (leftperson, rightperson, type),
    constraint PERSONPERSON_LEFT  foreign key (leftperson) references person(id),
    constraint PERSONPERSON_RIGHT  foreign key (rightperson) references person(id),
    constraint PERSONPERSON_TYPE  foreign key (type) references personrelationtype(id)
);

------------------------------------------------------------------------- даты
-- тип даты
create table typeofdate(
    id    uuid not null,
    name  varchar(255) not null,

    constraint typeofdate_pk primary key (id),
    constraint typeofdate_unique unique (name)
);

insert into typeofdate(id, name) values ('32d436f0d23f45dc82c4748dc9b8cd03', 'День рождения');
insert into typeofdate(id, name) values ('f8cda5ce5bbf4d1b93392e48462d637d', 'День смерти');
insert into typeofdate(id, name) values ('c80366ffd45f45b5b57927dc9ba2f29a', 'День свадьбы');


create table dates(
    id uuid not null,
    schemata uuid not null,
    datestart timestamp not null,
    dateend timestamp,

    constraint dates_pk primary key (id),
    constraint dates_schema_fk foreign key (schemata) references schemata(id)
);

create table persondates(
    id          uuid not null,
    typeofdate  uuid not null,
    person      uuid not null,
    dates       uuid not null,

    constraint persondates_pk primary key (id),
    constraint persondates_unique unique  (typeofdate, person, dates),
    constraint persondates_typeofdate_fk foreign key (typeofdate) references typeofdate(id),
    constraint persondates_person_fk foreign key (person) references person(id),
    constraint persondates_dates_fk foreign key (dates) references dates(id)
);
------------------------------------------------------------------------- контакты

create table typeofcontact(
    id    uuid not null,
    name  varchar(255) not null,

    constraint typeofcontact_pk primary key (id),
    constraint typeofcontact_unique unique (name)
);

insert into typeofcontact(id, name) values ('a41a2e363dd84661870b27292a3efc31', 'Телефон');
insert into typeofcontact(id, name) values ('a07ef73950ed47169fd07ad99e460b4f', 'Эл.почта');
insert into typeofcontact(id, name) values ('a20ac9b85c06448f9595c65b9cc6ddf1', 'Whatsapp');
insert into typeofcontact(id, name) values ('f6d90ef0715146b688f872c6569b725f', 'Почтовый адрес');
insert into typeofcontact(id, name) values ('77d646a1cc3847ada93472f27d1b8d15', 'Facebook');
insert into typeofcontact(id, name) values ('bf3c504d740c440781b5d6700f156d4e', 'ВКонтакте');
insert into typeofcontact(id, name) values ('198c450ea2ab4568a1f8baad4cff136b', 'Instagram');
insert into typeofcontact(id, name) values ('e02b6263f0e5455094189681a0a054b6', 'Однокласники');
insert into typeofcontact(id, name) values ('28a3b44899d7480b85841a96227cfc14', 'LiveJornal');
insert into typeofcontact(id, name) values ('04a558f699c94d279f3643dabe295124', 'Блог');

create table contact(
    id            uuid not null,
    schemata      uuid not null,
    origin     timestamp not null default current_timestamp,
    typeofcontact uuid not null,
    person        uuid not null,
    contact       varchar(1024),
    comment       varchar(1024),

    constraint contacts_pk primary key (id),
    constraint contacts_schema_fk foreign key (schemata) references schemata(id),
    constraint contacts_person_fk foreign key (person) references person(id),
    constraint contacts_typeofcontacts_fk foreign key (typeofcontact) references typeofcontact(id),
    constraint contacts_unique unique (schemata, contact, person)
);

