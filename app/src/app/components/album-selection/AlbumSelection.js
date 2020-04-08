import React, { useState, useEffect } from "react";
import "./AlbumSelection.css";

import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";

function AlbumSelection(props) {

  // useEffect(() => {
  //   setTimeout(() => {
  //     setShowDetails(true);
  //   }, 500);
  // }, []);

  return (
    <React.Fragment>
      <div className="selected-view-main-info">
        <div className="selected-view-album-art">
          {props.album !== null ? (
            <img alt="album art" src={props.album.image} />
          ) : (
            <SkeletonPulse></SkeletonPulse>
          )}
        </div>
        <div className="selected-view-details">
          <h2>
            {props.album !== null ? (
              props.album.name
            ) : (
              <SkeletonPulse style={{ width: "160px", height: "24px" }} />
            )}
          </h2>
          <p>
            {props.album !== null ? (
              props.album.artist_name
            ) : (
              <SkeletonPulse style={{ width: "256px", height: "24px" }} />
            )}
          </p>
          <p>
            {props.album !== null ? (
              "Release Date: " + props.album.release_date
            ) : (
              <SkeletonPulse style={{ width: "128px", height: "24px" }} />
            )}
          </p>
        </div>
      </div>
      {/* {showDetails ? (
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
      ) : null} */}
    </React.Fragment>
  );
}

export default AlbumSelection;
