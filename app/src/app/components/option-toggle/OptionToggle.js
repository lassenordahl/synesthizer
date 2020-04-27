import React from "react";
import "./OptionToggle.css";

import { Button } from "../index";
import { Card } from "../../containers";

function OptionToggle(props) {
  return (
    <Card
      innerStyle={{
        margin: "18px 24px 18px 24px",
      }}
    >
      <div className="options-filter">
        {props.options.map(function (option) {
          return (
            <div key={option} className="options-filter-button">
              <Button
                isBlue={props.selectedOption === option ? true : false}
                onClick={() => props.selectOption(option)}
              >
                {option}
              </Button>
            </div>
          );
        })}
      </div>
    </Card>
  );
}

export default OptionToggle;
