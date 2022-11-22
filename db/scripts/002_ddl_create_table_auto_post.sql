create table auto_post(
    id serial primary key,
    description text not null ,
    created timestamp without time zone not null,
    auto_user_id int not null references auto_user(id)
);