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
    id           serial unique,
    description  varchar(512) not null,
    requestor_id bigint references users (id)
);

create table items
(
    id           serial primary key unique,
    name         varchar(255)                    not null,
    description  varchar(512)                    not null,
    is_available boolean                         not null,
    owner_id     bigint references users (id)    null,
    request_id   bigint references requests (id) null
);

create table bookings
(
    id         serial unique,
    start_date timestamp with time zone,
    end_date   timestamp with time zone,
    booker_id  bigint references users (id),
    item_id    bigint references items (id),
    status     varchar not null
);

create table comments
(
    id        serial unique,
    text      varchar(512),
    item_id   bigint references items (id),
    author_id bigint references users (id),
    created   timestamp with time zone
);