import React, { useState, useEffect } from "react";
import "./SongsView.css";

import {
  Redirect
} from 'react-router-dom';

import { Button, SkeletonPulse } from "../../components";
import { Card } from "../../containers";

function SongsView() {

  // Selection Variables
  const [selectedCardId, setSelectedCardId] = useState(null);

  // Redirect Variables
  const [willRedirectSong, redirectSong] = useState(false);
  const [willRedirectArtist, redirectArtist] = useState(false);
  const [willRedirectAlbum, redirectAlbum] = useState(false);

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
  }, [selectedCardId])

  function selectCard(id) {
    setSelectedCardId(id);
  }

  return (
    <div className="songs-page">
      { willRedirectSong ? <Redirect push to={"/songs/" + selectedCardId}></Redirect> : null}
      <div className="songs-page-content">
        <div className="songs-page-search">
          <input></input>
          <div style={{ width: "48px" }}></div>
          <Button isPrimary={true}>Search</Button>
        </div>
        <div className="songs-page-cards">
          {[1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15].map(function (
            item,
            index
          ) {
            return (
              <Card 
                key={index} 
                style={{ margin: "24px" }} 
                willHover={true}
                onClick={() => selectCard(item)}
              >
                <div style={{width: '30px', height: '30px'}}>
                  <SkeletonPulse></SkeletonPulse>
                </div>
              </Card>
            );
          })}
        </div>
      </div>
    </div>
  );
}

export default SongsView;
