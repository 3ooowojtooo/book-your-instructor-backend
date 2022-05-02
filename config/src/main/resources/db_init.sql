CREATE SEQUENCE hibernate_sequence;

CREATE TABLE "user" (
                        id int primary key,
                        email varchar not null unique,
                        origin varchar(20) not null,
                        password varchar check ( (origin = 'EXTERNAL' AND password IS NULL) OR (origin = 'CREDENTIALS' AND password IS NOT NULL) ),
                        type varchar(20) default 'UNDECLARED',
                        external_id varchar check ( (origin = 'CREDENTIALS' AND external_id IS NULL) OR (origin = 'EXTERNAL' AND external_id IS NOT NULL) ),
                        external_id_provider varchar(20) check ( (origin = 'CREDENTIALS' AND external_id_provider IS NULL) OR (origin = 'EXTERNAL' AND external_id_provider IS NOT NULL) )
);
