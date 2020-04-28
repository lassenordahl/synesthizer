import React, { useState, useEffect } from "react";
import { Link, useHistory } from "react-router-dom";
import "./ArtistSelection.css";
import AlbumCard from "../album-card/AlbumCard";
import SkeletonPulse from "../skeleton-pulse/SkeletonPulse";

function SongSelection(props) {
  let history = useHistory();

  const [showContent, setShowContent] = useState(false);
  const [showDetails, setShowDetails] = useState(false);

  useEffect(() => {
    if (props.artist !== null) {
      setShowDetails(true);
      setShowContent(true);
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
              (props.artist.genres ? props.artist.genres : []).map(function (
                genre,
                index
              ) {
                return (
                  <Link
                    to={`/app/explore/artists?browseMode=Browse%20Mode&genre=${encodeURI(
                      genre
                    )}`}
                  >
                    {index < props.artist.genres.length - 1
                      ? genre + ", "
                      : genre}
                  </Link>
                );
              })
            ) : (
              <SkeletonPulse style={{ width: "160px", height: "24px" }} />
            )}
          </h3>
        </div>
      </div>
      {showDetails ? (
        <div className="selected-artist-extra-info">
          {props.artistAlbums.map(function (album, index) {
            return (
              <AlbumCard
                album={album}
                key={index}
                style={{
                  margin: "32px",
                  "background-color": "#1d222e",
                }}
                onClick={() => {
                  history.push(`/app/explore/albums/${album.id}`);
                }}
                skeletonPulse={showContent ? undefined : true}
              ></AlbumCard>
            );
          })}
        </div>
      ) : null}
    </React.Fragment>
  );
}

export default SongSelection;
