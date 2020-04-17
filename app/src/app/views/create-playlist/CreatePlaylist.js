import React, { useState, useEffect } from "react";
import './CreatePlaylist.css';

import { Card } from "./../../containers";

function CreatePlaylist() {

  const [playlistSession, setPlaylistSession] = useState([]);

  return (
    <div className="create-playlist">
      <Card className="create-playlist-card">
       <div className="create-playlist-header">
        <div className="create-playlist-header-picture">
          <img alt="playlist-profile"/>
        </div>
        <div className="create-playlist-header-info">
          <input></input>
        </div>
       </div>
       <div className="create-playlist-songs">
        <div className="create-playlist-song-labels format-column-grid">
          <div>
            Song
          </div>
          <div>
            Artist
          </div>
          <div>
            Album
          </div>
          <div>
            Length
          </div>
          <div>

          </div>
        </div>
        { playlistSession.length > 0
          ? <div>
              {playlistSession.map(function(song, index) {
                return (<div key={index}>
                  hehe
                </div>)
              })}
            </div>
          : <div className="create-playlist-no-songs">
              <p>
                No songs added to playlist
              </p>
            </div>
        }
       </div>
      </Card>
    </div>
  );
}

export default CreatePlaylist;
