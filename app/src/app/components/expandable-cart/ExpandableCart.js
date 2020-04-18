import React, { useState, useEffect } from "react";
import "./ExpandableCart.css";

import axios from "axios";

import api from "../../../api";

function ExpandableCart(props) {
  const [isExpanded, setIsExpanded] = useState(false);
  const [showTrackContent, setShowTrackContent] = useState(false);
  const [sessionTracks, setSessionTracks] = useState([]);

  useEffect(() => {
    if (isExpanded) {
      getSessionTracks();

      setTimeout(() => {
        setShowTrackContent(true);
      }, 500);
    } else {
      setShowTrackContent(false);
    }
  }, [isExpanded]);

  function getSessionTracks() {
    axios
      .get(api.playlistSession)
      .then(function (response) {
        console.log(response);
        // setSessionTracks(response.data.tracks);
        setSessionTracks(response.data.tracks);
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  return (
    <div className="expandable-cart-wrapper">
      <div
        className={
          "expandable-cart " +
          (isExpanded ? " expandable-cart-expanded " : "") +
          (props.className !== undefined ? props.className : "")
        }
        onClick={() => {
          if (!isExpanded) {
            setIsExpanded(!isExpanded);
          }
        }}
      >
        {isExpanded ? (
          <div className="cart-content">
            <div
              className="cart-content-header"
              style={{ animationDelay: "0.25s" }}
            >
              <h3>playlist</h3>
              <p className="fade-in">{sessionTracks.length} songs</p>
              <div
                className="X"
                onClick={() => {
                  setIsExpanded(!isExpanded);
                }}
              >
                <div className="X-left" />
                <div className="X-right" />
              </div>
            </div>
            <div className="cart-content-songs">
              {showTrackContent
                ? sessionTracks.map(function (track, index) {
                    return (
                      <React.Fragment key={index}>
                        <div
                          className="cart-content-song-row fade-in"
                          style={{ animationDelay: 0.1 * index + "s" }}
                        >
                          <img src={track.album.image} alt="album-art"></img>
                          <div>
                            <p>{track.name}</p>
                            <span style={{ fontSize: "0.5em" }}>
                              {track.artists[0].name}
                            </span>
                          </div>
                        </div>
                        {/* {index !== sessionTracks.length - 1 ? (
                          <div
                            className="light-border fade-in"
                            style={{ animationDelay: 0.1 * index + "s" }}
                          ></div>
                        ) : null} */}
                      </React.Fragment>
                    );
                  })
                : null}
            </div>
          </div>
        ) : (
          "cart"
        )}
      </div>
    </div>
  );
}

export default ExpandableCart;
