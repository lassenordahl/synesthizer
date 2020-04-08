import React from "react";
import "./ArtistCard.css";
import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";

function ArtistCard(props) {
  return (
    <div
      className="artist-card"
      style={props.style}
      onClick={() => props.onClick()}
    >
      <div className="artist-card-margin">
        <div className="artist-card-album-art">
          {props.skeletonPulse === undefined ? (
            <img alt="album art" src={props.artist.image}></img>
          ) : (
            <SkeletonPulse></SkeletonPulse>
          )}
        </div>
        <div className="artist-card-info">
          {props.skeletonPulse === undefined ? (
            <h2>{props.artist.name}</h2>
          ) : (
            <SkeletonPulse style={{ width: "128px", height: "24px" }} />
          )}
        </div>
      </div>
    </div>
  );
}

export default ArtistCard;
