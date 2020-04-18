import React, { useState, useEffect } from "react";
import "./ExpandableCart.css";

import axios from "axios";

import api from "../../../api";

let sampleTracks = [
  {
    id: "0IkKz2J93C94Ei4BvDop7P",
    name: "Party Rock Anthem",
    track_number: 3,
    duration_ms: 0,
    popularity: 7,
    artists: [
      {
        id: "3sgFRtyBnxXD5ESfmbK4dl",
        name: "LMFAO",
        image:
          "https://i.scdn.co/image/2d75f5222d8a92d5e12419ae3e5238261f943df6",
      },
      {
        id: "3sgFRtyBnxXD5ESfmbK4dl",
        name: "LMFAO",
        image:
          "https://i.scdn.co/image/2d75f5222d8a92d5e12419ae3e5238261f943df6",
      },
    ],
    album: {
      id: "1MbBSfcqLg2OjkeZ1RMSIq",
      name: "Sorry For Party Rocking",
      album_type: "album",
      image: "https://i.scdn.co/image/ab67616d0000b273d77a9a738c99b8c4f7a7c3ee",
      release_date: "2011-01-01",
      popularity: 0,
    },
  },
  {
    id: "5jlEc6MceTTBMqaN2AxLQq",
    name: "Sanctus, JD 6",
    track_number: 7,
    duration_ms: 0,
    popularity: 6,
    artists: [
      {
        id: "3DL7TTcfjDAJiDrhBl3q2H",
        name: "John Dunstable",
        image:
          "https://i.scdn.co/image/ab67616d0000b27348177682ea003003e0f4be95",
      },
    ],
    album: {
      id: "42dF8lsWScG35n44SXsOdR",
      name:
        "Dunstable: Quam Pulchra Es / Veni Sancte Spiritus / Mass Movements",
      album_type: "album",
      image: "https://i.scdn.co/image/ab67616d0000b27348177682ea003003e0f4be95",
      release_date: "2005-10-18",
      popularity: 0,
    },
  },
];

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
        setSessionTracks(sampleTracks);
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
              onClick={() => {
                setIsExpanded(!isExpanded);
              }}
            >
              <h3>playlist</h3>
            </div>
            <div className="cart-content-songs">
              {showTrackContent
                ? sessionTracks.map(function (track, index) {
                    return (
                      <div
                        className="cart-content-song-row fade-in"
                        style={{ animationDelay: 0.1 * index + "s" }}
                      >
                        <h4>{track.name}</h4>
                      </div>
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
