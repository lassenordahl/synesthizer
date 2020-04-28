import React, { useState } from "react";
import "./SortBy.css";

import { Card } from "../../containers";
import { useEffect } from "react";
import { beautifyString } from "../../../global/helper";

function SortBy(props) {
  function getInitialOrders(params) {
    let initialOrders = {};
    if (params.sortBy === undefined || params.sortBy === "") {
      return initialOrders;
    }

    params.sortBy.split(",").forEach((order) => {
      const [property, value] = order.split(" ");
      initialOrders = { ...initialOrders, [property]: value };
    });

    return initialOrders;
  }

  function getInitialPriorities(params, defaults) {
    let priorities = [];
    if (params.sortBy === undefined || params.sortBy === "") {
      return defaults;
    }

    // Iterate over params and add to priority
    params.sortBy.split(",").forEach((pair) => {
      const [property, value] = pair.split(" ");
      priorities.push(property);
    });

    // Filter defaults that are not in priority
    // Add filtered to the front
    priorities = [
      ...defaults.filter((property) => !priorities.includes(property)),
      ...priorities,
    ];

    return priorities;
  }

  const [orders, setOrders] = useState(
    Object.assign(
      {},
      ...props.sortOptions.map((option) => ({
        [option]: getInitialOrders(props.params)[option],
      }))
    )
  );

  const [priority, setPriority] = useState(
    getInitialPriorities(props.params, Object.keys(orders))
  );

  useEffect(() => {
    setOrders(
      Object.assign(
        {},
        ...props.sortOptions.map((option) => ({
          [option]: getInitialOrders(props.params)[option],
        }))
      )
    );
    setPriority(getInitialPriorities(props.params, Object.keys(orders)));
  }, [props.params]);

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

    const newOrders = {
      ...orders,
      [option]: order,
    };

    const newPriority = [
      ...priority.slice(0, prevChange),
      ...priority.slice(prevChange + 1, props.sortOptions.length),
      priority[prevChange],
    ];

    setOrders(newOrders);
    setPriority(newPriority);

    props.setParams({
      ...props.params,
      sortBy: newPriority
        .map(function (option, id) {
          if (newOrders[option] !== undefined) {
            return `${option} ${newOrders[option]}`;
          }
        })
        .filter(function (value) {
          if (value !== undefined) {
            return true;
          }
        })
        .join(","),
    });
  }

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
        {beautifyString(option) + orderSymbol}
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
