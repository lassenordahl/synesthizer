import React, { useState, useEffect } from "react";
import axios from "axios";

import "./SelectedView.css";

import { Redirect } from "react-router-dom";

import api from "../../../api";
import { Selection } from "../../containers";
import {
  SongSelection,
  ArtistSelection,
  AlbumSelection,
} from "../../components";

function SelectedView({ props, match }) {
  const [song, setSong] = useState(null);
  const [artist, setArtist] = useState(null);
  const [album, setAlbum] = useState(null);

  useEffect(() => {
    if (match.params.contentType === "song") {
      getSong();
    } else if (match.params.contentType === "artist") {
      getArtist();
    } else if (match.params.contentType === "album") {
      getAlbum();
    }
  }, []);

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
        setTimeout(() => setAlbum(response.data), 2000);
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function renderSelection() {
    if (match.params.contentType === "song") {
      return <SongSelection song={song} />;
    } else if (match.params.contentType === "artist") {
      return <ArtistSelection artist={artist} />;
    } else if (match.params.contentType === "album") {
      return <AlbumSelection album={album} />;
    }
  }

  return (
    <div>
      <Selection>{renderSelection()}</Selection>
    </div>
  );
}

export default SelectedView;
