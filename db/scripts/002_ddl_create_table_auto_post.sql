create table auto_post(
    id serial primary key,
    post_text text not null ,
    created timestamp without time zone not null,
    auto_user_id int not null references auto_user(id)
);