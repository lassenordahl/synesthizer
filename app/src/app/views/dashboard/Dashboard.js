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
      <p>Welcome Admin! Take a look at your database statistics:</p>
      <div className="dashboard-database-info">
        <div className="dashboard-info-div1">
          <h3>Artists</h3>
          <p>
            Artist Count:{" "}
            {props.databaseMeta ? props.databaseMeta.artist_count : 0}
          </p>
        </div>
        <div className="dashboard-info-div2">
          <h3>Albums</h3>
          <p>
            Album Count:{" "}
            {props.databaseMeta ? props.databaseMeta.album_count : 0}
          </p>
        </div>
        <div className="dashboard-info-div3">
          <h3>Songs</h3>
          <p>
            Song Count:{" "}
            {props.databaseMeta ? props.databaseMeta.track_count : 0}
          </p>
        </div>
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
      artist_id: selectedTrack.artists[0].id
    });
    props.setAlbum({
      id: selectedTrack.album.id,
      name: selectedTrack.album.name,
      image: selectedTrack.album.images[0].url,
      album_type: selectedTrack.album.album_type,
      release_date: selectedTrack.album.release_date,
      artist_id: selectedTrack.artists[0].id
    });
    props.setArtist({
      id: selectedTrack.artists[0].id,
      name: selectedTrack.artists[0].name,
      image: "https://picsum.photos/200" // Need to use default value for now
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
  });
  const [album, setAlbum] = useState({
    id: "",
    name: "",
    image: "",
    album_type: "",
    release_date: "",
    artist_id: ""
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

  function submitTrackInfo() {
    addArtist();
  }

  function addArtist() {
    axios.post(api.artist, artist)
      .then(function(response) {
        addAlbum(response === undefined ? "Artist already exists in database. ": "Added artist. ");
      })
      .catch(function(error) {
        console.log("Error retrieving dashboard information", error);
      });
  }

  function addAlbum(text) {
    axios.post(api.album, album)
      .then(function(response) {
        addTrack(text + (response === undefined ? "Album already exists in database. ": "Added album. "));
      })
      .catch(function(error) {
        console.log("Error retrieving dashboard information", error);
      });
  }

  function addTrack(text) {
    axios.post(api.song, song)
      .then(function(response) {
        console.log(response);
        if (response === undefined) {
          showErrorWrapper(text + "Track already exists in database.");
        } else if (response.status === 200) {
          showSuccess(text + "Successfully posted information to database.");
        }

        setSong({
          id: "",
          name: "",
          track_number: "",
          artist_id: "",
          album_id: "",
        });
        setAlbum({
          id: "",
          name: "",
          image: "",
          album_type: "",
          release_date: "",
          artist_id: ""
        });
        setArtist({
          id: "",
          name: "",
          image: "",
        });

        getDatabaseMeta();
      })
      .catch(function(error) {
        console.log("Error retrieving dashboard information", error);
      });
  }

  async function showErrorWrapper(text) {
    showError(text)
  }

  return (
    <div className="dashboard">
      {renderToast()}
      <div className="div1">
        <div className="dashboard-card">
          <DashboardInfo databaseMeta={databaseMeta}/>
        </div>
        <div className="dashboard-card" style={{ marginTop: "36px" }}>
          <div>
            <h2>Add Song</h2>
            <input value={song.id} placeholder="ID"></input>
            <input value={song.name} placeholder="Name"></input>
            <input value={song.track_number} placeholder="Track Number"></input>
          </div>
        </div>
        <div className="dashboard-card" style={{ marginTop: "36px" }}>
          <div className="album-wrapper">
            <div>
              <h2>Add Album</h2>
              <input value={album.id} placeholder="ID"></input>
              <input value={album.name} placeholder="Name"></input>
              <input
                value={album.release_date}
                placeholder="Release Date"
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
        <div className="dashboard-card" style={{ marginTop: "36px" }}>
          <div>
            <h2>Add Artist</h2>
            <input value={artist.id} placeholder="ID"></input>
            <input value={artist.name} placeholder="Name"></input>
          </div>
        </div>
        <Button
          isPrimary={true}
          style={{ marginLeft: "auto", marginTop: "36px" }}
          onClick={() => {
            submitTrackInfo();
          }}
        >
          Add Song Information
        </Button>
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
