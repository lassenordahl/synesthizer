import React from "react";
import './Button.css';

function Button(props) {

  function getButtonColor() {
    if (props.isPrimary) {
      return "button-primary";
    }
    if (props.isBlue) {
      return "button-blue";
    }
    if (props.isGreen) {

    }
    return "";
  }

  return (
    <div style={props.style} className={"button " + getButtonColor()} onClick={() => props.onClick()}>
      {props.children}
    </div>
  );
}

export default Button;
