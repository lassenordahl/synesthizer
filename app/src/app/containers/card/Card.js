import React from "react";
import "./Card.css";

function Card(props) {
  return (
    <div
      className={
        "card" +
        (props.willHover ? " card-hover" : "") +
        " " +
        (props.className !== undefined ? props.className : "")
      }
      style={props.style}
      onClick={() => (props.onClick !== undefined ? props.onClick() : null)}
    >
      <div className={"card-margin"} style={props.innerStyle}>{props.children}</div>
    </div>
  );
}

export default Card;
