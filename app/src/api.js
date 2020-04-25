// let baseUrl = process.env.REACT_APP_API_URL || "http://localhost:8080/unnamed/api";
let baseUrl = "http://127.0.0.1:8080/unnamed/api"

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
  playlistSessionTrack: baseUrl + "/playlist/session/track",
  playlist: baseUrl + "/playlist",
}