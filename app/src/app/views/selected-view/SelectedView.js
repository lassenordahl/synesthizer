import React, { useState, useEffect } from "react";
import "./SelectedView.css";

import axios from "axios";

import api from "../../../utils/api";
import { Selection } from "../../containers";
import {
  SongSelection,
  ArtistSelection,
  AlbumSelection,
} from "../../components";

function SelectedView({ props, match }) {
  const [song, setSong] = useState(null);
  const [songMeta, setSongMeta] = useState(null);
  const [artist, setArtist] = useState(null);
  const [album, setAlbum] = useState(null);
  const [tracksForAlbum, setTracksForAlbum] = useState([]);

  useEffect(() => {
    if (match.params.contentType === "songs") {
      getSong();
      getSongMeta();
    } else if (match.params.contentType === "artists") {
      getArtist();
    } else if (match.params.contentType === "albums") {
      getAlbum();
      getTracksForAlbum();
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
      })
      .catch(function (error) {
        console.error(error);
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
      })
      .catch(function (error) {
        console.error(error);
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
      })
      .catch(function (error) {
        console.error(error);
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
        // setAlbum(response.data);
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function renderSelection() {
    if (match.params.contentType === "songs") {
      return <SongSelection song={song} songMeta={songMeta} />;
    } else if (match.params.contentType === "artists") {
      return <ArtistSelection artist={artist} />;
    } else if (match.params.contentType === "albums") {
      return <AlbumSelection album={album} tracksForAlbum={tracksForAlbum} />;
    }
  }

  return <Selection>{renderSelection()}</Selection>;
}

export default SelectedView;
