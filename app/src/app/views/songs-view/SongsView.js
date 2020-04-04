import React, { useState, useEffect } from "react";
import "./SongsView.css";

import { Redirect } from "react-router-dom";

import { Button, SkeletonPulse, SongCard } from "../../components";
import { Card } from "../../containers";

function SongsView() {
  // Selection Variables
  const [selectedCardId, setSelectedCardId] = useState(null);

  // Redirect Variables
  const [willRedirectSong, redirectSong] = useState(false);
  const [willRedirectArtist, redirectArtist] = useState(false);
  const [willRedirectAlbum, redirectAlbum] = useState(false);

  const [songs, setSongs] = useState(null);

  useEffect(() => {
    if (willRedirectArtist) {
      redirectArtist(false);
    }
    if (willRedirectSong) {
      redirectSong(false);
    }
  }, [willRedirectArtist, willRedirectAlbum]);

  useEffect(() => {
    if (selectedCardId !== null) {
      redirectSong(true);
    }
  }, [selectedCardId]);

  function selectCard(id) {
    setSelectedCardId(id);
  }

  return (
    <div className="songs-page">
      {willRedirectSong ? (
        <Redirect push to={"/app/songs/" + selectedCardId}></Redirect>
      ) : null}
      <div className="songs-page-content">
        <div className="songs-page-search">
          <input></input>
          <div style={{ width: "48px" }}></div>
          <Button isPrimary={true}>Search</Button>
        </div>
        <div className="songs-page-filter-wrapper">
          <Card
            className="songs-page-filter"
            innerStyle={{
              display: "flex",
              "flex-direction": "row",
              margin: "8px 24px 8px 24px",
            }}
          >
            <p>Song Name</p>
            <p>Artist</p>
            <p>Album</p>
            <p>Popularity</p>
          </Card>
        </div>
        <div className="songs-page-cards">
          {[1, 2, 3, 4, 5, 6].map(function (item, index) {
            return (
              <SongCard
                song={item}
                key={index}
                style={{ margin: "24px" }}
                onClick={() => selectCard(item)}
                skeletonPulse
              ></SongCard>
            );
          })}
        </div>
        <div className="songs-page-filter-wrapper" style={{marginTop: '64px', marginBottom: '0px'}}>
          <Card
            className="songs-page-filter"
            innerStyle={{
              display: "flex",
              "flex-direction": "row",
              margin: "8px 24px 8px 24px",
            }}
          >
            <p>1</p>
          </Card>
        </div>
      </div>
    </div>
  );
}

export default SongsView;
