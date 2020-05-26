import React, { useState } from "react";
import "./App.css";
import "./helper.css";

import { Route, Switch, Redirect } from "react-router-dom";

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
  Dashboard,
} from "./app/views";
import { PrivateRoute, GoBackButton } from "./app/components";

import { LoggedInContext } from "app/context/LoggedInContext";

function App() {
  // Visual Variables
  const [showSidebar, setShowSidebar] = useState(true);
  const [isLoggedIn, setIsLoggedIn, isEmployee, setIsEmployee] = useState(
    LoggedInContext
  );
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
                  return match !== null ? (
                    <div className="route-title">
                      <h2>{match.params.route}</h2>
                      {/* {match.params.secondaryRoute !== undefined ? ">" : null} */}
                      {match.params.secondaryRoute !== undefined ? (
                        <div style={{ marginTop: "18px" }}>
                          <p>{match.params.secondaryRoute}</p>
                          <GoBackButton
                            className="go-back-button"
                            prevRoute={match.params.route}
                          />
                        </div>
                      ) : null}
                    </div>
                  ) : null;
                }}
              </PrivateRoute>
              <PrivateRoute exact employeeOnly={true} path="/app/_dashboard">
                <div className="route-title">
                  <h2>Dashboard</h2>
                </div>
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
              employeeNotAllowed={true}
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
              employeeNotAllowed={true}
              path="/app/user/playlists"
              component={Playlists}
            ></PrivateRoute>
            <PrivateRoute
              exact
              employeeNotAllowed={true}
              path="/app/user/playlists/:playlistId/spotify"
              component={SpotifyPlaylist}
            ></PrivateRoute>
            <PrivateRoute
              exact
              employeeNotAllowed={true}
              path="/app/user/playlists/create"
              component={CreatePlaylist}
            ></PrivateRoute>
            <PrivateRoute
              exact
              employeeOnly={true}
              component={Dashboard}
              path="/app/_dashboard"
            />
            <Footer />
          </Route>
          <Redirect exact from="/" to="/landing" />
        </Switch>
      </div>
    </div>
  );
}

export default App;
