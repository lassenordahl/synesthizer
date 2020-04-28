import React, { useState, useEffect } from "react";
import "./Paginate.css";

import { Card } from "../../containers";

function Paginate(props) {
  const [limit, setLimit] = useState(parseInt(props.params.limit));

  useEffect(() => {
    setLimit(parseInt(props.params.limit));
  }, [props.params]);

  useEffect(() => {
    props.setParams({
      ...props.params,
      offset: parseInt(props.params.offset),
      limit: limit,
    });
  }, [limit]);

  function leftClick() {
    if (props.params.offset / limit + 1 > 1) {
      props.setParams({
        ...props.params,
        offset: parseInt(props.params.offset) - limit,
        limit: limit,
      });
    }
  }

  function rightClick() {
    props.setParams({
      ...props.params,
      offset: parseInt(props.params.offset) + limit,
      limit: limit,
    });
  }

  return (
    <div className="page-controls">
      <div className="page-controls-paginate-filter">
        <Card
          innerStyle={{
            display: "flex",
            flexDirection: "row",
            margin: "8px 24px 8px 24px",
          }}
        >
          <div className="page-controls-paginate-filter-controls">
            <p onClick={leftClick}>{"<"} </p>
            <p>{props.params.offset / limit + 1}</p>
            <p onClick={rightClick}> {">"}</p>
          </div>
        </Card>
      </div>
      <div className="page-controls-limit-filter">
        <Card
          innerStyle={{
            width: "30px",
            display: "flex",
            flexDirection: "column",
            margin: "8px 10px 8px 10px",
          }}
        >
          <select
            value={limit}
            onChange={(e) => setLimit(e.currentTarget.value)}
          >
            <option key={20} value={20}>
              20
            </option>
            <option key={25} value={25}>
              25
            </option>
            <option key={40} value={40}>
              40
            </option>
            <option key={80} value={80}>
              80
            </option>
            <option key={100} value={100}>
              100
            </option>
          </select>
        </Card>
      </div>
    </div>
  );
}

export default Paginate;
