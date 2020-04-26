import { useLocation } from "react-router-dom";

function SpotifyQueryParams() {

  // helpful function from https://stackoverflow.com/questions/17483057/convert-url-to-json
  function getUrlVars(url) {
    var hash;
    var myJson = {
      fromSpotifyRedirect: true
    };
    var hashes = url.slice(url.indexOf('?') + 1).split('&');
    for (var i = 0; i < hashes.length; i++) {
      hash = hashes[i].split('=');
      myJson[hash[0]] = hash[1];
      // If you want to get in native datatypes
      // myJson[hash[0]] = JSON.parse(hash[1]); 
    }
    return myJson;
  }

  let hash = useLocation().hash;
  hash = "?" + hash.slice(1);

  if (hash.length > 1) {
    return getUrlVars(hash);
  } else {
    return {
      fromSpotifyRedirect: false
    }
  }
}

export default SpotifyQueryParams;