import React, { useState, useEffect } from "react";
import "./SongView.css";

import { Card } from "../../containers";
import { SkeletonPulse } from "../../components";

function SongView({ props, match }) {
  const [expandCard, setExpandCard] = useState(false);
  const [showDetails, setShowDetails] = useState(false);

  // Expand the card after the API request is made for the content
  useEffect(() => {
    setTimeout(() => {
      setExpandCard(true);

      setTimeout(() => {
        setShowDetails(true);
      }, 500);
    }, 250);
  }, []);

  return (
    <div className="song-view">
      <div
        className={
          "song-view-card" + (expandCard ? " song-view-card-expanded" : "")
        }
      >
        <div className="song-view-song-info">
          <div className="song-view-main-info">
            <div className="song-view-album-art">
              <SkeletonPulse></SkeletonPulse>
            </div>
            <div className="song-view-details">
              <h2>
                <SkeletonPulse style={{ width: "160px", height: "24px" }} />
              </h2>
              <p>
                <SkeletonPulse style={{ width: "256px", height: "24px" }} />
              </p>
              <p>
                <SkeletonPulse style={{ width: "128px", height: "24px" }} />
              </p>
            </div>
          </div>
          {showDetails ? (
            <div className="song-view-extra-info">
              {[1, 2, 3, 4, 5, 6, 7, 8, 9].map(function (item, index) {
                return (
                  <div
                    className={"fade-in"}
                    style={{
                      height: "20px",
                      width: "110px",
                      margin: "24px 0px 0px 24px",
                      animationDelay: index / 6 + 's',
                    }}
                  >
                    <SkeletonPulse></SkeletonPulse>
                  </div>
                );
              })}
            </div>
          ) : null}
        </div>
      </div>
    </div>
  );
}

export default SongView;
