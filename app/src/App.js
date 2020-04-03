import React, { Component } from 'react';
import {
    BrowserRouter,
    Link,
    Route,
    Switch
} from 'react-router-dom';
 
class App extends Component {
  render() {
    return (
      <div className="App">
        <div className="App-header">
          <h2>Welcome to React</h2>
        </div>
        {/* <BrowserRouter basename={process.env.REACT_APP_ROUTER_BASE || ''}> */}
        <BrowserRouter basename={'unnamed'}>
          <div>
            hello world
          </div>
        </BrowserRouter>
      </div>
    );
  }
}
 
export default App;
