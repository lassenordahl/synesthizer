import React, { useEffect, useContext } from "react";
import { useCookies } from "react-cookie";
import axios from "axios";
import "./Sidebar.css";

import { api } from "../../../utils/api";

import { Link } from "react-router-dom";
import { LoggedInContext } from "../../context/LoggedInContext";

import { Button } from "../../components";

function Sidebar(props) {
  const [loggedIn, setLoggedIn] = useContext(LoggedInContext);
  const [cookies, setCookie] = useCookies([]);

  function logout(form) {
    axios
      .post(api.logout, {})
      .then(function (response) {
        console.log(response);
        if (response.status === 200) {
          setLoggedIn(false);
          setCookie("logged_in", false, { path: "/unnamed", expires: 0 });
        }
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  return (
    <div className={"sidebar" + (props.showSidebar ? " sidebar-expanded" : "")}>
      {props.showSidebar ? (
        <React.Fragment>
          <Link to="/app/explore/songs">Songs</Link>
          <Link to="/app/explore/albums">Albums</Link>
          <Link to="/app/explore/artists">Artists</Link>
          {loggedIn ? (
            <React.Fragment>
              <Link to="/app/user/playlists">Playlists</Link>
              <Link to="/app/user/playlists/create">Create Playlist</Link>
              <Link to="/app/user/account/update">Account</Link>
              <Button onClick={logout}>Log Out</Button>
            </React.Fragment>
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
