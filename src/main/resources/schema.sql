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
    requestor_id bigint references users (id),
    description  varchar(512) not null,
    created      timestamp with time zone
);

create table items
(
    id           serial primary key unique,
    name         varchar(255)                    not null,
    description  varchar(512)                    not null,
    is_available boolean                         not null,
    owner_id     bigint references users (id)    not null,
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

create table answers
(
    id         serial unique,
    item_id    bigint references items (id),
    request_id bigint references requests (id)
);