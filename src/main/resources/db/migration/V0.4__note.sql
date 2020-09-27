

create table note(
    id        uuid not null,
    origin    timestamp not null default current_timestamp,
    schemata  uuid not null,
    purpose   uuid not null,
    text      varchar(32000) not null,

    constraint note_pk primary key (id),
    constraint note_purpose_fk foreign key (purpose) references purpose(id),
    constraint note_schema_fk foreign key (schemata) references schemata(id)
);