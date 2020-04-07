import React, { useState, useEffect } from "react";
import "./SelectedView.css";

import axios from "axios";

import { SkeletonPulse } from "../../components";

import api from "api.js";

function SelectedView({ props, match }) {
  const [expandCard, setExpandCard] = useState(false);
  const [showDetails, setShowDetails] = useState(false);

  const [album, setAlbum] = useState(null);

  useEffect(() => {
    if (match.params.contentType === "albums") {
      getAlbum();
    }
  }, []);

  // Expand the card after the API request is made for the content
  useEffect(() => {
    setTimeout(() => {
      setExpandCard(true);

      setTimeout(() => {
        setShowDetails(true);
      }, 500);
    }, 250);
  }, []);

  function getAlbum() {
    axios
      .get(api.album, {
        params: {
          id: match.params.itemId,
        },
      })
      .then(function (response) {
        console.log(response);
        setAlbum(response.data);
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  function renderCard() {
    if (match.params.contentType === "albums") {
      return (
        <div className="selected-view-song-info">
          <div className="selected-view-main-info">
            <div className="selected-view-album-art">
              {album !== null ? (
                <img alt="album art" src={album.image} />
              ) : (
                <SkeletonPulse></SkeletonPulse>
              )}
            </div>
            <div className="selected-view-details">
              <h2>
                {album !== null ? (
                  album.name
                ) : (
                  <SkeletonPulse style={{ width: "160px", height: "24px" }} />
                )}
              </h2>
              <p>
                {album !== null ? (
                  album.artist_name
                ) : (
                  <SkeletonPulse style={{ width: "256px", height: "24px" }} />
                )}
              </p>
              <p>
                {album !== null ? (
                  "Release Date: " + album.release_date
                ) : (
                  <SkeletonPulse style={{ width: "128px", height: "24px" }} />
                )}
              </p>
            </div>
          </div>
          {showDetails ? (
            <div className="selected-view-extra-info">
              {[1, 2, 3, 4, 5, 6, 7, 8, 9].map(function (item, index) {
                return (
                  <div
                    className={"fade-in"}
                    style={{
                      height: "20px",
                      width: "110px",
                      margin: "24px 0px 0px 24px",
                      animationDelay: index / 6 + "s",
                    }}
                  >
                    <SkeletonPulse></SkeletonPulse>
                  </div>
                );
              })}
            </div>
          ) : null}
        </div>
      );
    }
    return null;
  }

  return (
    <div className="selected-view">
      <div
        className={
          "selected-view-card" +
          (expandCard ? " selected-view-card-expanded" : "")
        }
      >
        {renderCard()}
      </div>
    </div>
  );
}

export default SelectedView;
