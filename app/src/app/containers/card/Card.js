import React from "react";
import "./Card.css";

function Card(props) {
  return (
    <div
      className={"card " + (props.willHover ? " card-hover" : "")}
      style={props.style}
      onClick={() => (props.onClick !== undefined ? props.onClick() : null)}
    >
      <div className="card-margin">{props.children}</div>
    </div>
  );
}

export default Card;
