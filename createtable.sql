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
    id varchar(10) not null,
    primary key (id)
);

CREATE TABLE track (
    id varchar(10) not null,
    title varchar(100) not null,
    artist_id varchar(10) not null,
    album_id varchar(10) default "",
    year integer not null,
    primary key (id),
    foreign key (id) references track_meta(id)
);

CREATE TABLE album (
    id varchar(10) not null,
    primary key (id)
);

CREATE TABLE track_in_album (
    track_id varchar(10) not null,
    album_id varchar(10) not null,
    foreign key (track_id) references track(id),
    foreign key (album_id) references album(id)
);

CREATE TABLE artist (
    id varchar(10) not null,
    primary key (id)
);

CREATE TABLE artist_in_track (
    artist_id varchar(10) not null,
    track_id varchar(10) not null,
    foreign key (artist_id) references artist(id),
    foreign key (track_id) references track(id)
);

CREATE TABLE genre (
    id integer not null AUTO_INCREMENT,
    name varchar (32) not null,
    primary key (id)
);

CREATE TABLE genre_in_track (
    genre_id integer not null,
    track_id varchar(10) not null,
    foreign key (genre_id) references genre(id),
    foreign key (track_id) references track(id)
);

CREATE TABLE credit_card (
    id varchar(20) not null,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    expiration date not null,
    primary key (id)
);

CREATE TABLE customer (
    id integer not null AUTO_INCREMENT,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    cc_id varchar(20) not null,
    address varchar(200) not null,
    email varchar(50) not null,
    password varchar(20) not null,
    primary key (id),
    foreign key (cc_id) references credit_card(id)
);

CREATE TABLE sale (
    id integer not null AUTO_INCREMENT,
    customer_id integer not null,
    track_id varchar(10) not null,
    sale_date date not null,
    primary key (id),
    foreign key (customer_id) references customer(id),
    foreign key (track_id) references track(id)
);

CREATE TABLE rating (
    track_id varchar(10) not null,
    rating float not null,
    num_votes integer not null,
    foreign key (track_id) references track(id)
);
