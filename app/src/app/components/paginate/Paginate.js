import React, { useState, useEffect } from "react";
import "./Paginate.css";

import { Card } from "../../containers";

function Paginate(props) {
  function leftClick() {
    if (props.params.offset / props.params.limit + 1 > 1) {
      props.setParams({
        ...props.params,
        offset: props.params.offset - props.params.limit,
      });
    }
  }

  function rightClick() {
    props.setParams({
      ...props.params,
      offset: props.params.offset + props.params.limit,
    });
  }

  return (
    <Card
      className="paginate-filter"
      innerStyle={{
        display: "flex",
        flexDirection: "row",
        margin: "8px 24px 8px 24px",
      }}
    >
      <div className="paginate-filter-controls">
        <p onClick={leftClick}>{"<"} </p>
        <p>{props.params.offset / props.params.limit + 1}</p>
        <p onClick={rightClick}> {">"}</p>
      </div>
    </Card>
  );
}

export default Paginate;
