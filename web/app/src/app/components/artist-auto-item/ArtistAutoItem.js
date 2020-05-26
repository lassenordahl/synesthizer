import React from "react";
import "./ArtistAutoItem.css";

import { truncateTitle } from "../../../global/helper";

function ArtistAutoItem({ item }) {
  return (
    <React.Fragment>
      <div className="artist-auto-item-art">
        <img alt="auto art" src={item.image} />
      </div>
      <div className="artist-auto-info">
        <p
          style={{
            fontWeight: "bold",
          }}
        >
          {item.name}
        </p>
        <p>{item.genres ? truncateTitle(item.genres.join(", "), 40) : null}</p>
      </div>
    </React.Fragment>
  );
}

export default ArtistAutoItem;
