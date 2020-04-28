import React, { useState, useEffect, useContext } from "react";
import "./App.css";
import "./helper.css";

import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";

import {
  Landing,
  Footer,
  Sidebar,
  ContentView,
  SelectedView,
  Playlists,
  CreatePlaylist,
  UserView,
  SpotifyPlaylist,
} from "./app/views";
import { PrivateRoute, GoBackButton } from "./app/components";
import { useRouter } from "./hooks";

function App() {
  // Visual Variables
  const [showSidebar, setShowSidebar] = useState(true);
  // let router = useRouter();
  // let history = useHistory();

  return (
    <div className="App">
      <Route path="/app">
        <Sidebar showSidebar={showSidebar}></Sidebar>
      </Route>
      <div className="app-content">
        <Switch>
          <Route exact path="/landing" component={Landing}></Route>
          <Route path="/app">
            <div
              className="app-content-sidebar-button"
              onClick={() => setShowSidebar(!showSidebar)}
            >
              <span style={{ marginBottom: "2px" }}>
                {showSidebar ? "<" : ">"}
              </span>
            </div>
            <div className="app-content-sidebar-route">
              <PrivateRoute
                exact
                path={[
                  "/app/explore/:route",
                  "/app/explore/:route/:secondaryRoute",
                  "/app/user/:route",
                  "/app/user/:route/:secondaryRoute",
                ]}
              >
                {({ match }) => {
                  console.log(match.params);
                  return match !== null ? (
                    <div className="route-title">
                      <h2>{match.params.route}</h2>
                      {/* {match.params.secondaryRoute !== undefined ? ">" : null} */}
                      {match.params.secondaryRoute !== undefined ? (
                        <div style={{marginTop: "18px"}}>
                          <p>{match.params.secondaryRoute}</p>
                          <GoBackButton className="go-back-button" prevRoute={match.params.route}/>
                        </div>
                      ) : null}
                    </div>
                  ) : null;
                }}
              </PrivateRoute>
            </div>
            <div className="app-header"></div>
            <PrivateRoute
              exact
              path="/app/explore/:contentType"
              component={({ props, match }) => <ContentView match={match} />}
            ></PrivateRoute>
            <PrivateRoute
              exact
              path="/app/explore/:contentType/:itemId"
              component={SelectedView}
            ></PrivateRoute>
            <PrivateRoute
              exact
              path="/app/user/account/update"
              component={React.Fragment}
            ></PrivateRoute>
            <Route
              exact
              path="/app/user/account/:mode"
              component={({ props, match }) => <UserView match={match} />}
            ></Route>
            <PrivateRoute
              exact
              path="/app/user/playlists"
              component={Playlists}
            ></PrivateRoute>
            <PrivateRoute
              exact
              path="/app/user/playlists/:playlistId/spotify"
              component={SpotifyPlaylist}
            ></PrivateRoute>
            <PrivateRoute
              exact
              path="/app/user/playlists/create"
              component={CreatePlaylist}
            ></PrivateRoute>
            <Footer />
          </Route>
          <Redirect exact from="/" to="/landing" />
        </Switch>
      </div>
    </div>
  );
}

export default App;
