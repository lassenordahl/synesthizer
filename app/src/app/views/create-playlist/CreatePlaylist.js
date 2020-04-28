import React, { useState, useEffect } from "react";
import "./CreatePlaylist.css";

import axios from "axios";
import { Link } from "react-router-dom";

import { Card } from "./../../containers";
import { Button, SkeletonPulse, DeleteSessionButton } from "./../../components";
import { useToast } from "./../../../hooks";
import { api, getRoute } from "../../../utils/api";

function CreatePlaylist() {
  // Session state variable
  const [playlistSession, setPlaylistSession] = useState(null);

  // Create a seperate playlist variable for efficiency when updating (don't need to update entire session object anytime a character changes in the name)
  const [playlistName, setPlaylistName] = useState("");

  // Toaster for error and success handling
  const [showSuccess, showError, renderToast] = useToast();

  useEffect(() => {
    getSessionPlaylist();
  }, []);

  function getSessionPlaylist() {
    axios
      .get(api.playlistSession)
      .then(function (response) {
        if (response.data.name === undefined) {
          setPlaylistName("");
        } else {
          setPlaylistName(response.data.name);
        }
        if (response.data.image === undefined) {
          response.data.image = "";
        }

        setPlaylistSession(response.data);
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function handleChange(e) {
    setPlaylistName(e.target.value);
  }

  async function createPlaylist() {
    // Create a deep copy so we don't manipulate what's currently in the session
    let postPlaylist = JSON.parse(JSON.stringify(playlistSession));
    postPlaylist.name = playlistName;
    postPlaylist.image = "https://picsum.photos/200";

    if (playlistName === "" || playlistName === undefined) {
      showError("Playlist needs a unique name");
      return;
    }

    // Get the tracks from the albums
    let albumTracks = await getTracksFromAlbums();
    postPlaylist.tracks = postPlaylist.tracks.concat(albumTracks);

    axios
      .post(api.playlist, postPlaylist)
      .then(function (response) {
        if (response.status === 200) {
          showSuccess("Successfully created playlist");
          getSessionPlaylist();
        }
      })
      .catch(function (error) {
        showError("Error creating playlist");
      });
  }

  async function getTracksFromAlbums() {
    let tracksFromAlbums = [];
    let albumResponses = await Promise.all(
      playlistSession.albums.map(function (album) {
        return axios.get(api.tracksForAlbum, {
          params: {
            id: album.id,
          },
        });
      })
    );
    for (let i = 0; i < albumResponses.length; i++) {
      if (
        albumResponses[i] !== undefined &&
        albumResponses[i].data !== undefined
      ) {
        for (let j = 0; j < albumResponses[i].data.length; j++) {
          console.log(albumResponses[i].data[j]);
          tracksFromAlbums.push(albumResponses[i].data[j]);
        }
      }
    }
    return tracksFromAlbums;
  }

  function savePlaylist() {
    let putPlaylist = playlistSession;
    putPlaylist.name = playlistName;
    console.log(putPlaylist);
    axios
      .put(api.playlistSession, putPlaylist)
      .then(function (response) {
        console.log(response);
        getSessionPlaylist();
        if (response.status === 200) {
          showSuccess("Your playlist saved successfully");
        }
      })
      .catch(function (error) {
        console.error(error);
        showError("Error saving playlist");
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
        getSessionPlaylist();
        if (response.status === 200) {
          showSuccess("Successfully removed playlist");
        }
      })
      .catch(function (error) {
        console.error(error);
        showError("Error removing from playlist");
      });
  }

  return (
    <div className="create-playlist">
      {renderToast()}
      <Card className="create-playlist-card">
        <div className="create-playlist-header">
          <div className="create-playlist-header-picture">
            {playlistSession !== null && playlistSession.image !== "" ? (
              <img alt="playlist-profile" />
            ) : (
              <div className="clouds-wrapper">
                <div className="cloud">
                  <div className="cloud-one" />
                  <div className="cloud-two" />
                </div>
                <p>Upload Image</p>
              </div>
            )}
          </div>
          <div className="create-playlist-header-info">
            {playlistSession !== null ? (
              <input
                value={playlistName}
                onChange={handleChange}
                placeholder="Playlist Name"
              ></input>
            ) : (
              <SkeletonPulse
                style={{ width: "400px", height: "56px", borderRadius: "30px" }}
              ></SkeletonPulse>
            )}
            <p>Playlist generated by user</p>
            <Button style={{ width: "200px" }} isBlue={true}>
              Generate Smart Playlist
            </Button>
          </div>
        </div>
        <div className="create-playlist-songs">
          <h3>songs</h3>
          {playlistSession !== null && playlistSession.tracks.length > 0 ? (
            <div className="create-playlist-song-labels format-column-grid">
              <div>name</div>
              <div>artist</div>
              <div>album</div>
              <div className="flex-center">length</div>
              <div></div>
            </div>
          ) : null}
          {playlistSession !== null ? (
            playlistSession.tracks.length > 0 ? (
              <div>
                {playlistSession.tracks.map(function (song, index) {
                  return (
                    <div
                      key={index}
                      className="format-column-grid formatted-song-row"
                    >
                      <Link to={"/app/explore/songs/" + song.id}>
                        <div>{song.name}</div>
                      </Link>
                      <Link to={"/app/explore/artists/" + song.artists[0].id}>
                        <div>{song.artists[0].name}</div>
                      </Link>
                      <Link to={"/app/explore/albums/" + song.album.id}>
                        <div>{song.album.name}</div>
                      </Link>
                      <div className="flex-center">{song.duration_ms}</div>
                      <div className="flex-center">
                        <DeleteSessionButton
                          onClick={() => removeFromSession(song.id, "track")}
                        />
                      </div>
                    </div>
                  );
                })}
              </div>
            ) : (
              <div className="create-playlist-no-songs">
                <p>no songs added to playlist</p>
              </div>
            )
          ) : null}
        </div>
        <div className="create-playlist-songs">
          <h3>albums</h3>
          {playlistSession !== null && playlistSession.albums.length > 0 ? (
            <React.Fragment>
              <div className="create-playlist-song-labels format-column-grid-album">
                <div>name</div>
                <div>artist</div>
                <div></div>
              </div>
              <div>
                {playlistSession.albums.map(function (album, index) {
                  return (
                    <div
                      key={index}
                      className="format-column-grid-album formatted-song-row"
                    >
                      <Link to={"/app/explore/albums/" + album.id}>
                        <div>{album.name}</div>
                      </Link>
                      <Link to={"/app/explore/artists/" + album.artists[0].id}>
                        <div>{album.artists[0].name}</div>
                      </Link>
                      <div className="flex-center">
                        <DeleteSessionButton
                          onClick={() => removeFromSession(album.id, "album")}
                        />
                      </div>
                    </div>
                  );
                })}
              </div>
            </React.Fragment>
          ) : (
            <div className="create-playlist-no-songs">
              <p>no albums added to playlist</p>
            </div>
          )}
        </div>
      </Card>
      <div className="create-playlist-button-wrapper">
        <Button isGreen={true} onClick={() => savePlaylist()}>
          Save Playlist
        </Button>
        <div style={{ width: "48px" }} />
        <Button isPrimary={true} onClick={() => createPlaylist()}>
          Create Playlist
        </Button>
      </div>
    </div>
  );
}

export default CreatePlaylist;
