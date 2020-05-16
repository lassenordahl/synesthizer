import React from "react";
import "./SongAutoItem.css";

function SongAutoItem({ item }) {
  return (
    <React.Fragment>
      <div className="song-auto-item-art">
        <img alt="auto art" src={item.album.image} />
      </div>
      <div className="song-auto-info">
        <p
          style={{
            fontWeight: "bold",
          }}
        >
          {item.name}
        </p>
        <p>{item.album.name}</p>
      </div>
    </React.Fragment>
  );
}

export default SongAutoItem;
