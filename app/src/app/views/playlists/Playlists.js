import React, { useState, useEffect } from "react";
import "./Playlists.css";

import { Button, PlaylistCard } from "./../../components";
import { useToast } from "../../../hooks";
import { getRoute, api } from "../../../utils/api";
import { useRouteMatch } from "react-router-dom";

function Playlists() {
  const [playlists, setPlaylists] = useState([]);

  const [showSuccess, showError, renderToast] = useToast();

  useEffect(() => {
    getPlaylists();
  }, []);

  function getPlaylists() {
    console.log(playlists);
    getRoute(api.playlists)
      .then(function (response) {
        showSuccess("funky and fresh");
        console.log(response);
        setPlaylists(response.data.playlists);
      })
      .catch(function (error) {
        showError("Unable to retrieve playlists");
      });
  }

  function renderCards() {
    if (playlists.length === 0) {
      return (
        [1,2,3,4,5,6].map(function (playlist, index) {
          return <PlaylistCard skeletonPulse={true} key={index}/>;
        })
      );
    } else {
      return (
        playlists.map(function (playlist, index) {
          return <PlaylistCard playlist={playlist} key={index}/>;
        })
      );
    }
  }

  return (
    <div className="playlists">
      {renderToast()}
      <div className="playlists-content">
        <div className="content-view-search">
          <input></input>
          <div style={{ width: "48px" }}></div>
          <Button isPrimary={true}>Search</Button>
        </div>
        {renderCards()}
      </div>
    </div>
  );
}

export default Playlists;
