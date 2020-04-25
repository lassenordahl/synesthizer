import React, { useState, useEffect } from "react";
import "./ContentView.css";

import { Redirect } from "react-router-dom";
import axios from "axios";

import { Button, SongCard, AlbumCard, ArtistCard } from "../../components";
import { Card } from "../../containers";
import { QueryParams } from "../../../hooks";
import { ExpandableCart } from "../../components";

import api from "../../../utils/api.js";

import { getRoute } from "../../../utils/api";

function ContentView(props) {
  // Selection Variables
  const [selectedCardId, setSelectedCardId] = useState(null);

  // Redirect Variables
  const [willRedirectSong, redirectSong] = useState(false);
  const [willRedirectArtist, redirectArtist] = useState(false);
  const [willRedirectAlbum, redirectAlbum] = useState(false);

  // Data Array
  const [albums, setAlbums] = useState([]);
  const [artists, setArtists] = useState([]);
  const [songs, setSongs] = useState([]);

  // Query Parameters
  let queryParams = QueryParams();

  // Match is passed in under props now because the <Route> component needed props and I had issues passing match normally
  let match = props.match;

  // Session Tracks
  const [sessionTracks, setSessionTracks] = useState([]);

  useEffect(() => {
    console.log(queryParams);
    getPlaylistSession();

    if (match.params.contentType === "albums") {
      getAlbums();
    } else if (match.params.contentType === "artists") {
      getArtists();
    } else if (match.params.contentType === "songs") {
      getSongs();
    }
  }, [match.params.contentType]);

  useEffect(() => {
    // Reset redirect variables where needed
    if (willRedirectArtist) {
      redirectArtist(false);
    }
    if (willRedirectAlbum) {
      redirectAlbum(false);
    }
    if (willRedirectSong) {
      redirectSong(false);
    }
  }, [willRedirectArtist, willRedirectAlbum, willRedirectSong]);

  useEffect(() => {
    if (selectedCardId === null) {
      return;
    }

    if (match.params.contentType === "albums") {
      redirectAlbum(true);
    } else if (match.params.contentType === "artists") {
      redirectArtist(true);
    } else if (match.params.contentType === "songs") {
      redirectSong(true);
    }
  }, [selectedCardId]);

  function getAlbums() {
    // axios
    axios
      .get(api.albums)
      .then(function (response) {
        console.log(response);
        setAlbums(response.data.albums);
        // setTimeout(() => setAlbums(response.data), 1000);
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function getArtists() {
    axios
      .get(api.artists)
      .then(function (response) {
        console.log(response);
        setArtists(response.data.artists);
        // setTimeout(() => setArtists(response.data.artists), 1000);
      })
      .catch(function (error) {
        console.log(error);
      });
  }

  function getSongs() {
    axios
      .get(api.songs)
      .then(function (response) {
        console.log(response);
        setSongs(response.data.songs);
        // setTimeout(() => setSongs(response.data.songs), 1000);
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function selectCard(id) {
    setSelectedCardId(id);
  }

  // Get what songs are in the playlist once in the parent so each card can know if it's selected or not
  function getPlaylistSession() {
    axios
      .get(api.playlistSession)
      .then(function (response) {
        console.log(response);
        setSessionTracks(response.data.tracks);
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function addToSession(songId) {
    axios
      .post(api.playlistSessionTrack, {
        id: songId,
      })
      .then(function (response) {
        console.log(response);
        getPlaylistSession();
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function removeFromSession(songId) {
    axios
      .delete(api.playlistSessionTrack, {
        params: { id: songId },
      })
      .then(function (response) {
        console.log(response);
        getPlaylistSession();
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function isInSession(songId) {
    for (let i = 0; i < sessionTracks.length; i++) {
      if (songId === sessionTracks[i].id) {
        return true;
      }
    }
    return false;
  }

  function renderContentCards() {
    if (match.params.contentType === "songs") {
      return (songs.length > 0
        ? songs
        : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
      ).map(function (song, index) {
        return (
          <SongCard
            song={song}
            key={index}
            style={{ margin: "32px" }}
            onClick={() => {
              if (songs.length > 0) selectCard(song.id);
            }}
            skeletonPulse={songs.length > 0 ? undefined : true}
            addToSession={addToSession}
            removeFromSession={removeFromSession}
            isInSession={isInSession(song.id)}
          ></SongCard>
        );
      });
    } else if (match.params.contentType === "albums") {
      return (albums.length > 0
        ? albums
        : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
      ).map(function (album, index) {
        return (
          <AlbumCard
            album={album}
            key={index}
            style={{ margin: "32px" }}
            onClick={() => {
              if (albums.length > 0) selectCard(album.id);
            }}
            skeletonPulse={albums.length > 0 ? undefined : true}
            addToSession={addToSession}
            removeFromSession={removeFromSession}
          ></AlbumCard>
        );
      });
    } else if (match.params.contentType === "artists") {
      return (artists.length > 0 ? artists : [1, 2, 3, 4, 5, 6, 7, 8]).map(
        function (artist, index) {
          return (
            <ArtistCard
              artist={artist}
              key={index}
              style={{ margin: "32px" }}
              onClick={() => {
                if (artists.length > 0) selectCard(artist.id);
              }}
              skeletonPulse={artists.length > 0 ? undefined : true}
            ></ArtistCard>
          );
        }
      );
    }

    return null;
  }

  return (
    <div className="content-view">
      <ExpandableCart sessionTracks={sessionTracks} getsOwnData={false} removeFromSession={removeFromSession}/>
      {willRedirectAlbum ? (
        <Redirect push to={"/app/explore/albums/" + selectedCardId}></Redirect>
      ) : null}
      {willRedirectArtist ? (
        <Redirect push to={"/app/explore/artists/" + selectedCardId}></Redirect>
      ) : null}
      {willRedirectSong ? (
        <Redirect push to={"/app/explore/songs/" + selectedCardId}></Redirect>
      ) : null}
      <div className="content-view-content">
        <div className="content-view-search">
          <input></input>
          <div style={{ width: "48px" }}></div>
          <Button isPrimary={true}>Search</Button>
        </div>
        <div className="content-view-filter-wrapper">
          <Card
            className="content-view-filter"
            innerStyle={{
              display: "flex",
              flexDirection: "row",
              margin: "8px 24px 8px 24px",
            }}
          >
            <p>Song</p>
            <p>Album</p>
            <p>Artist</p>
            <p>Popularity</p>
          </Card>
        </div>
        <div className="content-view-cards">{renderContentCards()}</div>
        <div
          className="content-view-filter-wrapper"
          style={{ marginTop: "64px", marginBottom: "0px" }}
        >
          <Card
            className="content-view-filter"
            innerStyle={{
              display: "flex",
              flexDirection: "row",
              margin: "8px 24px 8px 24px",
            }}
          >
            <p>1</p>
          </Card>
        </div>
      </div>
    </div>
  );
}

export default ContentView;
