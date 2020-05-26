import React from "react";
import "./AlbumAutoItem.css";

import { truncateTitle } from "../../../global/helper";

function AlbumAutoItem({ item }) {
  return (
    <React.Fragment>
      <div className="album-auto-item-art">
        <img alt="auto art" src={item.image} />
      </div>
      <div className="album-auto-info">
        <p
          style={{
            fontWeight: "bold",
          }}
        >
          {truncateTitle(item.name, 40)}
        </p>
        <p>
          {item.artists
            ? truncateTitle(
                item.artists.map((artist) => artist.name).join(", "),
                40
              )
            : null}
        </p>
      </div>
    </React.Fragment>
  );
}

export default AlbumAutoItem;
