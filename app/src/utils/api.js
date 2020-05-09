import axios from "axios";
import {
  history
} from "../index"

let baseUrl = process.env.REACT_APP_API_URL || "https://127.0.0.1:8080/unnamed/api";
// let baseUrl = "http://127.0.0.1:8080/unnamed/api"

axios.interceptors.response.use(undefined, error => {
  if (error.response !== undefined) {
    if (error.response.status === 401) {
      history.push("/app/user/account/login")
    }
  }
});

export const getRoute = async (path, params) => {
  const result = await axios({
    method: "get",
    url: path,
    responseType: "application/json",
    params: params,
  });

  return result;
};

export const postRoute = async (path, data) => {
  const result = await axios({
    method: "post",
    url: path,
    responseType: "application/json",
    data: data
  });
  return result;
};

export const api = {
  login: baseUrl + "/login",
  logout: baseUrl + "/logout",
  user: baseUrl + "/user",
  albums: baseUrl + "/albums",
  album: baseUrl + "/album",
  tracksForAlbum: baseUrl + "/albums/tracks",
  artists: baseUrl + "/artists",
  artist: baseUrl + "/artist",
  songs: baseUrl + "/tracks",
  song: baseUrl + "/track",
  songMeta: baseUrl + "/track/meta",
  genres: baseUrl + "/genres",
  databaseMeta: baseUrl + "/database/meta",
  playlists: baseUrl + "/playlists",
  playlist: baseUrl + "/playlist",
  playlistSnapshot: baseUrl + "/playlist/snapshot",
  playlistSession: baseUrl + "/playlist/session",
  playlistSessionTrack: baseUrl + "/playlist/session/track",
  playlistSessionAlbum: baseUrl + "/playlist/session/album",
  spotifyUser: "https://api.spotify.com/v1/me",
  spotifyPlaylist: "https://api.spotify.com/v1/users/{}/playlists",
  spotifyPlaylistTracks: "https://api.spotify.com/v1/playlists/{}/tracks",
  spotifySearch: "https://api.spotify.com/v1/search",
}