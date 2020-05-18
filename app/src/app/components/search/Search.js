import React, { useState, useEffect } from "react";
import "./Search.css";

import Autocomplete from "react-autocomplete";
import { Link, useHistory } from "react-router-dom";
import { beautifyString } from "../../../global/helper";
import { Button } from "../index";

function Search(props) {
  let history = useHistory();

  const [searchMode, setSearchMode] = useState(
    props.params.searchMode !== undefined
      ? props.params.searchMode
      : props.searchModes[0]
  );

  const [search, setSearch] = useState(
    props.params.search !== undefined ? props.params.search : ""
  );

  // localStorage.setItem("spotifyAuth", JSON.stringify(spotifyParams));
  // localStorage.getItem("spotifyAuth")
  // Should be set to local storage stuff
  const [autoReqLock, setAutoReqLock] = useState(false);

  const [autoItems, setAutoItems] = useState(
    localStorage.getItem(props.resource + props.searchMode)
      ? localStorage.getItem(props.resource + props.searchMode)
      : []
  );

  useEffect(() => {
    setSearchMode(
      props.params.searchMode !== undefined
        ? props.params.searchMode
        : props.searchModes[0]
    );
    setSearch(props.params.search !== undefined ? props.params.search : "");
  }, [props.params]);

  function sendSearch() {
    console.log("sending search");
    if (search === "") {
      return;
    }

    props.setParams({
      ...props.params,
      searchMode: searchMode,
      search: search,
    });
  }

  function selectAuto(val) {
    history.push(`/app/explore/${props.resource}/${val}`);
  }

  function getAutoItems() {
    if (search.length < 3) {
      return;
    }

    if (autoReqLock) {
      return;
    }

    setAutoReqLock(true);
    console.log("Autocomplete Search Initialized");
    // Check the cache
    // Send the request
    // Print wether using cash or sending request
    props.getAutoItems(searchMode, search, setAutoItems);
    // Print the suggestion list
    setTimeout(() => {
      setAutoReqLock(false);
    }, 300);
  }

  return (
    <div className="search">
      <select
        value={searchMode}
        onChange={(e) => setSearchMode(e.currentTarget.value)}
      >
        {props.searchModes.map(function (mode, index) {
          return (
            <option className="search-mode" key={mode} value={mode}>
              {beautifyString(mode)}
            </option>
          );
        })}
      </select>
      <div className="search-bar-auto">
        <Autocomplete
          key="search-bar"
          placeholder="Search"
          autoHighlight={false}
          getItemValue={(item) => item}
          items={autoItems}
          menuStyle={{
            position: "",
          }}
          renderItem={(item, isHighlighted) => {
            return (
              <div
                key={item.value}
                className="search-bar-auto-item"
                style={{
                  background: isHighlighted ? "#ff7668" : "white",
                  color: isHighlighted ? "white" : "black",
                }}
              >
                <div className="search-bar-auto-item-art">
                  <img alt="auto art" src={item.image} />
                </div>
                <div className="search-bar-auto-info">
                  <p>{item.name}</p>
                  <p>{item.release_date}</p>
                </div>
              </div>
            );
          }}
          value={search}
          inputProps={{
            placeholder: "Search",
            onKeyPress: (event) => {
              if (event.key === "Enter") {
                sendSearch();
              }
            },
          }}
          onChange={(e) => {
            setSearch(e.currentTarget.value);
            getAutoItems();
          }}
          onSelect={(item) => {
            setSearch(item.value);
            selectAuto(item.id);
          }}
        />
      </div>
      <div style={{ width: "48px" }}></div>
      <Button type="submit" isPrimary={true} onClick={sendSearch}>
        Search
      </Button>
    </div>
  );
}

export default Search;

/* <input
        key="search"
        placeholder="Search"
        value={search}
        onChange={(e) => setSearch(e.currentTarget.value)}
      ></input> */
