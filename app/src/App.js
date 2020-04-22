import React, { useState} from "react";
import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";
import "./App.css";
import "./helper.css";

import {
  Landing,
  Footer,
  Sidebar,
  ContentView,
  SelectedView,
  Playlists,
  CreatePlaylist,
  LoginView
} from "./app/views";

function App() {
  // Visual Variables
  const [showSidebar, setShowSidebar] = useState(true);

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
                path="/app/user/login"
                component={LoginView}
              />
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
