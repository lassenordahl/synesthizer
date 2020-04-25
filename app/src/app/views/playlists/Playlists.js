import React, { useState, useEffect } from "react";
import "./Playlists.css";

import { Button } from "./../../components";
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
      })
      .catch(function (error) {
        showError("Unable to retrieve playlists");
      });
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
      </div>
    </div>
  );
}

export default Playlists;
