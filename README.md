# Project 3 README

# CS122B Songs Project

Lasse Nordahl and Zachary Pinto - Team 53

URL: https://ec2-3-94-82-6.compute-1.amazonaws.com:8443/unnamed/landing

## Note on Dashboard

When attempting to use the dashboard to add songs, it may be difficult to find songs that are not added already. Try to think of weird names and obscure songs. Here is one that should not already be added:

- When You Come Back Down
  - Kina Grannis

## Video Links

### Project 1

- https://www.youtube.com/watch?v=t2Wi-SXb-Z4

### Project 2

- https://youtu.be/vKfiHp3Gv6g

### Project 3

- https://youtu.be/jR0fzJdOtPQ

## Deployment Instructions

Before creating the artifact, the react app needs to be built and copied over to the web directory of the project.

```
cd cs122b-spring20-team-53
mysql -u cs122b -p -e "show databases;"

mysql -u cs122b -p < create_table.sql

cd ..
mysql -u cs122b -p --database=cs122b < CreationInsertion.sql

mysql -u cs122b -p -e "use cs122b;select count(*) from artist;select count(*) from track;"

#start tomcat server
source /home/ubuntu/tomcat/bin/startup.sh

# run react app build
cd cs122b-spring20-team-53/app
npm install
REACT_APP_API_URL=http://ec2-3-94-82-6.compute-1.amazonaws.com:8080/unnamed/api npm run-script build

# copy build to web dir
npm run-script predeploy

# build artifact
cd ..
mvn package

# check the currently deployed apps
ls -lah /home/ubuntu/tomcat/webapps

# put artifact in deployable dir
cp target/unnamed.war ~/tomcat/webapps

# check currently deployed apps
ls -lah /home/ubuntu/tomcat/webapps
```

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

### Queries with Prepared Statements

For our code, we created Services that we used to assist the Servlet GET/POST/PUT endpoints. All of our code (prepared statements) for these endpoints are located in these services. Below are all of the endpoints that take user information.

**Tracks -** [https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/src/main/java/com/cs122b/service/TrackService.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/src/main/java/com/cs122b/service/TrackService.java)

```
GET track
GET tracks
GET track/meta
POST track
```

**Albums -** [https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/src/main/java/com/cs122b/service/AlbumService.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/src/main/java/com/cs122b/service/AlbumService.java)

```
GET album
GET albums
GET album/tracks
POST album
```

**Artists -** [https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/src/main/java/com/cs122b/service/ArtistService.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/src/main/java/com/cs122b/service/ArtistService.java)

```
GET artist
GET artists
POST artist
```

**Playlists -** [https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/src/main/java/com/cs122b/service/PlaylistService.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/src/main/java/com/cs122b/service/PlaylistService.java)

```
GET playlis
GET playlists
POST playist (Uses session, not any body params)
POST playlist/snapshot
```

**User -** [https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/src/main/java/com/cs122b/service/UserService.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/src/main/java/com/cs122b/service/UserService.java)

```
GET user
POST user
```

**Employee** [https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/src/main/java/com/cs122b/service/UserService.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/src/main/java/com/cs122b/service/UserService.java)

```
GET employee
POST employee
```

### Custom Domain Feature Substitutions

1. Captchas
   1. Captchas have been added to the login, sign up, and update account pages
2. Prepared Statements
   1. All queries requiring any form of input have been converted to prepared statements
3. Employee Dashboard (Located under `app/_dashboard`)
   1. Login to Dashboard with Employee Credentials
      1. Employees can login using special employee credentials. These credentials are in a different table from the normal user accounts. They log in using the same page however on `app/account/login`, the test login you should use is:
         1. Username/Email:
         2. Password:
   2. Show database metadata
      1. Two elements of Dashboard metadata are located on the dashboard. At the top we see the numbers of how many rows are in each of the 3 main data element tables. At the bottom there is a card with all of the tables and their respective attributes/types pulled `GET database/meta` endpoint.
   3. Add a new star/movie successfully
      1. On the dashboard, on the right side of the page, there is a card listed "Spotify Songs". Using this search bar, you can search for Spotify songs to add to the database. Selecting a song will autofill the artist, album, and song fields. These fields can be individually added to the database, but in order to ensure data validity, they must be added in the order of artist → album → song, or else an error will show due to not having the correct valid foreign key order.
      2. If an artist or album has already been added, the track or album can be added to the database normally.
      3. This prevention of inserting values with invalid ID's ensures that our data is valid for creating Spotify playlists. Because we get our track images from album arts through table joins, the entire flow of data needs to be added at a time for our custom domain. Error handling happens for all invalid data.
      4. All added values are accessible on the search view. To see immediate changes in the database numbers, look at the "Database Information" numbers above for row couunts.
   4. Show Error Message for existing movie
      1. Toasts will show up in the top right corner in a stack for any invalid/valid insertions of the data. Duplicate ID's for items are mentioned in the toasts.
   5. Error Handling
      1. All error handling happens through our Toast component, which is now a stack to handle multiple messages at a time, each takes 5s to disappear.
4. XML Parsing
   1. Main.xml
      1. We created another 2 playlists of 10,000 songs to add to our database using the XML Parsing. Data collection code using the Spotify api (Python Scripts) can be found in `data_collection_creation/data.py` in the root directory of the project.
      2. We then converted this data from JSON to xml using another script `xml_creator.py`
      3. We parsed through and inserted the data similarly to how we would with the normal movies.xml database schema.
   2. Report
      1. Optimizations:
         - Using batch insert instead of inserting each record one at a time greatly reduced the time that it took to parse and insert records. This is because several records can be committed at once, reducing the load on the DBMS.
         - Pulling Ids for the corresponding tables prior to adding a record to the batch. This reduced the number of records in the batch allowing the batch to be executed more quickly.
         - Checking the parsed data to see if it had proper fields prior to putting it into the batch. This also reduces the number of records in the batch and improves performance by removing the error handling from the DBMS.
      2. Inconsistency Data and Relevant Statistics can be found in the following directory [https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/tree/master/parse_reports](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/tree/master/parse_reports)
         - Final Stats can be found at the bottom of these files
      3. Performance Comparison (Both Approaches Use Batch Insert, since we originally implemented the parser this way)
         - Without Optimization:
           - artists.xml: 11.08s user 1.01s system 176% cpu 6.837 total
           - albums.xml: 13.47s user 1.33s system 171% cpu 8.621 total
           - tracks.xml: 17.23s user 2.41s system 96% cpu 20.336 total
           - track_metas.xml: 17.26s user 1.70s system 139% cpu 13.626 total
         - With Optimization
           - artists.xml: 9.35s user 0.88s system 185% cpu 5.521 total
           - albums.xml: 10.86s user 1.16s system 146% cpu 8.216 total
           - tracks.xml: 16.53s user 2.44s system 92% cpu 20.580 total
           - track_metas.xml: 13.98s user 1.53s system 136% cpu 11.355 total
      4. Custom Domain
         - We built our parser to handle duplicate data and xml elements that are missing data
      5. Testing Parsing
         - Artists inserted by parser:
           - Guitar Duo
           - L’Orchestra Cinematique
         - Albums inserted by parser
           - In'terview in Concert
           - Midnight Mushrumps
         - Songs inserted by parser
           - Missed Calls (feat. Hayley Kiyoko)
           - Good In Goodbye

## Project Contributions

### Lasse Nordahl

- Start React App
- Do main css
- Design song, artist, album list view
- Create track_meta, album, track_in_album endpoints
- Create album selection page
- Add hyperlinks to single view pages
- Add popularity sorting to album and song endpoint
- Application routing for new pages
- Cart/Playlist generation functionality for Songs and Albums (Front-end and Back-end)
- Create Playlist Page
- Playlists page
- Playlist Endpoints
- Playlist Session Endpoints
- Spotify Integration
  - Implicit Grant Flow on Front-End
  - Create spotify playlist functionality on playlist page
- Spotify Snapshot SQL and Endpoints
- ReCaptcha integration
- Password Encryption
- Prepared Statement Conversion
- Dashboard Meta Endpoints
- Insert Track/Artist/Album stored procedures and endpoints
- Front-end of Dashboard

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
