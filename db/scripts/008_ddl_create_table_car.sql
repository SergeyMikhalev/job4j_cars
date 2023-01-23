create table car(
    id serial primary key,
    engine_id int not null unique references engine(id)
);