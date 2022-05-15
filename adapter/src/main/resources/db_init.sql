CREATE SEQUENCE hibernate_sequence START WITH 4;

CREATE TABLE "user" (
                        id int primary key,
                        email varchar not null unique,
                        origin varchar(20) not null,
                        password varchar check ( (origin = 'EXTERNAL' AND password IS NULL) OR (origin = 'CREDENTIALS' AND password IS NOT NULL) ),
                        type varchar(20) default 'UNDECLARED',
                        name varchar check ( (type = 'UNDECLARED' AND name IS NULL) OR (type != 'UNDECLARED' AND name IS NOT NULL)),
                        surname varchar check ( (type = 'UNDECLARED' AND surname IS NULL) OR (type != 'UNDECLARED' AND surname IS NOT NULL)),
                        external_id varchar check ( (origin = 'CREDENTIALS' AND external_id IS NULL) OR (origin = 'EXTERNAL' AND external_id IS NOT NULL) ),
                        external_id_provider varchar(20) check ( (origin = 'CREDENTIALS' AND external_id_provider IS NULL) OR (origin = 'EXTERNAL' AND external_id_provider IS NOT NULL) )
);
