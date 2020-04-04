import React from "react";
import "./SongCard.css";
import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";

function SongCard(props) {
  return (
    <div className="song-card" style={props.style} onClick={() => props.onClick()}>
      <div className="song-card-margin">
        <div className="song-card-album-art">
          { props.skeletonPulse === undefined ? 
            <img
              alt="album art"
              src="https://99designs-blog.imgix.net/blog/wp-content/uploads/2017/12/attachment_68585523.jpg"
            ></img> :
            <SkeletonPulse></SkeletonPulse> 
          }
        </div>
        <div className="song-card-info">
          <h2>
            { props.skeletonPulse === undefined ? 
              "Blinding Lights" :
              <SkeletonPulse style={{width: "128px", height: "24px"}}/>
            }
          </h2>
          <p>
          { props.skeletonPulse === undefined ? 
              "The Weeknd - After Hours" :
              <SkeletonPulse style={{width: "200px", height: "20px"}}/>
            }
          </p>
          <p className="subtitle">
            { props.skeletonPulse === undefined ? 
              "Playlist Count: 18" :
              <SkeletonPulse style={{width: "81px", height: "12px"}}/>
            }
          </p>
        </div>
      </div>
    </div>
  );
}

export default SongCard;
