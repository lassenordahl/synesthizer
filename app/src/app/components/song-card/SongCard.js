import React from "react";
import "./SongCard.css";
import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";

function SongCard(props) {
  return (
    <div
      className="song-card"
      style={props.style}
      onClick={() => props.onClick()}
    >
      <div className="song-card-margin">
        <div className="song-card-album-art">
          {props.skeletonPulse === undefined ? (
            <img alt="album art" src={props.song.album.image}></img>
          ) : (
            <SkeletonPulse></SkeletonPulse>
          )}
        </div>
        <div className="song-card-info">
          <h2>
            {props.skeletonPulse === undefined ? (
              props.song.name
            ) : (
              <SkeletonPulse style={{ width: "128px", height: "24px" }} />
            )}
          </h2>
          <p>
            {props.skeletonPulse === undefined ? (
              props.song.artists.map(function (artist, index) {
                return index < props.song.artists.length - 1
                  ? artist.name + ", "
                  : artist.name;
              })
            ) : (
              <SkeletonPulse style={{ width: "200px", height: "20px" }} />
            )}
          </p>
          <p className="subtitle">
            {props.skeletonPulse === undefined ? (
              "Playlist Count: 18"
            ) : (
              <SkeletonPulse style={{ width: "81px", height: "12px" }} />
            )}
          </p>
        </div>
      </div>
    </div>
  );
}

export default SongCard;
