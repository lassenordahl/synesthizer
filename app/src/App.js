import React, { useState } from "react";
import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";
import "./App.css";
import "./helper.css";

import {
  Landing,
  Footer,
  Sidebar,
  ContentView,
  SelectedView,
} from "./app/views";

function App() {
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
                <Route path="/app/:contentType">
                  {({ match }) => {
                    return <h2>{match.params.contentType}</h2>;
                  }}
                </Route>
              </div>
              <div className="app-header"></div>
              <Route
                exact
                path="/app/:contentType"
                component={ContentView}
              ></Route>
              <Route
                path="/app/:contentType/:itemId"
                component={SelectedView}
              ></Route>
              <Footer />
            </Route>
          </Switch>
        </div>
      </BrowserRouter>
    </div>
  );
}

export default App;
