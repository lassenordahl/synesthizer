import React, { useEffect } from "react";
import { useLocation } from "react-router-dom";

import axios from "axios";
import { api } from "./../../utils/api";

function useSpotify() {
  function getMinuteDifference(diffMs) {
    return Math.round(diffMs / 60000);
  }

  function isValidAuth() {
    let previousTime = new Date(parseInt(localStorage.getItem("lastSpotify")));
    let currentTime = new Date().getTime();

    // If our access token is out of the time range, we need to get a new one
    if (getMinuteDifference(currentTime - previousTime) >= 59) {
      return false;
    } else {
      return true;
    }
  }

  async function getSpotifyUserId() {
    let spotifyAuth = JSON.parse(localStorage.getItem("spotifyAuth"));
    return await axios.get(api.spotifyUser, {
      headers: { Authorization: `Bearer ${spotifyAuth.access_token}` },
    });
  }

  return {
    spotifyAuth: JSON.parse(localStorage.getItem("spotifyAuth")),
    authTime: new Date(parseInt(localStorage.getItem("lastSpotify"))),
    isValidAuth: isValidAuth(),
    getSpotifyUserId: getSpotifyUserId
  };
}

export default useSpotify;
