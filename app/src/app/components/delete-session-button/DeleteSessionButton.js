import React from "react";
import "./DeleteSessionButton.css";

function DeleteSessionButton(props) {
  return (
    <div
      className={
        "remove-playlist-button " +
        (props.isSelected ? "remove-playlist-button-selected" : "") +
        (props.classname !== undefined ? props.className : "")
      }
      onClick={(e) => {
        // Stop propagation to prevent from clicking parent element
        e.stopPropagation();
        props.onClick()
      }}
    >
      <span className="remove-playlist-X">
        <div className={"remove-playlist-X-left"}></div>
        <div className={"remove-playlist-X-right"}></div>
      </span>
    </div>
  );
}

export default DeleteSessionButton;
