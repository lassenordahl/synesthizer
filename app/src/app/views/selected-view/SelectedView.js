import React, { useState, useEffect } from "react";
import "./SelectedView.css";

import axios from "axios";

import { api } from "../../../utils/api";
import { Selection } from "../../containers";
import {
  SongSelection,
  ArtistSelection,
  AlbumSelection,
  ExpandableCart,
} from "../../components";
import { useToast } from "../../../hooks";

function SelectedView({ props, match }) {
  // Data variables
  const [song, setSong] = useState(null);
  const [songMeta, setSongMeta] = useState(null);
  const [artist, setArtist] = useState(null);
  const [artistAlbums, setArtistAlbums] = useState([]);
  const [album, setAlbum] = useState(null);
  const [tracksForAlbum, setTracksForAlbum] = useState([]);

  // Session Tracks
  const [sessionTracks, setSessionTracks] = useState([]);
  const [sessionAlbums, setSessionAlbums] = useState([]);

  // Toaster
  const [showSuccess, showError, renderToast] = useToast();

  useEffect(() => {
    if (match.params.contentType === "songs") {
      getSong();
    } else if (match.params.contentType === "artists") {
      getArtist();
    } else if (match.params.contentType === "albums") {
      getAlbum();
    }
    getPlaylistSession();
  }, [match.params.contentType]);

  function getSong() {
    axios
      .get(api.song, {
        params: {
          id: match.params.itemId,
        },
      })
      .then(function (response) {
        setSong(response.data.song);
        getSongMeta();
      })
      .catch(function (error) {
        console.error(error);
        showError("Error retrieving song");
      });
  }

  function getSongMeta() {
    axios
      .get(api.songMeta, {
        params: {
          id: match.params.itemId,
        },
      })
      .then(function (response) {
        setSongMeta(response.data);
      })
      .catch(function (error) {
        console.error(error);
        showError("Error retrieving song metadata");
      });
  }

  function getArtist() {
    axios
      .get(api.artist, {
        params: {
          id: match.params.itemId,
        },
      })
      .then(function (response) {
        setArtist(response.data.artist);
        getArtistAlbums();
      })
      .catch(function (error) {
        console.error(error);
        showError("Error retrieving artist");
      });
  }

  function getArtistAlbums() {
    axios
      .get(api.albums, {
        params: {
          artist_id: match.params.itemId,
        },
      })
      .then(function (response) {
        console.log(response);
        setArtistAlbums(response.data.albums);
      })
      .catch(function (error) {
        console.error(error);
        showError("Error retrieving artist's albums");
      });
  }

  function getAlbum() {
    axios
      .get(api.album, {
        params: {
          id: match.params.itemId,
        },
      })
      .then(function (response) {
        setAlbum(response.data);
        getTracksForAlbum();
      })
      .catch(function (error) {
        console.error(error);
        showError("Error retrieving album");
      });
  }

  function getTracksForAlbum() {
    axios
      .get(api.tracksForAlbum, {
        params: {
          id: match.params.itemId,
        },
      })
      .then(function (response) {
        console.log(response);
        setTracksForAlbum(response.data);
      })
      .catch(function (error) {
        console.error(error);
        showError("Error retrieving album tracks");
      });
  }

  // Get what songs are in the playlist once in the parent so each card can know if it's selected or not
  function getPlaylistSession() {
    axios
      .get(api.playlistSession)
      .then(function (response) {
        console.log(response);
        setSessionTracks(response.data.tracks);
        setSessionAlbums(response.data.albums);
      })
      .catch(function (error) {
        console.error(error);
        showError("Error retrieving playlist");
      });
  }

  function addToSession(id, itemType) {
    axios
      .post(
        itemType === "track"
          ? api.playlistSessionTrack
          : api.playlistSessionAlbum,
        {
          id: id,
        }
      )
      .then(function (response) {
        console.log(response);
        getPlaylistSession();
        if (response.status === 200) {
          showSuccess("Successfully added to playlist");
        }
      })
      .catch(function (error) {
        console.error(error);
        showError("Error adding to playlist");
      });
  }

  function removeFromSession(id, itemType) {
    axios
      .delete(
        itemType === "track"
          ? api.playlistSessionTrack
          : api.playlistSessionAlbum,
        {
          params: { id: id },
        }
      )
      .then(function (response) {
        console.log(response);
        getPlaylistSession();
        if (response.status === 200) {
          showSuccess("Successfully removed from playlist");
        }
      })
      .catch(function (error) {
        console.error(error);
        showError("Error removing from playlist");
      });
  }

  function isInTrackSession(songId) {
    for (let i = 0; i < sessionTracks.length; i++) {
      if (songId === sessionTracks[i].id) {
        return true;
      }
    }
    return false;
  }

  function isInAlbumSession(albumId) {
    for (let i = 0; i < sessionAlbums.length; i++) {
      if (albumId === sessionAlbums[i].id) {
        return true;
      }
    }
    return false;
  }

  function renderSelection() {
    if (match.params.contentType === "songs") {
      return (
        <SongSelection
          song={song}
          songMeta={songMeta}
          isInSession={song !== null ? isInTrackSession(song.id) : false}
          addToSession={addToSession}
          removeFromSession={removeFromSession}
        />
      );
    } else if (match.params.contentType === "artists") {
      return <ArtistSelection artist={artist} artistAlbums={artistAlbums} />;
    } else if (match.params.contentType === "albums") {
      return (
        <AlbumSelection
          album={album}
          tracksForAlbum={tracksForAlbum}
          isInSession={album !== null ? isInAlbumSession(album.id) : false}
          addToSession={addToSession}
          removeFromSession={removeFromSession}
        />
      );
    }
  }

  return (
    <React.Fragment>
      {renderToast()}
      <ExpandableCart
        sessionTracks={sessionTracks}
        sessionAlbums={sessionAlbums}
        getsOwnData={false}
        removeFromSession={removeFromSession}
      />
      <Selection>{renderSelection()}</Selection>
    </React.Fragment>
  );
}

export default SelectedView;
