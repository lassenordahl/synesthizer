# Project 2 README

# CS122B Songs Project

Lasse Nordahl and Zachary Pinto - Team 53

URL: http://ec2-3-94-82-6.compute-1.amazonaws.com:8080/unnamed/app

## Video Links

### Project 2

- https://www.youtube.com/watch?v=t2Wi-SXb-Z4

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

### Feature Substitutions

1. Searching
   - Our search parameters:
     - Search by `name`, `album name`, `artist name`, and `release date` across all pages
     - Search types change depending on what you're looking at. For instance, if you're on the _artist_ page, you cannot search by _song name_
   - Our sort parameters:
     - Sort by `popularity`, `name`, `release date`, and `album name`
     - Sort types change depending on what you're looking at. For instance, if you're on the _artist_ page, you cannot sort by _album name_
2. Movie List Page
   - Replaced by `Song Page`, `Album Page`, `Artist Page`, and `Playlist Page` (However, the playlist page has minimal features and is mostly used for Spotify integration)
     - Song Page displays:
       - Song Name (Hyperlinked)
       - Artists (Hyperlinked)
       - Release Date
       - Album Art
       - Popularity (# of playlists this song has been added to)
     - Album Page displays:
       - Album Name (Hyperlinked)
       - Artists (Hyperlinked)
       - Release Date
       - Album Art
       - Popularity (Sum of the # of playlists each song in the album has been added to)
     - Artist Page displays:
       - Artist Name (Hyperlinked)
   - Previous/Next button is at the bottom of the page
   - Number of listings can be changed from 20, to 40, to 80
   - Sorting is described in the section above (#1)
3. Single Page
   - Replaced by `Song Selected Page`, `Album Selected Page`, `Artist Selected Page`
     - Song Selected Page displays:
       - Song Name
       - Album (Hyperlinked)
       - Artists (Hyperlinked)
       - Album art
       - Song metadata (Note, liveliness, etc)
     - Album Selected Page displays:
       - Album Name
       - Artist Name (Hyperlinked)
       - Release date
       - Album art
       - Songs in album (Hyperlinked)
         - Song name, duration in seconds
     - Artist Selected Page displays:
       - Artist Name
       - Artist Genres
       - Albums (Hyperlinked)
4. Jump functionality

   - For single pages, that are accessed through the browser view, the go back to songs button can be clicked

     ![Project%202%20README/Screen_Shot_2020-04-27_at_7.28.56_PM.png](https://user-images.githubusercontent.com/13127625/80444583-e24fbc80-88c6-11ea-9adc-38d51cccb35f.png)

   - Pagination information is maintained through using the back button or the "go back to \_\_\_" button at the top

5. Browsing

   1. Browsing can be enabled through use of the browse vs search button

      - Browsing can search by `letter/number` for songs and albums
      - Browsing can search by `letter/number` and `genre` for artists (Genre data is only provided for artists from our scraped Spotify data)

      ![Project%202%20README/Screen_Shot_2020-04-27_at_7.31.53_PM.png](https://user-images.githubusercontent.com/13127625/80444605-e8de3400-88c6-11ea-91b3-9cdc7bc877ca.png)

6. Shopping Cart

   - Rather than building a "shopping cart", we opted to build a playlist session that would step by step generate playlists for the user
   - Rather than modifying quantity for tracks, we allowed the user to add both `albums` and `songs` to their playlist session
     - Duplicate tracks are removed on playlist generation, for example: if you add one song from a playlist, then add that playlist, it will not duplicate the track information.
   - Since we had all of the track data from Spotify, we opted to use that to create a Spotify playlist from the created playlist that the user made. This uses the `implicit grant flow` from Spotify, as we felt it had a good mix of complexity and security for the scope of our application.
     - [https://developer.spotify.com/documentation/general/guides/authorization-guide/](https://developer.spotify.com/documentation/general/guides/authorization-guide/)
   - Adding to cart:

     - Songs/Albums can be added to cart using the top right icon on the song/album card. When something is added to cart, the cart in the top right will update.

       ![Project%202%20README/Screen_Shot_2020-04-27_at_7.36.58_PM.png](https://user-images.githubusercontent.com/13127625/80444615-eed41500-88c6-11ea-8c13-30a5df296d7c.png)

       Add to cart in the top right

       ![Project%202%20README/Screen_Shot_2020-04-26_at_10.43.16_PM.png](https://user-images.githubusercontent.com/13127625/80444534-cb10cf00-88c6-11ea-8d36-b79c149fd7f7.png)

       Cart in top right of browsing pages

   - After navigating to the playlist/create page, the user can name a playlist, and save the playlist name in the session or create the playlist. After a playlist has been posted, the create playlist page will refresh, and the playlist will show up in the /playlists page
   - The user can click "Add to Spotify" on the playlist cards. This will trigger the implicit grant flow if the user does not have an authentication key.

     [https://developer.spotify.com/documentation/general/guides/authorization-guide/](https://developer.spotify.com/documentation/general/guides/authorization-guide/)

     1. If there is no authentication key in localstorage or the authentication key has expired, the application will redirect to the Spotify Auth page. Functionality for this can be found in the Playlists.js and PlaylistCard.js files in the react application under /app.
     2. After logging into the Spotify Auth page, the Spotify auth page will redirect back to the /playlists URL, where the application will save the authentication key and the time that it was saved. This authentication key has scope for editing/managing playlists for the user.
        1. Test Account for TA's
           1. **Username:** [`cs122b.test.spotify@gmail.com`](mailto:cs122b.test.spotify@gmail.com)
           2. **Password:** `chenlics122b`
     3. The app will then use the authentication key to make a request to the Spotify API, getting the users username.
        1. [https://developer.spotify.com/documentation/web-api/reference/users-profile/get-current-users-profile/](https://developer.spotify.com/documentation/web-api/reference/users-profile/get-current-users-profile/)
     4. The app will use the username to create a playlist with the name of the playlist that is being added to Spotify.
        1. [https://developer.spotify.com/console/post-playlists/](https://developer.spotify.com/console/post-playlists/)
     5. After a playlist is successfully created, the app then adds all the tracks from the playlist to the Spotify playlist.
        1. [https://developer.spotify.com/console/post-playlist-tracks/](https://developer.spotify.com/console/post-playlist-tracks/)
     6. After the tracks are added, the application saves the Spotify-generated snapshot ID in our database. This snapshot id can be used to see what happened during a given API request using the Spotify api. Which contains information of which tracks were added to the Spotify playlist. This is also used to know which playlists have already been added to Spotify.
        1. POST `api/playlist/snapshot`

7. Payment Page
   - Rather than having a payment page, we have a page that lists all of the playlists that have been generated and a create playlist page. This serves as the mediary between creating a playlist and the Spotify functionality.
   - For incorrect input, we have error messages that show for if a playlist doesn't have a valid name on playlist correction
8. Place Order Action

   - On playlist creation, the session playlist is added to the playlist table.

     ![Project%202%20README/Screen_Shot_2020-04-27_at_8.04.57_PM.png](https://user-images.githubusercontent.com/13127625/80444521-c51aee00-88c6-11ea-8e10-9d7976a17b36.png)

   - The "confirmation page" is the playlist page that have been generated. These show the image (we haven't implemented this yet, using a default one right now), name of the playlist, time it was generated, and an export to Spotify button.

     ![Project%202%20README/Screen_Shot_2020-04-25_at_8.56.03_PM.png](https://user-images.githubusercontent.com/13127625/80444625-f4c9f600-88c6-11ea-8cc0-0aab71fbfb6c.png)

9. Additional Performance Functionality
   - All of the browsing pages for songs, albums, and artists have the cart available. This cart can be dropped down on any of these pages to view the information.
   - The cart changes the numbers based on how many items are in the cart
     1. `<Song Count>` - `<Album Count>`
10. CSS Extra Credit
    - Spent way too much time on this but it ended up looking pretty neat!

## Substring Matching

For substring matching, we used SQL syntax such as :

```
WHERE track.name LIKE <name search string>
```

This adjusts what it is applied to based on what the user is searching for.

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
  - Create playlist functionality on playlist page
- Spotify Snapshot SQL and Endpoints

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
