import React, { useState, useEffect } from "react";
import "./PlaylistCard.css";

import moment from "moment";
import { Link, Redirect } from "react-router-dom";
import axios from "axios";

import { SkeletonPulse, Button } from "../../components";
import { useToast } from "../../../hooks";
import Axios from "axios";
import { api } from "../../../utils/api";

function PlaylistCard(props) {
  const [willRedirectSpotify, redirectSpotify] = useState(false);

  const [showSuccess, showError, renderToast] = useToast();

  useEffect(() => {
    if (willRedirectSpotify) {
      redirectSpotify(false);
    }
  }, [willRedirectSpotify]);

  function checkAddToSpotify() {
    let spotifyAuth = JSON.parse(localStorage.getItem("spotifyAuth"));
    // If we don't have a stored authentication code
    if (spotifyAuth === null) {
      window.location.href = buildSpotifyRedirectString();
    } else {
      // Get time difference
      let previousTime = new Date(
        parseInt(localStorage.getItem("lastSpotify"))
      );
      let currentTime = new Date().getTime();

      // If our access token is out of the time range, we need to get a new one
      if (getMinuteDifference(currentTime - previousTime) >= 59) {
        window.location.href = buildSpotifyRedirectString();
      } else {
        props.addToSpotify(props.playlist.id);
      }
    }
  }

  function getMinuteDifference(diffMs) {
    console.log(diffMs);
    return Math.round(((diffMs % 86400000) % 3600000) / 60000);
  }

  function buildSpotifyRedirectString() {
    let redirect = "https://accounts.spotify.com/authorize";
    redirect += "?client_id=bbcd6fe242784619a04a475fd0454c6f";
    redirect += "&response_type=token";
    redirect +=
      "&redirect_uri=http://127.0.0.1:8080/unnamed/app/user/playlists";
    redirect += "&state=" + props.playlist.id;
    redirect += "&scope=playlist-modify-public";

    return redirect;
  }

  return (
    <div className="playlist-card">
      <div className="playlist-card-info-wrapper">
        <div className="playlist-card-image">
          {!props.skeletonPulse ||
          (props.playlist !== undefined && props.playlist.image === "") ? (
            <img src={props.playlist.image} alt="playlist-icon" />
          ) : (
            <SkeletonPulse
              style={{ width: "100%", height: "100%" }}
            ></SkeletonPulse>
          )}
        </div>
        <div style={{ width: "36px" }} />
        <div className="playlist-card-info">
          {!props.skeletonPulse ? (
            <h2>{props.playlist.name}</h2>
          ) : (
            <SkeletonPulse style={{ width: "80%", height: "36px" }} />
          )}
          {!props.skeletonPulse ? (
            <p>
              {moment(props.playlist.creation_date).format(
                "MMMM Do YYYY, h:mm:ss a"
              )}
            </p>
          ) : (
            <SkeletonPulse
              style={{ width: "60%", height: "24px", marginTop: "12px" }}
            />
          )}
          {!props.skeletonPulse ? (
            <Button isGreen={true} onClick={() => checkAddToSpotify()}>
              Export to Spotify
            </Button>
          ) : null}
        </div>
      </div>
    </div>
  );
}

export default PlaylistCard;
