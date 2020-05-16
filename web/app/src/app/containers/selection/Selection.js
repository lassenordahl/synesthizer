import React, { useState, useEffect } from "react";
import "./Selection.css";

function Selection(props) {
  const [expandCard, setExpandCard] = useState(false);

  useEffect(() => {
    setTimeout(() => {
      setExpandCard(true);
    }, 200);
  }, []);

  return (
    <div className="selected-view">
      <div
        className={
          "selected-view-card" +
          (expandCard ? " selected-view-card-expanded" : "")
        }
      >
        <div className="selected-view-info">{props.children}</div>
      </div>
    </div>
  );
}

export default Selection;
