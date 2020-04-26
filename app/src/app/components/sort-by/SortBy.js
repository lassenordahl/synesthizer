import React, { useState } from "react";
import "./SortBy.css";

import { Card } from "../../containers";
import { useEffect } from "react";
import { capitalizeFirstLetter } from "../../../global/helper";

function SortBy(props) {
  const [orders, setOrders] = useState(
    Object.assign(
      {},
      ...props.sortOptions.map((option) => ({ [option]: undefined }))
    )
  );

  const [priority, setPriority] = useState(Object.keys(orders));

  //   props.sortOptions.map(function (option) {
  //     return option;
  //   })

  function toggleOrder(option) {
    console.log("toggled" + option);
    console.log(props.sortOptions);
    let prevChange = 0;
    for (let i = 0; i < priority.length; i++) {
      if (priority[i] === option) {
        prevChange = i;
        break;
      }
    }

    let order;
    if (orders[option] === "desc") {
      order = "asc";
    } else if (orders[option] === "asc") {
      order = undefined;
    } else {
      order = "desc";
    }

    setOrders({
      ...orders,
      [option]: order,
    });

    console.log(orders);

    setPriority([
      ...priority.slice(0, prevChange),
      ...priority.slice(prevChange + 1, props.sortOptions.length),
      priority[prevChange],
    ]);

    console.log(priority);
  }

  useEffect(() => {
    props.setParams({
      ...props.params,
      sortBy: priority
        .map(function (option, id) {
          if (orders[option] !== undefined) {
            return `${option} ${orders[option]}`;
          }
        })
        .filter(function (value) {
          if (value !== undefined) {
            return true;
          }
        })
        .join(","),
    });
  }, [priority, orders]);

  function renderOption(option, id) {
    let orderSymbol = "";

    if (orders[option] === "desc") {
      orderSymbol = "↡";
    } else if (orders[option] === "asc") {
      orderSymbol = "↟";
    } else {
      orderSymbol = "";
    }

    let marginRight = {};
    if (orderSymbol !== "") {
      if (id < priority.length - 1) {
        marginRight = { marginRight: "18.5px" };
      } else {
        marginRight = { marginRight: "-11.5px" };
      }
    }

    return (
      <p
        key={id}
        id={id}
        style={marginRight}
        onClick={() => toggleOrder(option)}
      >
        {capitalizeFirstLetter(option) + orderSymbol}
      </p>
    );
  }

  return (
    <Card
      className="sort-by-filter"
      innerStyle={{
        display: "flex",
        flexDirection: "row",
        margin: "8px 24px 8px 24px",
      }}
    >
      {Object.keys(orders).map(function (option, id) {
        return renderOption(option, id);
      })}
    </Card>
  );
}

export default SortBy;
