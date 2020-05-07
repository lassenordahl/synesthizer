import React from "react";
import "./Dashboard.css";

import { Card } from "./../../containers";

function DashboardInfo(props) {
  return (
    <div className="dashboard-info">
      <h2>Database Information</h2>
      <p>Welcome Admin! Take a look at your database statistics:</p>
      <div className="dashboard-database-info">
        <div className="dashboard-info-div1">
          <h3>Artists</h3>
          <p>Artist Count: </p>
        </div>
        <div className="dashboard-info-div2">
          <h3>Albums</h3>
          <p>Album Count: </p>
        </div>
        <div className="dashboard-info-div3">
          <h3>Songs</h3>
          <p>Song Count: </p>
        </div>
      </div>
    </div>
  );
}

function SpotifyCards(props) {
  return (
    <div>
      <h2 style={{ marginBottom: "36px" }}>Available Songs</h2>
      <input placeholder="Song Name" style={{marginBottom: "36px"}}></input>
      {[1, 2, 3, 4, 5].map(function (index) {
        return (
          <div className="database-song-card" key={index}>
            <div>
              <h3>Spotify Songs</h3>
            </div>
          </div>
        );
      })}
    </div>
  );
}

function Dashboard() {
  return (
    <div className="dashboard">
      <div className="div1">
        <div className="dashboard-card">
          <DashboardInfo />
        </div>
      </div>
      <div className="div2">
        <div className="dashboard-card" style={{ height: "100%" }}>
          <SpotifyCards />
        </div>
      </div>
      <div className="div3">
        <div className="dashboard-card dashboard-card-song-list">
          <div>
            <h2>Add Song</h2>
            <input placeholder="Song ID"></input>
            <input placeholder="Song Name"></input>
            <input placeholder="Artist ID"></input>
          </div>
        </div>
      </div>
      <div className="div4">
        <div className="dashboard-card">
          <div>
            <h2>Add Artist</h2>
            <input placeholder="Artist ID"></input>
            <input placeholder="Artist Name"></input>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
