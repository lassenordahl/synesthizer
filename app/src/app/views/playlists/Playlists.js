import React from "react";
import './Playlists.css';

import { Button } from "./../../components";

function Playlists() {
  return (
    <div className="playlists">
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
