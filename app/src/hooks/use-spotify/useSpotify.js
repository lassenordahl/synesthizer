import { useLocation } from "react-router-dom";

function useSpotify() {

  function getMinuteDifference(diffMs) {
    return Math.round(diffMs / 60000);
  }

  return {
    spotifyAuth: JSON.parse(localStorage.getItem("spotifyAuth"));
  };
}

export default useSpotify;