CREATE TYPE USER_ORIGIN AS ENUM ('CREDENTIALS', 'EXTERNAL');

CREATE TYPE USER_TYPE AS ENUM ('UNDECLARED', 'INSTRUCTOR', 'STUDENT');

CREATE TYPE EXTERNAL_IDENTITY_PROVIDER AS ENUM ('FACEBOOK');

CREATE TABLE "USER" (
                        id int primary key not null,
                        email varchar not null unique,
                        origin USER_ORIGIN not null,
                        password varchar check ( (origin = 'EXTERNAL'::USER_ORIGIN AND password IS NULL) OR (origin = 'CREDENTIALS'::USER_ORIGIN AND password IS NOT NULL) ),
                        type USER_TYPE default 'UNDECLARED',
                        external_id varchar check ( (origin = 'CREDENTIALS'::USER_ORIGIN AND external_id IS NULL) OR (origin = 'EXTERNAL'::USER_ORIGIN AND external_id IS NOT NULL) ),
                        external_id_provider EXTERNAL_IDENTITY_PROVIDER check ( (origin = 'CREDENTIALS'::USER_ORIGIN AND external_id_provider IS NULL) OR (origin = 'EXTERNAL'::USER_ORIGIN AND external_id_provider IS NOT NULL) )
);
