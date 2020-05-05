import React, { useEffect, useContext } from "react";
import { useCookies } from "react-cookie";
import axios from "axios";
import "./Sidebar.css";

import { Link, Route } from "react-router-dom";

import { LoggedInContext } from "../../context/LoggedInContext";
import { api } from "../../../utils/api";
import { Button } from "../../components";
import { useToast } from "../../../hooks";
import logo from "../../assets/cs122b-logo.png";

function Sidebar(props) {
  const [showSuccess, showError, renderToaster] = useToast();
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
          showSuccess("Successfully logged out");
        }
      })
      .catch(function (error) {
        console.error(error);
        showError("Error logging out");
      });
  }

  return (
    <div className={"sidebar" + (props.showSidebar ? " sidebar-expanded" : "")}>
      {renderToaster()}
      {props.showSidebar ? (
        <React.Fragment>
          <div className="logo-headline fade-in">
            <img src={logo} alt="synestheiser" />
            <h2>Synesthizer</h2>
            <p>Intelligent Playlist Creation</p>
          </div>
          <div style={{height: "160px"}}/>
          <Link to="/app/explore/songs">
            <Route path="/app/explore/songs">
              <div className="link-active" />
            </Route>
            Songs
          </Link>
          <Link to="/app/explore/albums">
            <Route path="/app/explore/albums">
              <div className="link-active" />
            </Route>
            Albums
          </Link>
          <Link to="/app/explore/artists">
            <Route path="/app/explore/artists">
              <div className="link-active" />
            </Route>
            Artists
          </Link>
          {loggedIn ? (
            <React.Fragment>
              <Link to="/app/user/playlists">
                <Route exact path="/app/user/playlists">
                  <div className="link-active" />
                </Route>
                Playlists
              </Link>
              <Link to="/app/user/playlists/create">
                <Route path="/app/user/playlists/create">
                  <div className="link-active" />
                </Route>
                Create Playlist
              </Link>
              <Link to="/app/user/account/update">
                <Route path="/app/user/account/update">
                  <div className="link-active" />
                </Route>
                Account
              </Link>
              <Button onClick={logout}>Log Out</Button>
            </React.Fragment>
          ) : (
            <React.Fragment>
              <Link to="/app/user/account/login">
                <Route path="/app/user/account/login">
                  <div className="link-active" />
                </Route>
                Login
              </Link>
              <Link to="/app/user/account/create">
                <Route path="/app/user/account/create">
                  <div className="link-active" />
                </Route>
                Sign Up
              </Link>
            </React.Fragment>
          )}
        </React.Fragment>
      ) : null}
    </div>
  );
}

export default Sidebar;
