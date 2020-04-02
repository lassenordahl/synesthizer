DROP TABLE track;
DROP TABLE track_meta;
DROP TABLE album;
DROP TABLE track_in_album;
DROP TABLE artist;
DROP TABLE artist_in_track;
DROP TABLE genre;
DROP TABLE genre_in_track;
DROP TABLE customer;
DROP TABLE sale;
DROP TABLE credit_card;
DROP TABLE rating;

CREATE TABLE track_meta (
    id VARCHAR(10) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE track (
    id VARCHAR(10) NOT NULL,
    title VARCHAR(100) NOT NULL,
    artist_id VARCHAR(10) NOT NULL,
    album_id VARCHAR(10) DEFAULT "",
    year INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES track_meta(id)
);

CREATE TABLE album (
    id VARCHAR(10) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE track_in_album (
    track_id VARCHAR(10) NOT NULL,
    album_id VARCHAR(10) NOT NULL,
    FOREIGN KEY (track_id) REFERENCES track(id),
    FOREIGN KEY (album_id) REFERENCES album(id)
);

CREATE TABLE artist (
    id VARCHAR(10) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE artist_in_track (
    artist_id VARCHAR(10) NOT NULL,
    track_id VARCHAR(10) NOT NULL,
    FOREIGN KEY (artist_id) REFERENCES artist(id),
    FOREIGN KEY (track_id) REFERENCES track(id)
);

CREATE TABLE genre (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR (32) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE genre_in_track (
    genre_id INTEGER NOT NULL,
    track_id VARCHAR(10) NOT NULL,
    FOREIGN KEY (genre_id) REFERENCES genre(id),
    FOREIGN KEY (track_id) REFERENCES track(id)
);

CREATE TABLE credit_card (
    id VARCHAR(20) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    expiration DATE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE customer (
    id INTEGER NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    cc_id VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cc_id) REFERENCES credit_card(id)
);

CREATE TABLE sale (
    id INTEGER NOT NULL AUTO_INCREMENT,
    customer_id INTEGER NOT NULL,
    track_id VARCHAR(10) NOT NULL,
    sale_DATE DATE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (track_id) REFERENCES track(id)
);

CREATE TABLE rating (
    track_id VARCHAR(10) NOT NULL,
    rating FLOAT NOT NULL,
    num_votes INTEGER NOT NULL,
    FOREIGN KEY (track_id) REFERENCES track(id)
);
