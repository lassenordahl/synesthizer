import React, { useState, useEffect } from "react";
import "./ArtistSelection.css";
import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";

function SongSelection(props) {
  const [showContent, setShowContent] = useState(false);
  const [showDetails, setShowDetails] = useState(false);

  useEffect(() => {
    if (props.artist !== null) {
      console.log(props.artist);
      setTimeout(() => {
        setShowDetails(true);
        setTimeout(() => setShowContent(true), 2000);
      }, 500);
    }
  }, [props.artist]);

  return (
    <React.Fragment>
      <div className="selected-artist-main-info">
        <div className="selected-artist-song-art">
          {showContent ? (
            <img
              className={"fade-in"}
              alt="song art"
              src={props.artist.image}
            />
          ) : (
            <SkeletonPulse></SkeletonPulse>
          )}
        </div>
        <div className="selected-artist-details">
          <h2>
            {showContent ? (
              <div className="fade-in">{props.artist.name}</div>
            ) : (
              <SkeletonPulse style={{ width: "160px", height: "24px" }} />
            )}
          </h2>
          <h3>
            {showContent ? (
              props.artist.genres.map(function (genre, index) {
                return index < props.artist.genres.length - 1
                  ? genre + ", "
                  : genre;
              })
            ) : (
              <SkeletonPulse style={{ width: "160px", height: "24px" }} />
            )}
          </h3>
        </div>
      </div>
      {showDetails ? (
        <div className="selected-artist-extra-info">
          {[1, 2, 3, 4, 5, 6, 7, 8, 9].map(function (attribute, index) {
            return (
              <div
                className={"fade-in selected-artist-extra-info-item"}
                style={{ animationDelay: index / 9 + "s" }}
              >
                <SkeletonPulse></SkeletonPulse>
              </div>
            );
          })}
        </div>
      ) : null}
    </React.Fragment>
  );
}

export default SongSelection;
