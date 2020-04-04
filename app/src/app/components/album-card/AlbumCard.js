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
              src="https://99designs-blog.imgix.net/blog/wp-content/uploads/2017/12/attachment_68585523.jpg"
            ></img> :
            <SkeletonPulse></SkeletonPulse> 
          }
        </div>
        <div className="album-card-info">
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

export default AlbumCard;
