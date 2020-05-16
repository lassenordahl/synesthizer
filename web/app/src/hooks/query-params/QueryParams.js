import { useLocation } from "react-router-dom";

function QueryParams() {

  let queryParams = {};

  let query = new URLSearchParams(useLocation().search);

  if (query.get("song"))
    queryParams.song = query.get("song");
  if (query.get("album"))
    queryParams.album = query.get("album");
  if (query.get("artist"))
    queryParams.artist = query.get("artist");
  if (query.get("popularity"))
    queryParams.popularity = query.get("popularity");

  return queryParams;
}

export default QueryParams;