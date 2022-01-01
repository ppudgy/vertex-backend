-- Файл создания базы данных проекта "apex"
-- project: apex
-- file: security.sql
-- user: pudgy
-- time: 23.12.2021


create table refresh_token (
    id uuid not null,
    username varchar(256) not null,
    refresh_token varchar(1024) not null,
    revoked int,
    date_created timestamp default current_timestamp,

    constraint pk_refresh_token primary key (id),
    constraint unq_refresh_token unique (username),
    constraint chk_refresh_token check revoked in (0,1)
);
