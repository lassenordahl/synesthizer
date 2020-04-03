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
          <Route exact path="/landing" component={Landing}></Route>
          <Route path="/">
            <Route exact path="/songs" component={SongsView}></Route>
            <Route path="/songs/:songId" component={SongView}></Route>
            <Footer>

            </Footer>
          </Route>
          
        </Switch>
        <Redirect from="/" to ="/landing"></Redirect>
      </BrowserRouter>
    </div>
  );
}
 
export default App;
