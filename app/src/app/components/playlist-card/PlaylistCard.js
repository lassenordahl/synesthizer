import React from "react";
import './PlaylistCard.css';

import moment from "moment";

import { SkeletonPulse, Button } from "../../components";

function PlaylistCard(props) {
  return (
    <div className="playlist-card">
      <div className="playlist-card-info-wrapper">
        <div className="playlist-card-image">

        </div>
        <div style={{ width: "36px" }}/>
        <div className="playlist-card-info">
          { !props.skeletonPulse 
            ? <h2>
                {props.playlist.name}
              </h2>
            : <SkeletonPulse style={{ width: "80%", height: "36px" }}/>
          }
          { !props.skeletonPulse 
            ? <p>
                {moment(props.playlist.creation_date).format("MMMM Do YYYY, h:mm:ss a")}
              </p>
            : <SkeletonPulse style={{ width: "60%", height: "24px", marginTop: "12px" }}/>
          }
          {
            !props.skeletonPulse
            ? <Button isGreen={true}>
                Export to Spotify
              </Button>
            : null
          }
        </div>
      </div>
    </div>
  );
}

export default PlaylistCard;
