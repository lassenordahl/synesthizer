import React from "react";
import "./ArtistCard.css";
import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";

function ArtistCard(props) {
  return (
    <div className="artist-card" style={props.style} onClick={() => props.onClick()}>
      <div className="artist-card-margin">
        <div className="artist-card-album-art">
          { props.skeletonPulse === undefined ? 
            <img
              alt="album art"
              src="https://99designs-blog.imgix.net/blog/wp-content/uploads/2017/12/attachment_68585523.jpg"
            ></img> :
            <SkeletonPulse></SkeletonPulse> 
          }
        </div>
        <div className="artist-card-info">
          <h2>
            { props.skeletonPulse === undefined ? 
              "The Weeknd" :
              <SkeletonPulse style={{width: "128px", height: "24px"}}/>
            }
          </h2>
        </div>
      </div>
    </div>
  );
}

export default ArtistCard;
