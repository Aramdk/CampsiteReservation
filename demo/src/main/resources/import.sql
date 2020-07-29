create table RESERVATION
(
    ID                     BIGINT auto_increment
        primary key,
    BOOKING_ID             VARCHAR(255),
    RESERVATION_START_DATE TIMESTAMP,
    USER_ID                BIGINT,
    RESERVATION_END_DATE   TIMESTAMP,
    constraint FK_RESERVATION_USER_ID
        foreign key (USER_ID) references USER (ID)
);

create table USER
(
    ID    BIGINT auto_increment
        primary key,
    NAME  VARCHAR(255),
    EMAIL VARCHAR(255)
);

