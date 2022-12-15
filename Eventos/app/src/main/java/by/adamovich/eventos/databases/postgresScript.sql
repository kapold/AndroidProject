CREATE TABLE Users
(
    idUser SERIAL PRIMARY KEY,
    nickname VARCHAR(16) UNIQUE,
    name VARCHAR(32),
    surname VARCHAR(32),
    password TEXT,
    phoneNumber VARCHAR(16)
);

CREATE TABLE Types
(
    idType SERIAL PRIMARY KEY,
    type VARCHAR(32)
);

CREATE TABLE Events
(
    idEvent SERIAL PRIMARY KEY,
    idCreator INTEGER REFERENCES Users(idUser),
    name VARCHAR(64),
    idType INTEGER REFERENCES Types(idType),
    image BYTEA,
    place VARCHAR(64),
    time TEXT,
    date TEXT,
    capacity INTEGER CHECK(capacity > 0),
    occupied INTEGER CHECK(occupied < capacity) DEFAULT(0)
);

CREATE TABLE Requests
(
    idRequest SERIAL PRIMARY KEY,
    idSender INTEGER REFERENCES Users(idUser),
    idEvent INTEGER REFERENCES Events(idEvent),
    isStandby BOOLEAN default(true),
    isAccepted BOOLEAN default(null)
);

DROP TABLE requests;
DROP TABLE events;
DROP TABLE types;
DROP TABLE users;

INSERT INTO types (type)
    VALUES ('День рождения'), ('Копоратив'), ('Собрание'), ('Праздник');

