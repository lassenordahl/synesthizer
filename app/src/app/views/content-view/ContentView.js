import React, { useState, useEffect, useRef } from "react";
import "./ContentView.css";

import { Redirect } from "react-router-dom";
import axios from "axios";
import queryString from "query-string";

import {
  SongCard,
  AlbumCard,
  ArtistCard,
  ExpandableCart,
  Paginate,
  SortBy,
  Search,
  OptionToggle,
  Browse,
} from "../../components";
import { Card } from "../../containers";
import { useRouter, useToast } from "../../../hooks";

import { api } from "../../../utils/api.js";

import { alphaNumArray } from "../../../global/helper";

function ContentView(props) {
  // Query Parameters
  let router = useRouter();
  const [params, setParams] = useState({
    offset: 0,
    limit: 20,
    ...router.query,
  });

  // Genres for dropdown
  const [genres, setGenres] = useState([]);

  // Selection Variables
  const [selectedCardId, setSelectedCardId] = useState(null);

  // Redirect Variables
  const [willRedirectSong, redirectSong] = useState(false);
  const [willRedirectArtist, redirectArtist] = useState(false);
  const [willRedirectAlbum, redirectAlbum] = useState(false);

  // Browse Mode
  const [browseMode, setBrowseMode] = useState(
    params.browseMode !== undefined && params.browseMode !== ""
      ? params.browseMode
      : "Search Mode"
  );

  // Data Array
  const [albums, setAlbums] = useState(null);
  const [artists, setArtists] = useState(null);
  const [songs, setSongs] = useState(null);

  // Match is passed in under props now because the <Route> component needed props and I had issues passing match normally
  let match = props.match;

  // Session Tracks
  const [sessionTracks, setSessionTracks] = useState([]);
  const [sessionAlbums, setSessionAlbums] = useState([]);

  const [showSuccess, showError, renderToaster] = useToast();

  useEffect(() => {
    console.log("resetting the params");
    console.log("CONTENT TYPE CHANGED", match.params.contentType);

    setParams({
      offset: 0,
      limit: 20,
      ...router.query,
    });

    if (router.query.browseMode === undefined) {
      setBrowseMode("Search Mode");
    } else {
      setBrowseMode(router.query.browseMode);
    }
  }, [match.params.contentType]);

  useEffect(() => {
    console.log("params changed");
    getPlaylistSession();

    if (match.params.contentType === "albums") {
      setAlbums(null);
      getAlbums();
    } else if (match.params.contentType === "artists") {
      setArtists(null);
      getGenres();
      getArtists();
    } else if (match.params.contentType === "songs") {
      setSongs(null);
      getSongs();
    }

    router.push("?".concat(queryString.stringify(params)));
  }, [params]);

  useEffect(() => {
    if (browseMode === "Search Mode") {
      setParams({
        ...params,
        name: undefined,
        genre: undefined,
        browseMode: browseMode,
      });
    } else if (browseMode === "Browse Mode") {
      setParams({
        ...params,
        searchMode: undefined,
        search: undefined,
        browseMode: browseMode,
      });
    }
  }, [browseMode]);

  useEffect(() => {
    // Reset redirect variables where needed
    if (willRedirectArtist) {
      redirectArtist(false);
    }
    if (willRedirectAlbum) {
      redirectAlbum(false);
    }
    if (willRedirectSong) {
      redirectSong(false);
    }
  }, [willRedirectArtist, willRedirectAlbum, willRedirectSong]);

  useEffect(() => {
    if (selectedCardId === null) {
      return;
    }

    if (match.params.contentType === "albums") {
      redirectAlbum(true);
    } else if (match.params.contentType === "artists") {
      redirectArtist(true);
    } else if (match.params.contentType === "songs") {
      redirectSong(true);
    }
  }, [selectedCardId]);

  function getGenres() {
    axios
      .get(api.genres)
      .then(function (response) {
        console.log(response.genres);
        setGenres(response.data.genres);
      })
      .catch(function (error) {
        console.log(error);
        showError("Error retrieving genres");
      });
  }

  function getAlbums() {
    axios
      .get(api.albums, { params: params })
      .then(function (response) {
        console.log(response);
        if (response.data.albums !== null) {
          setAlbums(response.data.albums);
        } else {
          showError("Error retrieving albums");
        }
      })
      .catch(function (error) {
        console.error(error);
        showError("Error retrieving albums");
      });
  }

  function getArtists() {
    axios
      .get(api.artists, { params: params })
      .then(function (response) {
        console.log(response);
        if (response.data.artists !== null) {
          setArtists(response.data.artists);
        } else {
          showError("Error retrieving artists");
        }
      })
      .catch(function (error) {
        console.log(error);
        showError("Error retrieving artists");
      });
  }

  function getSongs() {
    console.log("songs params");
    console.log(params);
    console.log("router params");
    console.log(router.query);
    axios
      .get(api.songs, { params: params })
      .then(function (response) {
        console.log(response);
        if (response.data.songs !== null) {
          setSongs(response.data.songs);
        } else {
          showError("Error retrieving songs");
        }
      })
      .catch(function (error) {
        console.error(error);
        showError("Error retrieving songs");
      });
  }

  function getAutoItems(
    searchMode,
    search,
    setAutoCallback,
    cacheItemsCallback
  ) {
    let url = "";
    if (match.params.contentType === "albums") {
      url = api.albums;
    } else if (match.params.contentType === "artists") {
      url = api.artists;
    } else if (match.params.contentType === "songs") {
      url = api.songs;
    }

    axios
      .get(url, {
        params: {
          searchMode,
          search,
          limit: 10,
        },
      })
      .then(function (response) {
        if (response.data[match.params.contentType] !== null) {
          console.log(response.data[match.params.contentType]);
          setAutoCallback(response.data[match.params.contentType]);
          cacheItemsCallback(search, response.data[match.params.contentType]);
        } else {
          showError("Error retrieving autocompletes");
        }
      })
      .catch(function (error) {
        console.log(error);
        showError("Error retrieving autocompletes");
      });
  }

  function selectCard(id) {
    setSelectedCardId(id);
  }

  // Get what songs are in the playlist once in the parent so each card can know if it's selected or not
  function getPlaylistSession() {
    axios
      .get(api.playlistSession)
      .then(function (response) {
        console.log(response);
        setSessionTracks(response.data.tracks);
        setSessionAlbums(response.data.albums);
      })
      .catch(function (error) {
        console.error(error);
        showError("Error retrieving playlist");
      });
  }

  function addToSession(id, itemType) {
    axios
      .post(
        itemType === "track"
          ? api.playlistSessionTrack
          : api.playlistSessionAlbum,
        {
          id: id,
        }
      )
      .then(function (response) {
        console.log(response);
        getPlaylistSession();
        if (response.status === 200) {
          showSuccess("Successfully added to playlist");
        }
      })
      .catch(function (error) {
        console.error(error);
        showError("Error adding to playlist");
      });
  }

  function removeFromSession(id, itemType) {
    axios
      .delete(
        itemType === "track"
          ? api.playlistSessionTrack
          : api.playlistSessionAlbum,
        {
          params: { id: id },
        }
      )
      .then(function (response) {
        console.log(response);
        getPlaylistSession();
        if (response.status === 200) {
          showSuccess("Successfully removed from playlist");
        }
      })
      .catch(function (error) {
        console.error(error);
        showError("Error removing from playlist");
      });
  }

  function isInTrackSession(songId) {
    for (let i = 0; i < sessionTracks.length; i++) {
      if (songId === sessionTracks[i].id) {
        return true;
      }
    }
    return false;
  }

  function isInAlbumSession(albumId) {
    for (let i = 0; i < sessionAlbums.length; i++) {
      if (albumId === sessionAlbums[i].id) {
        return true;
      }
    }
    return false;
  }

  function renderSearch() {
    let searchModes;
    if (match.params.contentType === "albums") {
      searchModes = ["name", "release_date", "artist_name"];
    } else if (match.params.contentType === "artists") {
      searchModes = ["name"];
    } else if (match.params.contentType === "songs") {
      searchModes = ["name", "album_name", "artist_name", "release_date"];
    }

    return (
      <Search
        key={match.params.contentType}
        resource={match.params.contentType}
        searchModes={searchModes}
        getAutoItems={getAutoItems}
        params={params}
        setParams={setParams}
      />
    );
  }

  function renderBrowse() {
    let browseOptions;
    if (match.params.contentType === "albums") {
      browseOptions = {
        name: alphaNumArray(),
      };
    } else if (match.params.contentType === "artists") {
      browseOptions = {
        name: alphaNumArray(),
        genre: genres === undefined ? [] : genres,
      };
    } else if (match.params.contentType === "songs") {
      browseOptions = {
        name: alphaNumArray(),
      };
    }

    return (
      <Browse
        key={match.params.contentType + "browse"}
        browseOptions={browseOptions}
        params={params}
        setParams={setParams}
      />
    );
  }

  function renderSortBy() {
    let sortOptions;
    if (match.params.contentType === "albums") {
      sortOptions = ["popularity", "name", "release_date", "artist_name"];
    } else if (match.params.contentType === "artists") {
      sortOptions = ["popularity", "name"];
    } else if (match.params.contentType === "songs") {
      sortOptions = ["popularity", "name", "release_date", "album_name"];
    }

    return (
      <SortBy
        key={match.params.contentType}
        sortOptions={sortOptions}
        params={params}
        setParams={setParams}
      />
    );
  }

  function renderContentCards() {
    if (match.params.contentType === "songs") {
      return (songs != null
        ? songs
        : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
      ).map(function (song, index) {
        return (
          <SongCard
            song={song}
            key={index}
            style={{ margin: "32px" }}
            onClick={() => {
              if (songs != null && songs.length > 0) selectCard(song.id);
            }}
            skeletonPulse={songs != null && songs.length > 0 ? undefined : true}
            addToSession={addToSession}
            removeFromSession={removeFromSession}
            isInSession={isInTrackSession(song.id)}
          ></SongCard>
        );
      });
    } else if (match.params.contentType === "albums") {
      return (albums != null
        ? albums
        : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]
      ).map(function (album, index) {
        return (
          <AlbumCard
            album={album}
            key={index}
            style={{ margin: "32px" }}
            onClick={() => {
              if (albums != null && albums.length > 0) selectCard(album.id);
            }}
            skeletonPulse={
              albums != null && albums.length > 0 ? undefined : true
            }
            addToSession={addToSession}
            removeFromSession={removeFromSession}
            isInSession={isInAlbumSession(album.id)}
          ></AlbumCard>
        );
      });
    } else if (match.params.contentType === "artists") {
      return (artists != null ? artists : [1, 2, 3, 4, 5, 6, 7, 8]).map(
        function (artist, index) {
          return (
            <ArtistCard
              artist={artist}
              key={index}
              style={{ margin: "32px" }}
              onClick={() => {
                if (artists != null && artists.length > 0)
                  selectCard(artist.id);
              }}
              skeletonPulse={
                artists != null && artists.length > 0 ? undefined : true
              }
            ></ArtistCard>
          );
        }
      );
    }

    return null;
  }

  return (
    <div className="content-view">
      {renderToaster()}
      <ExpandableCart
        sessionTracks={sessionTracks}
        sessionAlbums={sessionAlbums}
        getsOwnData={false}
        removeFromSession={removeFromSession}
      />
      {willRedirectAlbum ? (
        <Redirect push to={"/app/explore/albums/" + selectedCardId}></Redirect>
      ) : null}
      {willRedirectArtist ? (
        <Redirect push to={"/app/explore/artists/" + selectedCardId}></Redirect>
      ) : null}
      {willRedirectSong ? (
        <Redirect push to={"/app/explore/songs/" + selectedCardId}></Redirect>
      ) : null}
      <div className="content-view-content">
        <div className="content-view-options">
          <OptionToggle
            key="option-toggle"
            options={["Search Mode", "Browse Mode"]}
            selectedOption={browseMode}
            selectOption={setBrowseMode}
          />
        </div>
        <div className="content-view-browse">
          {browseMode === "Search Mode" ? renderSearch() : renderBrowse()}
        </div>
        <div className="content-view-filter-wrapper">{renderSortBy()}</div>
        <div className="content-view-cards">{renderContentCards()}</div>
        <div
          className="content-view-filter-wrapper"
          style={{ marginTop: "64px", marginBottom: "0px" }}
        >
          <Paginate params={params} setParams={setParams} />
        </div>
      </div>
    </div>
  );
}

export default ContentView;
