import React from "react";
import './Button.css';

function Button(props) {

  function getButtonColor() {
    if (props.isPrimary) {
      return "button-primary"
    }
    return "";
  }

  return (
    <div className={"button " + getButtonColor()} onClick={() => props.onClick()}>
      {props.children}
    </div>
  );
}

export default Button;
