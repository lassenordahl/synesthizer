import React, { useState, useEffect } from "react";
import "./ExpandableCart.css";

import axios from "axios";
import { Link } from "react-router-dom";

import { Button, DeleteSessionButton } from "../../components";
import api from "../../../api";

function ExpandableCart(props) {
  const [isExpanded, setIsExpanded] = useState(false);
  const [showTrackContent, setShowTrackContent] = useState(false);

  // Session Tracks
  const [sessionTracks, setSessionTracks] = useState([]);

  useEffect(() => {
    if (isExpanded) {
      // Get the data if it's specified to, this is so we can nicely load the cart total
      // by passing data in from the outside on changes
      if (props.getsOwnData) {
        getPlaylistSession();
      }

      setTimeout(() => {
        setShowTrackContent(true);
      }, 500);
    } else {
      setShowTrackContent(false);
    }
  }, [isExpanded]);

  useEffect(() => {
    if (!props.getsOwnData) {
      setSessionTracks(props.sessionTracks);
    }
  }, [props.sessionTracks]);

  function getPlaylistSession() {
    axios
      .get(api.playlistSession)
      .then(function (response) {
        console.log(response);
        setSessionTracks(response.data.tracks);
      })
      .catch(function (error) {
        console.error(error);
      });
  }

  return (
    <div className="expandable-cart-wrapper">
      {isExpanded ? (
        <div
          className="expandable-cart-opaque-cover fade-in"
          onClick={() => {
            setIsExpanded(!isExpanded);
          }}
        />
      ) : null}
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
                          <div className="card-content-song-row-wrapper">
                            <div>
                              <p>{track.name}</p>
                              <span style={{ fontSize: "0.5em" }}>
                                {track.artists[0].name}
                              </span>
                            </div>
                          </div>
                          <DeleteSessionButton onClick={() => {props.removeFromSession(track.id)}}/>
                        </div>
                      </React.Fragment>
                    );
                  })
                : null}
            </div>
            <Link to="/app/user/playlists/create">
              <Button isGreen={true} onClick={() => {}} className="create-playlist-button fade-in">
                Create Playlist
              </Button>
            </Link>
          </div>
        ) : sessionTracks.length > 0 ? (
          sessionTracks.length
        ) : (
          "cart"
        )}
      </div>
    </div>
  );
}

export default ExpandableCart;
