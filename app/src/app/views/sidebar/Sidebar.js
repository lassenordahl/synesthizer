import React from "react";
import "./Sidebar.css";

import { Link } from "react-router-dom";
import { isLoggedIn } from "../../../utils/session";

function Sidebar(props) {
  return (
    <div className={"sidebar" + (props.showSidebar ? " sidebar-expanded" : "")}>
      {props.showSidebar ? (
        <React.Fragment>
          <Link to="/app/explore/songs">Songs</Link>
          <Link to="/app/explore/albums">Albums</Link>
          <Link to="/app/explore/artists">Artists</Link>
          <Link to="/app/user/playlists">Playlists</Link>
          <Link to="/app/user/playlists/create">Create Playlist</Link>
          {isLoggedIn() ? (
            <Link to="/app/user/account/update">Account</Link>
          ) : (
            <React.Fragment>
              <Link to="/app/user/account/login">Login</Link>
              <Link to="/app/user/account/create">Sign Up</Link>
            </React.Fragment>
          )}
        </React.Fragment>
      ) : null}
    </div>
  );
}

export default Sidebar;
