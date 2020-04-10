import React from "react";
import "./AlbumCard.css";
import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";

import { Link } from "react-router-dom";
import { isOverCardLimit, truncateTitle } from "../../../global/helper";

function AlbumCard(props) {
  return (
    <div
      className="album-card"
      style={props.style}
      onClick={() => props.onClick()}
    >
      <div className="album-card-margin">
        <div className="album-card-album-art">
          {props.skeletonPulse === undefined ? (
            <img alt="album art" src={props.album.image}></img>
          ) : (
            <SkeletonPulse></SkeletonPulse>
          )}
        </div>
        <div className="album-card-info">
          {props.skeletonPulse === undefined ? (
            <h2>{truncateTitle(props.album.name)}</h2>
          ) : (
            <SkeletonPulse style={{ width: "128px", height: "24px", marginBottom: "8px" }} />
          )}
          <div className={isOverCardLimit(props.album.name) ? "shift-card-content": "no-shift-content"}>
            {props.skeletonPulse === undefined ? (
              <Link to={"/app/artists/" + props.album.artist_id} >
                <p>
                  {props.album.artist_name}
                </p>
              </Link>
            ) : (
              <SkeletonPulse style={{ width: "200px", height: "20px", marginBottom: "8px" }} />
            )}
            {props.skeletonPulse === undefined ? (
              <p className="subtitle">
                Release Date: {props.album.release_date}
              </p>
            ) : (
              <SkeletonPulse style={{ width: "81px", height: "12px", marginBottom: "8px" }} />
            )}
          </div>
          
        </div>
      </div>
    </div>
  );
}

export default AlbumCard;
