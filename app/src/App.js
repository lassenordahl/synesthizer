import React from 'react';
import {
    BrowserRouter,
    Route,
    Switch,
    Redirect
} from 'react-router-dom';
import './App.css';
import './helper.css';

import { Landing, SongsView, SongView, Footer } from './app/views';
 
function App() {

  return (
    <div className="App">
      <BrowserRouter basename={'/unnamed'}>
        
            <Switch>
              <Route path="/app">
                <div className="app-header">
                  <h1>
                  <Route exact path="/app/songs">
                    Song Search
                  </Route>
                  <Route exact path="/app/songs/:songId">
                    Selected Song
                  </Route>
                  </h1>
                </div>
              </Route> 
            </Switch>
       
        <Switch>
          <Route exact path="/landing" component={Landing}></Route>
          <Route path="/app">
            <Route exact path="/app/songs" component={SongsView}></Route>
            <Route path="/app/songs/:songId" component={SongView}></Route>
            <Footer/>
          </Route> 
        </Switch>
      </BrowserRouter>
    </div>
  );
}
 
export default App;
