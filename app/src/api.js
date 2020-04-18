// let baseUrl = "http://128.195.53.189:4001/api";
// let baseUrl = process.env.REACT_APP_API_URL || "http://localhost:8080/unnamed/api";
let baseUrl = "http://localhost:8080/unnamed/api"
// let baseUrl = "http://dev-tippers.ics.uci.edu/api";
// let baseUrl = "http://home-tippers.ics.uci.edu/api";

export default {
  albums: baseUrl + "/albums",
  album: baseUrl + "/album",
  tracksForAlbum: baseUrl + "/albums/tracks",
  artists: baseUrl + "/artists",
  artist: baseUrl + "/artist",
  songs: baseUrl + "/tracks",
  song: baseUrl + "/track",
  songMeta: baseUrl + "/track/meta",
  playlistSession: baseUrl + "/playlist/session",
  playlistSessionTrack: baseUrl + "/playlist/session/track"
}