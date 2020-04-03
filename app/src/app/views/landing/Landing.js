import React, { useState, useEffect } from "react";
import './Landing.css';

import {
  Redirect
} from "react-router-dom";

import { Card } from './../../containers';
import { Button } from './../../components';

function Landing() {

  const [ willRedirectSongs, redirectSongs ] = useState(false);

  useEffect(() => {
    if (willRedirectSongs) {
      redirectSongs(false)
    }
  }, [willRedirectSongs]);

  function openSongView() {
    redirectSongs(true);
  }

  return (
    <div className="landing flex-center">
      { willRedirectSongs ? <Redirect to="/songs"></Redirect> : null}
      <div className="landing-content flex-center">
        <Card>
          <h1>CS122B</h1>
          <p>
            What are we doing
          </p>
          <Button isPrimary={true} onClick={() => openSongView()}>
            Join us
          </Button>
        </Card>
      </div>
    </div>
  );
}

export default Landing;
