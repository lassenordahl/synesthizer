import React from "react";
import './SongView.css';

function SongView({props, match}) {
  return (
    <div className="song-view">
      template [{match.params.songId}]
    </div>
  );
}

export default SongView;
