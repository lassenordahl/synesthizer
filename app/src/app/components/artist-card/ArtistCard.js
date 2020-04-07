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
          <h2>
            {props.skeletonPulse === undefined ? (
              props.artist.name
            ) : (
              <SkeletonPulse style={{ width: "128px", height: "24px" }} />
            )}
          </h2>
        </div>
      </div>
    </div>
  );
}

export default ArtistCard;
