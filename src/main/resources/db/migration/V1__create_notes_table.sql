CREATE table notes (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title varchar(255),
    content text
);