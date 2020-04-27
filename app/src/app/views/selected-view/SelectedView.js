import React, { useState, useEffect } from "react";
import "./SelectedView.css";

import axios from "axios";

import { api } from "../../../utils/api";
import { Selection } from "../../containers";
import {
  SongSelection,
  ArtistSelection,
  AlbumSelection,
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

  function renderSelection() {
    if (match.params.contentType === "songs") {
      return <SongSelection song={song} songMeta={songMeta} />;
    } else if (match.params.contentType === "artists") {
      return <ArtistSelection artist={artist} artistAlbums={artistAlbums} />;
    } else if (match.params.contentType === "albums") {
      return <AlbumSelection album={album} tracksForAlbum={tracksForAlbum} />;
    }
  }

  return (
    <React.Fragment>
      {renderToast()}
      <Selection>{renderSelection()}</Selection>
    </React.Fragment>
  );
}

export default SelectedView;
