import React from "react";
import "./SongCard.css";

import axios from "axios";
import { Link } from "react-router-dom";
import moment from "moment";

import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";
import { isOverCardLimit, truncateTitle } from "../../../global/helper";
import { SessionButton } from "../../components";
import api from "../../../utils/api";

function SongCard(props) {
  return (
    <div
      className="song-card"
      style={props.style}
      onClick={() => props.onClick()}
    >
      <div className="song-card-margin">
        {props.skeletonPulse === undefined ? (
          <SessionButton
            isSelected={props.isInSession}
            onClick={() => {
              if (props.isInSession) {
                props.removeFromSession(props.song.id, "track");
              } else {
                props.addToSession(props.song.id, "track");
              }
            }}
          />
        ) : null}
        <div className="song-card-album-art">
          {props.skeletonPulse === undefined ? (
            <img alt="album art" src={props.song.album.image}></img>
          ) : (
            <SkeletonPulse></SkeletonPulse>
          )}
        </div>
        <div className="song-card-info">
          {props.skeletonPulse === undefined ? (
            <h2>{truncateTitle(props.song.name, 22)}</h2>
          ) : (
            <SkeletonPulse
              style={{ width: "128px", height: "24px", marginBottom: "8px" }}
            />
          )}
          <div
            className={
              "no-shift-content"
              // isOverCardLimit(props.song.name)
              //   ? "shift-card-content"
              //   : "no-shift-content"
            }
          >
            {props.skeletonPulse === undefined ? (
              <div style={{ display: "flex", width: "100%" }}>
                {props.song.artists.map(function (artist, index) {
                  return (
                    <Link to={"/app/explore/artists/" + artist.id}>
                      <p>
                        {index < props.song.artists.length - 1
                          ? artist.name + ",  "
                          : artist.name}
                      </p>
                    </Link>
                  );
                })}
              </div>
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
                  {props.song.album.release_date}
                </p>
                <p className="subtitle" style={{ position: "absolute", right: "24px" }}>
                  Popularity: {props.song.popularity}
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

export default SongCard;
