create table account(
    id uuid not null,
    origin timestamp not null default current_timestamp,
    schemata uuid not null,
    name varchar(1024) not null,
    passwd varchar(1024) not null,
    email varchar(1024) not null,
    enable boolean not null default false,
    language  varchar(32) not null,
    phone varchar(255),
    iconf varchar(36000),

    constraint user_pk primary key (id),
    constraint user_unique unique (schemata, name),
    constraint user_email_unique unique(email),
    constraint user_schemata foreign key (schemata) references schemata(id),
    constraint user_language foreign key (language) references language(id)
);


insert into account (id, schemata, name, passwd, email, language) values ('3d1520f7-87a5-4c31-ae07-b8774b1fc1f0', '3d1520f7-87a5-4c31-ae07-b8774b1fc1f0', 'admin@mail.ru', '$2a$10$1RLCpfTVi0/AjP.KnOs6yO/PWany.zMjxHURHR0.3Iv/4edUEWdCK', 'sergeyschukin@mail.ru', 'en_GB');
insert into account (id, schemata, name, passwd, email, language) values ('4bc059da-66db-4684-a7ba-b0d8656c1c96', '4bc059da-66db-4684-a7ba-b0d8656c1c96', 'ya.pudgy@yandex.ru', '$2a$10$1RLCpfTVi0/AjP.KnOs6yO/PWany.zMjxHURHR0.3Iv/4edUEWdCK', 'ya.pudgy@yandex.ru', 'ru_RU');
insert into account (id, schemata, name, passwd, email, language) values ('4dbf84f4-ca50-4eeb-88ed-ff07ffedce87', '916e7f18-efb8-4a1a-8b5b-275c319b88d8', 'test@mail.ru', '$2a$10$1RLCpfTVi0/AjP.KnOs6yO/PWany.zMjxHURHR0.3Iv/4edUEWdCK', 'sergey.schukin@mail.ru', 'ru_RU');



-- end of file