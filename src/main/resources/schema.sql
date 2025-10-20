drop table if exists answers;

drop table if exists comments;

drop table if exists bookings;

drop table if exists items;

drop table if exists requests;

drop table if exists users;

create table users
(
    id    serial primary key unique,
    name  varchar(255) not null,
    email varchar(512) not null unique
);

create table requests
(
    id           serial primary key unique,
    requestor_id bigint references users (id) on delete cascade not null,
    description  varchar(512)                                   not null,
    created      timestamp with time zone                       not null,
    updated      timestamp with time zone                       null
);

create table items
(
    id           serial primary key unique,
    name         varchar(255)                                   not null,
    description  varchar(512)                                   not null,
    is_available boolean                                        not null,
    owner_id     bigint references users (id) on delete cascade not null,
    request_id   bigint references requests (id)                null
);

create table bookings
(
    id         serial unique,
    start_date timestamp with time zone                       not null,
    end_date   timestamp with time zone                       not null,
    booker_id  bigint references users (id) on delete cascade not null,
    item_id    bigint references items (id) on delete cascade not null,
    status     varchar                                        not null
);

create table comments
(
    id        serial unique,
    text      varchar(512)                                   not null,
    item_id   bigint references items (id) on delete cascade not null,
    author_id bigint references users (id) on delete cascade not null,
    created   timestamp with time zone                       not null,
    updated   timestamp with time zone                       null
);

create table answers
(
    id         serial unique,
    item_id    bigint references items (id) on delete cascade    not null,
    request_id bigint references requests (id) on delete cascade not null
);