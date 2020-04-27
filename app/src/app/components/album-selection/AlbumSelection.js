import React, { useState, useEffect } from "react";
import "./AlbumSelection.css";

import { Link } from "react-router-dom";
import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";
import { convertToSeconds } from "../../../global/helper";

import { SessionButton } from "../../components";

function AlbumSelection(props) {
  return (
    <React.Fragment>
      <div className="fade-in selected-view-main-info">
        {props.album !== undefined ? (
          <SessionButton
            style={{top: "24px"}}
            isSelected={props.isInSession}
            onClick={() => {
              if (props.isInSession) {
                props.removeFromSession(props.album.id, "album");
              } else {
                props.addToSession(props.album.id, "album");
              }
            }}
          />
        ) : null}
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
            <Link to={"/app/explore/artists/" + props.album.artist_id}>
              <p>{props.album.artist_name}</p>
            </Link>
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
          <h3>Songs</h3>
          {props.tracksForAlbum.map(function (track, index) {
            return (
              <Link to={"/app/explore/songs/" + track.id} key={index}>
                <div className="selected-view-song-row" key={index}>
                  <p>{track.name}</p>
                  <p>{convertToSeconds(track.duration_ms)}</p>
                </div>
              </Link>
            );
          })}
        </div>
      ) : null}
    </React.Fragment>
  );
}

export default AlbumSelection;
