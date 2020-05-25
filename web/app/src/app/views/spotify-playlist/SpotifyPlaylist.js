
import React, { useEffect } from "react";
import './SpotifyPlaylist.css';

import { Card } from "../../containers";

function SpotifyPlaylist() {

  useEffect(() => {

  }, []);

  return (
    <div className="spotify-playlist">
      <Card>
        Spotify playlist for this item
      </Card>
    </div>
  );
}

export default SpotifyPlaylist;
