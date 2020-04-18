import React from "react";
import "./SessionButton.css";

function SessionButton(props) {
  return (
    <div
      className={
        "song-card-playlist-button " +
        (props.isSelected ? "song-card-playlist-button-selected" : "") +
        (props.classname !== undefined ? props.className : "")
      }
      onClick={(e) => {
        // Stop propagation to prevent from clicking parent element
        e.stopPropagation();
        props.onClick()
      }}
    >
      <span className="checkmark">
        <div className={"checkmark_stem" + (props.isSelected ? " checkmark-selected" : "")}></div>
        <div className={"checkmark_kick" + (props.isSelected ? " checkmark-selected" : "")}></div>
      </span>
    </div>
  );
}

export default SessionButton;
