-- Файл создания базы данных проекта "apex"
-- project: apex
-- file: document.psql.sql
-- user: pudgy
-- time: 4.04.2015

create table document(
    id        uuid not null,    -- 32 - длина строки UUID в hex представлении
    origin    timestamp not null default current_timestamp,
    schemata  uuid not null,
    name      varchar(1024) not null,
    date      timestamp not null,
    purpose   uuid not null,
    location  varchar(1024),
    text      varchar(32000),
    mime      varchar(256),
    treated   boolean,
    file      bytea, --blob,

    constraint document_pk primary key (id),
    constraint document_unique unique (schemata, name, date),
    constraint document_schema_fk foreign key (schemata) references schemata (id),
    constraint autotodo_purpose_fk foreign key (purpose) references purpose (id)
);

create table fragment(
    id        uuid not null,
    origin    timestamp not null  default current_timestamp,
    schemata  uuid not null,
    name      varchar(1024) not null,
    document  uuid not null,
    text      varchar(32000),
    posstart  integer,
    posend    integer,

    constraint FRAGMENT_PK primary key (id),
    constraint fragment_unique unique (schemata, name, document),
    constraint FRAGMENT_DOCUMENT foreign key (document) references document(id)
);

create table fragmenttopic(
    fragment uuid not null,
    topic uuid not null,

    constraint fragmenttopic_pk primary key (fragment, topic),
    constraint fragmenttopic_fragment_fk foreign key (fragment) references fragment (id),
    constraint fragmenttopic_topic_fk foreign key (topic) references topic (id)
);

create table fragmentperson(
    fragment uuid not null,
    person uuid not null,

    constraint fragmentperson_pk primary key (fragment, person),
    constraint fragmentperson_fragment_fk foreign key (fragment) references fragment (id),
    constraint fragmentperson_person_fk foreign key (person) references person (id)
);