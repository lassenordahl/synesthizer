import React, { useState, useEffect } from "react";
import "./AlbumSelection.css";

import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";

function AlbumSelection(props) {
  return (
    <React.Fragment>
      <div className="fade-in selected-view-main-info">
        <div className="selected-view-album-art">
          {props.album !== null ? (
            <img alt="album art" src={props.album.image} />
          ) : (
            <SkeletonPulse></SkeletonPulse>
          )}
        </div>
        <div className="selected-view-details">
          {props.album !== null ? (
            <h2>{props.album.name}</h2>
          ) : (
            <SkeletonPulse style={{ width: "160px", height: "24px", marginBottom: "8px" }} />
          )}
          {props.album !== null ? (
            <p>{props.album.artist_name}</p>
          ) : (
            <SkeletonPulse style={{ width: "256px", height: "24px", marginBottom: "8px" }} />
          )}
          {props.album !== null ? (
            <p className="subtitle">Release Date: {props.album.release_date}</p>
          ) : (
            <SkeletonPulse style={{ width: "128px", height: "24px", marginBottom: "8px" }} />
          )}
        </div>
      </div>
      {props.tracksForAlbum.length > 0 ? (
        <div className="selected-view-song-list">
          {props.tracksForAlbum.map(function (track, index) {
            return (
              <div className="selected-view-song-row" key={index}>
                <p>{track.name}</p>
                <p>{Math.floor(track.duration_ms / 60000)}:{Math.floor(track.duration_ms % 60000 / 1000)}</p>
              </div>
            );
          })}
        </div>
      ) : null}
    </React.Fragment>
  );
}

export default AlbumSelection;
