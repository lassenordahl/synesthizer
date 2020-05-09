import React, { useEffect, useState } from "react";
import "./Dashboard.css";

import axios from "axios";

import { Card } from "./../../containers";
import { Button } from "../../components";
import { api } from "../../../utils/api";
import { useSpotify, useToast } from "../../../hooks";
import { truncateTitle } from "../../../global/helper";

function DashboardInfo(props) {
  return (
    <div className="dashboard-info">
      <h2>Database Information</h2>
      <p>Welcome Admin! Take a look at your database statistics. Quick note about the dashboard, to maintain Spotify data validity, we only permit adding data directly from the Spotify API within our app. Please search for songs to add using the search bar on the right. Thanks!</p>
      <div className="dashboard-database-info">
        <div className="dashboard-info-div1">
          <h3>Artists</h3>
          <p>
            Count:{" "}
            {props.databaseMeta ? props.databaseMeta.artist_count : 0}
          </p>
        </div>
        <div className="dashboard-info-div2">
          <h3>Albums</h3>
          <p>
            Count:{" "}
            {props.databaseMeta ? props.databaseMeta.album_count : 0}
          </p>
        </div>
        <div className="dashboard-info-div3">
          <h3>Songs</h3>
          <p>
            Count:{" "}
            {props.databaseMeta ? props.databaseMeta.track_count : 0}
          </p>
        </div>
      </div>
    </div>
  );
}

function DatabaseTables(props) {
  return (
    <div className="dashboard-card">
      <div className="database-tables">
        <h2>Database Tables</h2>

        {props.databaseMeta ? (
          props.databaseMeta.tables.map(function (table, index) {
            return (
              <div className="table-card" key={index}>
                <div>
                  <p style={{ marginBottom: "24px" }}>{table.name}</p>
                  {table.attributes.map(function (attribute, indexTwo) {
                    return <p key={indexTwo}>{attribute}</p>;
                  })}
                </div>
              </div>
            );
          })
        ) : (
          <p> No tables available</p>
        )}
      </div>
    </div>
  );
}

function SpotifyCards(props) {
  const { spotifyAuth } = useSpotify();
  const [searchedTracks, setSearchedTracks] = useState([]);
  const [searchField, setSearchField] = useState("");

  function handleChange(e) {
    setSearchField(e.target.value);
  }

  function searchItems() {
    axios
      .get(api.spotifySearch + `?q=${searchField}&type=track`, {
        headers: { Authorization: `Bearer ${spotifyAuth.access_token}` },
      })
      .then(function (response) {
        if (response === undefined) {
          props.showError("Invalid search params");
        } else if (response.status === 200) {
          setSearchedTracks(response.data.tracks.items);
          props.showSuccess("Pulled Spotify tracks");
        }
      })
      .catch(function (error) {
        console.log("An error has occured", error);
        props.showError("Error searching spotify tracks");
      });
  }

  function fillInTrack(selectedTrack) {
    console.log(selectedTrack);
    props.setSong({
      id: selectedTrack.id,
      name: selectedTrack.name,
      track_number: selectedTrack.track_number,
      album_id: selectedTrack.album.id,
      artist_id: selectedTrack.artists[0].id,
      duration_ms: selectedTrack.duration_ms
    });
    props.setAlbum({
      id: selectedTrack.album.id,
      name: selectedTrack.album.name,
      image: selectedTrack.album.images[0].url,
      album_type: selectedTrack.album.album_type,
      release_date: selectedTrack.album.release_date,
      artist_id: selectedTrack.artists[0].id,
    });
    props.setArtist({
      id: selectedTrack.artists[0].id,
      name: selectedTrack.artists[0].name,
      image: "https://picsum.photos/200", // Need to use default value for now
    });
  }

  return (
    <div className="search-songs">
      <h2 style={{ marginBottom: "36px", marginTop: "16px" }}>Spotify Songs</h2>
      <input
        placeholder="Search Songs"
        style={{ marginBottom: "36px" }}
        value={searchField}
        onChange={handleChange}
      ></input>
      <Button
        isGreen={true}
        style={{
          marginBottom: "36px",
          marginLeft: "calc(50% - 80px)",
        }}
        onClick={() => searchItems()}
      >
        Search
      </Button>
      {searchedTracks.map(function (track, index) {
        return (
          <div
            className="database-song-card"
            key={index}
            onClick={() => {
              fillInTrack(track);
            }}
          >
            <img src={track.album.images[0].url} alt="album-art"></img>
            <div>
              <h3>{truncateTitle(track.name, 24)}</h3>
              <p>{track.artists[0].name}</p>
              <p className="subtitle">{track.album.release_date}</p>
            </div>
          </div>
        );
      })}
    </div>
  );
}

function Dashboard() {
  const [showSuccess, showError, renderToast] = useToast();

  const [databaseMeta, setDatabaseMeta] = useState(null);

  const [song, setSong] = useState({
    id: "",
    name: "",
    track_number: "",
    artist_id: "",
    album_id: "",
    duration_ms: "",
  });
  const [album, setAlbum] = useState({
    id: "",
    name: "",
    image: "",
    album_type: "",
    release_date: "",
    artist_id: "",
  });
  const [artist, setArtist] = useState({
    id: "",
    name: "",
    image: "",
  });

  useEffect(() => {
    getDatabaseMeta();
  }, []);

  function getDatabaseMeta() {
    axios
      .get(api.databaseMeta)
      .then(function (response) {
        if (response.status === 200) {
          setDatabaseMeta(response.data.meta);
        }
      })
      .catch(function (error) {
        console.log("Error retrieving dashboard information");
      });
  }

  function addArtist() {
    axios
      .post(api.artist, artist)
      .then(function (response) {
        if (response) {
          setArtist({
            id: "",
            name: "",
            image: "",
          });
          showSuccess("Artists has been added to database");
        } else {
          showError("Artist already exists in the database");
        }
        getDatabaseMeta();
      })
      .catch(function (error) {
        console.log("Error retrieving dashboard information", error);
      });
  }

  function addAlbum() {
    axios
      .post(api.album, album)
      .then(function (response) {
        if (response) {
          setAlbum({
            id: "",
            name: "",
            image: "",
            album_type: "",
            release_date: "",
            artist_id: "",
          });
          showSuccess("Album submitted to database");
        } else {
          showError("Album already exists in database");
        }
        getDatabaseMeta();
      })
      .catch(function (error) {
        console.log("Error retrieving dashboard information", error);
      });
  }

  function addTrack() {
    axios
      .post(api.song, song)
      .then(function (response) {
        if (response) {
          setSong({
            id: "",
            name: "",
            track_number: "",
            artist_id: "",
            album_id: "",
            duration_ms: "",
          });
          showSuccess("Song submitted to database");
        } else {
          showError("Song already exists in database");
        }
        getDatabaseMeta();
      })
      .catch(function (error) {
        console.log("Error retrieving dashboard information", error);
      });
  }

  return (
    <div className="dashboard">
      {renderToast()}
      <div className="div1">
        <div className="dashboard-card">
          <DashboardInfo databaseMeta={databaseMeta} />
        </div>
        <div className="dashboard-card" style={{ marginTop: "36px" }}>
          <div>
            <h2>Add Artist</h2>
            <input value={artist.id} placeholder="ID" readonly></input>
            <input value={artist.name} placeholder="Name" readonly></input>
          </div>
        </div>
        <Button
          isPrimary={true}
          style={{ marginLeft: "auto", marginTop: "36px" }}
          onClick={() => {
            addArtist();
          }}
        >
          Add Artist
        </Button>
        <div className="dashboard-card" style={{ marginTop: "36px" }}>
          <div className="album-wrapper">
            <div>
              <h2>Add Album</h2>
              <input value={album.id} placeholder="ID" readonly></input>
              <input value={album.name} placeholder="Name" readonly></input>
              <input
                value={album.release_date}
                placeholder="Release Date"
                readonly
              ></input>
            </div>
            <div className="album-wrapper-art-wrapper">
              {album.image !== "" ? (
                <img
                  src={album.image}
                  style={{ marginTop: "38px" }}
                  alt="album-art"
                ></img>
              ) : (
                <p>Please select an image</p>
              )}
            </div>
          </div>
        </div>
        <Button
          isPrimary={true}
          style={{ marginLeft: "auto", marginTop: "36px" }}
          onClick={() => {
            addAlbum();
          }}
        >
          Add Album
        </Button>
        <div className="dashboard-card" style={{ marginTop: "36px" }}>
          <div>
            <h2>Add Song</h2>
            <input value={song.id} placeholder="ID" readonly></input>
            <input value={song.name} placeholder="Name" readonly></input>
            <input
              value={song.track_number}
              placeholder="Track Number"
              readonly
            ></input>
            <input
              value={song.duration_ms}
              placeholder="Duration in MS"
              readonly
            ></input>
          </div>
        </div>
        <Button
          isPrimary={true}
          style={{
            marginLeft: "auto",
            marginTop: "36px",
            marginBottom: "36px",
          }}
          onClick={() => {
            addTrack();
          }}
        >
          Add Song
        </Button>
        <DatabaseTables databaseMeta={databaseMeta} />
      </div>
      <div className="div2">
        <div className="dashboard-card" style={{ height: "100%" }}>
          <SpotifyCards
            setSong={setSong}
            setAlbum={setAlbum}
            setArtist={setArtist}
            showError={showError}
            showSuccess={showSuccess}
          />
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
