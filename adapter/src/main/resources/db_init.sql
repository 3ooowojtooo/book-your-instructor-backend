CREATE SEQUENCE hibernate_sequence START WITH 4;

CREATE TABLE "user"
(
    id                   int primary key,
    email                varchar     not null unique,
    origin               varchar(20) not null,
    password             varchar check ( (origin = 'EXTERNAL' AND password IS NULL) OR
                                         (origin = 'CREDENTIALS' AND password IS NOT NULL) ),
    type                 varchar(20) not null,
    name                 varchar not null,
    surname              varchar not null,
    external_id          varchar check ( (origin = 'CREDENTIALS' AND external_id IS NULL) OR
                                         (origin = 'EXTERNAL' AND external_id IS NOT NULL) ),
    external_id_provider varchar(20) check ( (origin = 'CREDENTIALS' AND external_id_provider IS NULL) OR
                                             (origin = 'EXTERNAL' AND external_id_provider IS NOT NULL) )
);

CREATE TABLE "event"
(
    id                               int primary key,
    version                          int                      not null,
    type                             varchar(20)              not null,
    instructor_id                    int references "user" (id) not null,
    student_id int references "user" (id),
    name                             varchar(100)             not null,
    description                      varchar(255),
    location                         varchar(255)             not null,
    status                           varchar(20)              not null,
    price                            decimal                  not null,
    creation_timestamp               timestamp with time zone not null,
    single_start_timestamp           timestamp without time zone,
    single_end_timestamp             timestamp without time zone,
    cyclic_start_time                time,
    cyclic_duration                  bigint,
    cyclic_day_of_week               varchar(20),
    cyclic_start_boundary            timestamp without time zone,
    cyclic_end_boundary              timestamp without time zone,
    cyclic_absence_event             bool,
    cyclic_absence_event_name        varchar(100),
    cyclic_absence_event_description varchar(255),
        CHECK (price > 0),
    CHECK ( (type = 'SINGLE' AND single_start_timestamp IS NOT NULL) OR
            (type != 'SINGLE' AND single_start_timestamp IS NULL)),
    CHECK ( (type = 'SINGLE' AND single_end_timestamp IS NOT NULL) OR
            (type != 'SINGLE' AND single_end_timestamp IS NULL)),
    CHECK ( (type = 'SINGLE' AND single_start_timestamp < single_end_timestamp) OR type != 'SINGLE'
        ),
    CHECK ((type = 'CYCLIC' AND cyclic_start_time IS NOT NULL) OR (type != 'CYCLIC' AND cyclic_start_time IS NULL)),
    CHECK ((type = 'CYCLIC' AND cyclic_duration IS NOT NULL) OR (type != 'CYCLIC' AND cyclic_duration IS NULL)),
    CHECK ((type = 'CYCLIC' AND cyclic_day_of_week IS NOT NULL) OR (type != 'CYCLIC' AND cyclic_day_of_week IS NULL)),
    CHECK ((type = 'CYCLIC' AND cyclic_start_boundary IS NOT NULL) OR
           (type != 'CYCLIC' AND cyclic_start_boundary IS NULL)),
    CHECK ((type = 'CYCLIC' AND cyclic_end_boundary IS NOT NULL) OR (type != 'CYCLIC' AND cyclic_end_boundary IS NULL)),
    CHECK ( (type = 'CYCLIC' AND cyclic_start_boundary < cyclic_end_boundary) OR type != 'CYCLIC'),
    CHECK (status = 'DRAFT' OR status = 'FREE' OR status = 'BOOKED' OR status = 'STUDENT_RESIGNED' OR status = 'INSTRUCTOR_ABSENT'),
    CHECK ((cyclic_absence_event = true AND cyclic_absence_event_name IS NOT NULL) OR
           (cyclic_absence_event = false AND cyclic_absence_event_name IS NULL)),
    CHECK ((cyclic_absence_event = false AND cyclic_absence_event_description IS NULL) OR cyclic_absence_event = true)
);


CREATE TABLE "event_realization"
(
    id              int primary key,
    event_id        int references "event" (id) not null,
    student_id      int references "user" (id),
    start_timestamp timestamp with time zone    not null,
    end_timestamp   timestamp with time zone    not null,
    status          varchar(20)                 not null,
    CHECK ( "start_timestamp" < "end_timestamp" ),
    CHECK (status = 'DRAFT' OR status = 'FREE' OR status = 'BOOKED' OR status = 'INSTRUCTOR_ABSENT' OR
           status = 'STUDENT_ABSENT' OR status = 'STUDENT_RESIGNED')
);

CREATE TABLE "event_lock"
(
    id                   int primary key,
    event_id             int references "event" (id) not null unique,
    event_version        int                         not null,
    user_id              int references "user" (id)  not null,
    expiration_timestamp timestamp with time zone    not null
);

CREATE TABLE "event_schedule"
(
    id                    int primary key,
    event_id              int references "event" (id)             not null,
    event_realization_id  int references "event_realization" (id) not null,
    student_id            int references "user" (id)              not null,
    instructor_id         int references "user" (id)              not null,
    status                varchar(20)                             not null,
    owner                 varchar(20)                             not null,
    CHECK (status = 'BOOKED' OR status = 'INSTRUCTOR_ABSENT' OR status = 'STUDENT_ABSENT'),
    CHECK (owner = 'STUDENT' OR owner = 'INSTRUCTOR')
);