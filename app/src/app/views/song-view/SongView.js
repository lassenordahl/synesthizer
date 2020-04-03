import React from "react";
import './SongView.css';

import { Card } from '../../containers';

function SongView({props, match}) {
  return (
    <div className="song-view">
      <Card className="song-view-card">
        Template card for {match.params.songId}
      </Card>
    </div>
  );
}

export default SongView;
