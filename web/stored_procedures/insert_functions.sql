DROP FUNCTION IF EXISTS insert_track;

DELIMITER $$

CREATE FUNCTION insert_track(track_id VARCHAR(25), name VARCHAR(100), track_number INT, duration_ms INT, album_id VARCHAR(25), artist_id VARCHAR(25))
    RETURNS VARCHAR(25)
    DETERMINISTIC
BEGIN
    -- Declare a variable that says if we're inserting a duplicate or not
    DECLARE track_in_table INT DEFAULT 1;
    SELECT EXISTS(SELECT * FROM track WHERE id=track_id LIMIT 1) INTO track_in_table;

    -- If a track is not in the table we can insert it
    IF (track_in_table = 0) THEN
        INSERT INTO track (id, name, track_number) VALUES (track_id, name, track_number);
        INSERT INTO track_meta(id, duration_ms) VALUES (track_id, duration_ms);
        INSERT INTO track_in_album (track_id, album_id) VALUES (track_id, album_id);
        INSERT INtO artist_in_track(track_id, artist_id) VALUES (track_id, artist_id);
        RETURN track_id;
    ELSE
        RETURN "duplicate id";
    END IF;
END
$$

DELIMITER ;

DROP FUNCTION IF EXISTS insert_album;

DELIMITER $$

CREATE FUNCTION insert_album(
    album_id VARCHAR(25),
    name VARCHAR(100),
    album_type VARCHAR(25),
    image VARCHAR(200),
    release_date VARCHAR(40),
    artist_id VARCHAR(25)
)
    RETURNS VARCHAR(25)
    DETERMINISTIC
BEGIN
    -- Declare a variable that says if we're inserting a duplicate or not
    DECLARE album_in_table INT DEFAULT 1;
    SELECT EXISTS(SELECT * FROM album WHERE id=album_id LIMIT 1) INTO album_in_table;

    -- If a track is not in the table we can insert it
    IF (album_in_table = 0) THEN
        INSERT INTO album (id, name, album_type, release_date, image) VALUES (album_id, name, album_type, release_date, image);
        INSERT INTO artist_in_album (artist_id, album_id) VALUES (artist_id, album_id);
        RETURN album_id;
    ELSE
        RETURN "duplicate id";
    END IF;
END
$$

DELIMITER ;

DROP FUNCTION IF EXISTS insert_artist;

DELIMITER $$

CREATE FUNCTION insert_artist(
    artist_id VARCHAR(25),
    name VARCHAR(100),
    image VARCHAR(200)
)
    RETURNS VARCHAR(25)
    DETERMINISTIC
BEGIN
    -- Declare a variable that says if we're inserting a duplicate or not
    DECLARE artist_in_table INT DEFAULT 1;
    SELECT EXISTS(SELECT * FROM artist WHERE id=artist_id LIMIT 1) INTO artist_in_table;

    -- If a track is not in the table we can insert it
    IF (artist_in_table = 0) THEN
        INSERT INTO artist (id, name, image) VALUES (artist_id, name, image);
        RETURN artist_id;
    ELSE
        RETURN "duplicate id";
    END IF;
END
$$

DELIMITER ;