import React from "react";
import "./AlbumCard.css";
import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";

function AlbumCard(props) {
  return (
    <div className="album-card" style={props.style} onClick={() => props.onClick()}>
      <div className="album-card-margin">
        <div className="album-card-album-art">
          { props.skeletonPulse === undefined ? 
            <img
              alt="album art"
              src={props.album.image}
            ></img> :
            <SkeletonPulse></SkeletonPulse> 
          }
        </div>
        <div className="album-card-info">
          <h2>
            { props.skeletonPulse === undefined ? 
              props.album.name :
              <SkeletonPulse style={{width: "128px", height: "24px"}}/>
            }
          </h2>
          <p>
          { props.skeletonPulse === undefined ? 
              props.album.artist_name :
              <SkeletonPulse style={{width: "200px", height: "20px"}}/>
            }
          </p>
          <p className="subtitle">
            { props.skeletonPulse === undefined ? 
              "Release Date: " + props.album.release_date :
              <SkeletonPulse style={{width: "81px", height: "12px"}}/>
            }
          </p>
        </div>
      </div>
    </div>
  );
}

export default AlbumCard;
