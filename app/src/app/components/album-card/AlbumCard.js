import React from "react";
import "./AlbumCard.css";
import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";

import moment from "moment";
import { Link } from "react-router-dom";

import { SessionButton } from "../../components";
import { isOverCardLimit, truncateTitle } from "../../../global/helper";

function AlbumCard(props) {
  return (
    <div
      className="album-card"
      style={props.style}
      onClick={() => props.onClick()}
    >
      <div className="album-card-margin">
        {props.skeletonPulse === undefined ? (
          <SessionButton
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
        <div className="album-card-album-art">
          {props.skeletonPulse === undefined ? (
            <img alt="album art" src={props.album.image}></img>
          ) : (
            <SkeletonPulse></SkeletonPulse>
          )}
        </div>
        <div className="album-card-info">
          {props.skeletonPulse === undefined ? (
            <h2>{truncateTitle(props.album.name, 22)}</h2>
          ) : (
            <SkeletonPulse
              style={{ width: "128px", height: "24px", marginBottom: "8px" }}
            />
          )}
          <div
            className={
              "no-shift-content"
              // isOverCardLimit(props.album.name)
              //   ? "shift-card-content"
              //   : "no-shift-content"
            }
          >
            {props.skeletonPulse === undefined ? (
              <Link to={"/app/explore/artists/" + props.album.artist_id}>
                <p>{props.album.artist_name}</p>
              </Link>
            ) : (
              <SkeletonPulse
                style={{ width: "200px", height: "20px", marginBottom: "8px" }}
              />
            )}

            {props.skeletonPulse === undefined ? (
              <div
                style={{
                  width: "100%",
                  display: "flex",
                  justifyContent: "space-between",
                }}
              >
                <p className="subtitle">
                  Release Date:{" "}
                  {moment(props.album.release_date).format(
                    "MMM Do YYYY, hh:mm a"
                  )}
                </p>
                <p className="subtitle" style={{ marginLeft: "20px" }}>
                  Popularity: {props.album.popularity}
                </p>
              </div>
            ) : (
              <SkeletonPulse
                style={{ width: "81px", height: "12px", marginBottom: "8px" }}
              />
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default AlbumCard;
