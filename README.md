# Project 4 README

# CS122B Songs Project

Lasse Nordahl and Zachary Pinto - Team 53

URL: https://ec2-3-94-82-6.compute-1.amazonaws.com:8443/unnamed/landing

## Video Links

### Project 1

- https://www.youtube.com/watch?v=t2Wi-SXb-Z4

### Project 2

- https://youtu.be/vKfiHp3Gv6g

### Project 3

- https://youtu.be/jVCjuwmYFyg

### Project 4

- insertvideohere

## Custom Domain Information

We're using a custom domain for our project, which is a playlist generator for Spotify.

### Schema

![Project%202%20README/Screen_Shot_2020-04-27_at_5.50.00_PM.png](https://user-images.githubusercontent.com/13127625/80444804-791c7900-88c7-11ea-9529-0b587d6449a5.png)

Custom domain schema

The above schema also includes one new table (Spotify snapshots)

```
CREATE TABLE playlist_spotify_snapshot (
    playlist_id INTEGER NOT NULL,
    snapshot_id VARCHAR(100) NOT NULL,
    FOREIGN KEY (playlist_id) REFERENCES playlist(id)
);
```

## Domain Specific Project Changes

Our project domain didn't change the implementation of these features much for this project. It mostly changes the content that was drawn.

### Web

The auto-complete front-end for the search parameters on the front-end site contain information that is specific to the songs.

### Android

On the android end, the content shown on the selected items is changed. Below lists the following information exchanges we used for the different pages.

- Title → Song Name
- Year → Release Date
- Director → Artist Name
- Genres → Album Name
- 3 Stars → Popularity
- All information about the movie → Track Meta we have collected from Spotify. This includes metrics like danceability, wordiness, etc

## Project Contributions

### Lasse Nordahl

- Application routing for new pages
- Cart/Playlist generation functionality for Songs and Albums (Front-end and Back-end)
- Create Playlist Page
- Playlists page
- Playlist Endpoints
- Playlist Session Endpoints
- Spotify Integration
- Create playlist functionality on playlist page
- Implicit Grant Flow on Front-End for Spotify
- Spotify Snapshot SQL and Endpoints
- ReCaptcha integration
- Password Encruption
- Prepared Statement Conversion
- Dashboard Meta Endpoints
- Insert Track/Artist/Album stored procedures and endpoints
- Front-end of Dashboard
- Created android app
- Edited backend endpoints for android integration

### Zachary Pinto

- Write SQL schema
- Write scripts to generate sample data
- Create song selection and artist selection pages
- Create tracks, track, artist, and artists endpoint
- Create SQLClient class
- Connect the API to list views
- Configure for deployment
- Deploy to AWS
- Sorting for albums, artists, songs
- Searching for albums, artists, songs
- Browse by title and genre
- Links genres from artists
- Add query params to URL
- Update user
- Login
- Create user
- HTTPS Conversion
- Employee integration to Login
- XML Creation
- XML Parsing/Insertion/Optimization
- Inconsistency report

## Deployment / Demo, kept from Project 3

```bash
#config file
# env
env=prod

# SQL Configs and Secrets
db.type=mysql
db.name=cs122b
db.username=cs122b
db.password=team53

# Recaptcha
recaptcha.secret=6LecPfEUAAAAAF0dXtUXO7v1o4ePbyRokof5taJh
```

```bash
## Deployment Instructions

#start tomcat server
source /home/ubuntu/tomcat/bin/startup.sh

#Database Setup
mysql -u cs122b -p --database=cs122b < sql_creation/CreationInsertion.sql

#Encrypt Passwords
cd cs122b-spring20-project3-encryption-example/
mvn exec:java -Dexec.mainClass="UpdateSecurePassword"

#add employee creds
"classta@email.edu" and password "classta"

git clone https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53.git

cd cs122b-spring20-team53

git log

cd src/resources
vim config.properties

# Show DB
mysql -u cs122b -p -e "use cs122b;select count(*) from artist;select count(*) from album;select count(*) from track;select count(*) from track_meta;"

mysql -u cs122b -p -e "use cs122b; select * from user;"

# check the currently deployed apps
ls -lah /home/ubuntu/tomcat/webapps

# run react app build
cd cs122b-spring20-team-53/app
# run one of the following
# for HTTPS
REACT_APP_API_URL=https://ec2-3-94-82-6.compute-1.amazonaws.com:8443/unnamed/api npm run-script build
# for HTTP
REACT_APP_API_URL=http://ec2-3-94-82-6.compute-1.amazonaws.com:8080/unnamed/api npm run-script build

# copy build to web dir
npm run-script predeploy

# build artifact
cd ..
mvn package

#start tomcat
starttomcat

# run parser here
commands are below
cd ..
mkdir xml_creation
cp xml_creation/ cs122b/data_collection_creation/xml_creation

# put artifact in deployable dir
cp target/unnamed.war ~/tomcat/webapps

# check currently deployed apps
ls -lah /home/ubuntu/tomcat/webapps

# execute parser

#add employee creds
"classta@email.edu" and password "classta"

cd ..
mkdir xml_creation
cp xml_creation/ cs122b/data_collection_creation/xml_creation

#artists
mvn exec:java -Dexec.mainClass="com.cs122b.parser.MainParser" -Dexec.cleanupDaemonThreads=false -Dexec.args="data_collection_creation/xml_creation/artists.xml" 

#albums
mvn exec:java -Dexec.mainClass="com.cs122b.parser.MainParser" -Dexec.cleanupDaemonThreads=false -Dexec.args="data_collection_creation/xml_creation/albums.xml" 
#track
mvn exec:java -Dexec.mainClass="com.cs122b.parser.MainParser" -Dexec.cleanupDaemonThreads=false -Dexec.args="data_collection_creation/xml_creation/tracks.xml" 

#track_metas
mvn exec:java -Dexec.mainClass="com.cs122b.parser.MainParser" -Dexec.cleanupDaemonThreads=false -Dexec.args="data_collection_creation/xml_creation/track_metas.xml" 

#search things inserted from xml
Searchable Artists
- Guitar Duo
- L’Orchestra Cinematique

Searchable Albums
- In'terview in Concert
- Midnight Mushrumps

Searchable Tracks
- Missed Calls (feat. Hayley Kiyoko)
- Good In Goodbye

# dashboard demo
Songs To Add From Dashboard
When You Come Back Down
- N~~ickel Creek~~

When You Come Back Down
- Kina Grannis

When You Come Back Down
- The UVM Top Cats
```
## Fuzzy Search Implementation

```bash
USE cs122b;

DROP FUNCTION IF EXISTS fuzzy;
DROP FUNCTION IF EXISTS SPLIT_STR;

CREATE FUNCTION SPLIT_STR(
  x VARCHAR(255),
  delim VARCHAR(12),
  pos INT
)
RETURNS VARCHAR(255)
RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(x, delim, pos),
       LENGTH(SUBSTRING_INDEX(x, delim, pos -1)) + 1),
       delim, '');

DELIMITER $$

CREATE FUNCTION fuzzy(name VARCHAR(1000), search VARCHAR(1000))
        RETURNS INT
BEGIN
        DECLARE pass_count INT DEFAULT 0;
    DECLARE i INT Default 0;
        DECLARE search_sub VARCHAR(255);
        search_loop: LOOP
                SET i = i + 1;
                SET search_sub=SPLIT_STR(search, " ", i);
                IF search_sub = '' THEN
                        LEAVE search_loop;
                END IF;
        IF edrec(search_sub,name,2) THEN
                        SET pass_count = pass_count + 1;
                END IF;
        END LOOP search_loop;
        RETURN pass_count = i - 1;
END
$$
```
