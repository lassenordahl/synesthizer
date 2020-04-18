import React, { useState, useEffect } from "react";
import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";
import "./App.css";
import "./helper.css";

import axios from "axios";

import {
  Landing,
  Footer,
  Sidebar,
  ContentView,
  SelectedView,
  Playlists,
  CreatePlaylist,
} from "./app/views";

import { ExpandableCart } from "./app/components";
import api from "api";

function App() {
  // Visual Variables
  const [showSidebar, setShowSidebar] = useState(true);

  // Session Tracks
  const [sessionTracks, setSessionTracks] = useState([]);

  useEffect(() => {
    getPlaylistSession();
  }, [])

  // Get what songs are in the playlist once in the parent so each card can know if it's selected or not
  function getPlaylistSession() {
    axios
      .get(api.playlistSession)
      .then(function (response) {
        console.log(response);
        setSessionTracks(response.data.tracks);
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function addToSession(songId) {
    axios
      .post(api.playlistSessionTrack, {
        id: songId,
      })
      .then(function (response) {
        console.log(response);
        getPlaylistSession();
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function removeFromSession(songId) {
    axios
      .delete(api.playlistSessionTrack, {
        params: { id: songId },
      })
      .then(function (response) {
        console.log(response);
        getPlaylistSession();
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function isInSession(songId) {
    for (let i = 0; i < sessionTracks.length; i++) {
      if (songId === sessionTracks[i].id) {
        return true;
      }
    }
    return false;
  }

  return (
    <div className="App">
      <BrowserRouter basename={"/unnamed"}>
        <Route path="/app">
          <Sidebar showSidebar={showSidebar}></Sidebar>
        </Route>
        <div className="app-content">
          <Switch>
            <Route exact path="/landing" component={Landing}></Route>
            <Route path="/app">
              <Route exact path="/app/explore/:route">
                <ExpandableCart />
              </Route>
              <div
                className="app-content-sidebar-button"
                onClick={() => setShowSidebar(!showSidebar)}
              >
                <span style={{ marginBottom: "2px" }}>
                  {showSidebar ? "<" : ">"}
                </span>
              </div>
              <div className="app-content-sidebar-route">
                <Route
                  exact
                  path={[
                    "/app/explore/:route",
                    "/app/user/:route",
                    "/app/user/:route/:secondaryRoute",
                  ]}
                >
                  {({ match }) => {
                    return match !== null ? (
                      <div className="route-title">
                        <h2>{match.params.route}</h2>
                        {/* {match.params.secondaryRoute !== undefined ? ">" : null} */}
                        <p>{match.params.secondaryRoute}</p>
                      </div>
                    ) : null;
                  }}
                </Route>
              </div>
              <div className="app-header"></div>
              <Route
                exact
                path="/app/explore/:contentType"
                component={({ props, match }) => (
                  <ContentView
                    addToSession={addToSession}
                    removeFromSession={removeFromSession}
                    isInSession={isInSession}
                    getPlaylistSession={getPlaylistSession}
                    match={match}
                  />
                )}
              ></Route>
              <Route
                exact
                path="/app/explore/:contentType/:itemId"
                component={SelectedView}
              ></Route>
              <Route
                exact
                path="/app/user/playlists"
                component={Playlists}
              ></Route>
              <Route
                exact
                path="/app/user/playlists/create"
                component={CreatePlaylist}
              ></Route>
              <Footer />
            </Route>
            <Redirect exact from="/" to="/landing" />
          </Switch>
        </div>
      </BrowserRouter>
    </div>
  );
}

export default App;
