// let baseUrl = "http://128.195.53.189:4001/api";
let baseUrl = "http://localhost:8080/unnamed/api";
// let baseUrl = "http://dev-tippers.ics.uci.edu/api";
// let baseUrl = "http://home-tippers.ics.uci.edu/api";

export default {
  albums: baseUrl + "/albums",
  album: baseUrl + "/album",
  artists: baseUrl + "/artists",
  artist: baseUrl + "/artist",
  songs: baseUrl + "/tracks",
  song: baseUrl + "/track"
}