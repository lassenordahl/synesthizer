import React from "react";
import './GoBackButton.css';

import { useHistory } from "react-router-dom";

function GoBackButton(props) {

  let history = useHistory();

  return (
    <span 
      className={"subtitle " + props.className}
      onClick={() => {
        history.goBack();
      }}
    >
      go back to {props.prevRoute}
    </span>
  );
}

export default GoBackButton;
