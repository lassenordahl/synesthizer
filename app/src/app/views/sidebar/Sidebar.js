import React from "react";
import './Sidebar.css';

import { Link } from "react-router-dom"

function Sidebar(props) {
  return (
    <div className={"sidebar" + (props.showSidebar ? " sidebar-expanded" : "")}>
      { props.showSidebar ? 
        <React.Fragment>
          <Link to="/app/explore/songs">
            Songs
          </Link>
          <Link to="/app/explore/albums">
            Albums
          </Link>
          <Link to="/app/explore/artists">
            Artists
          </Link>
          <Link to="/app/user/playlists">
            Playlists
          </Link>
          <Link to="/app/user/playlists/create">
            Create Playlist
          </Link>
          <Link to="/app/user/login">
            Login
          </Link>
        </React.Fragment>
        : null 
      }
    </div>
  );
}

export default Sidebar;
