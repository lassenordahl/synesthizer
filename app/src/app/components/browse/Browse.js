import React, { useState, useEffect } from "react";
import "./Browse.css";

import { Card } from "../../containers";

import { beautifyString } from "../../../global/helper";

function Browse(props) {
  const [selections, setSelections] = useState(
    Object.assign(
      {},
      ...Object.keys(props.browseOptions).map((option) => ({
        [option]: props.params[option],
      }))
    )
  );

  useEffect(() => {
    props.setParams({
      ...props.params,
      ...selections,
    });
  }, [selections]);

  return (
    <Card
      innerStyle={{
        margin: "20px 24px 20px 24px",
      }}
    >
      <div className="options">
        {Object.keys(selections).map(function (option) {
          console.log(option);
          console.log(selections);
          console.log(props.browseOptions);
          return (
            <div key={option} className="options-option">
              <p>{`${beautifyString(option)}: `}</p>
              <select
                value={selections[option]}
                onChange={(e) =>
                  setSelections({
                    ...selections,
                    [option]:
                      e.currentTarget.value === "--"
                        ? undefined
                        : e.currentTarget.value,
                  })
                }
              >
                <option value={null}>--</option>
                {props.browseOptions[option].map(function (selection, index) {
                  return (
                    <option
                      className="option-selection"
                      key={selection}
                      value={selection}
                    >
                      {selection}
                    </option>
                  );
                })}
              </select>
            </div>
          );
        })}
      </div>
    </Card>
  );
}

export default Browse;
