

create table todo(
    id           uuid not null,
    origin       timestamp not null default current_timestamp,
    schemata     uuid not null,
    purpose      uuid not null,
    external     boolean not null default false,
    done         boolean not null default false,
    relevance    timestamp,
    endtime      timestamp,
    description  varchar(32000),
    auto         integer not null default 0,
    autoperiod   integer not null default 0,

    constraint todo_pk primary key (id),
    constraint todo_schema_fk foreign key (schemata) references schemata (id),
    constraint todo_purpose_fk foreign key (purpose) references purpose (id)
);