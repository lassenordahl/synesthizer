import React from "react";
import "./Button.css";

function Button(props) {
  function getButtonColor() {
    if (props.isPrimary) {
      return "button-primary";
    }
    if (props.isBlue) {
      return "button-blue";
    }
    if (props.isGreen) {
      return "button-green";
    }
    return "";
  }

  return (
    <div
      style={props.style}
      type={props.type}
      className={
        "button " +
        (props.className !== undefined ? props.className : "") +
        " " +
        getButtonColor()
      }
      onClick={() => props.onClick()}
    >
      {props.children}
    </div>
  );
}

export default Button;
