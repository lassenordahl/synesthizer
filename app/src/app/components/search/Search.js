import React, { useState, useEffect } from "react";
import "./Search.css";

import { Autocomplete } from "react-autocomplete";
import { beautifyString } from "../../../global/helper";
import { Button } from "../index";

function Search(props) {
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
      <Autocomplete
        key="search"
        placeholder="Search"
        value={search}
        onChange={(e) => setSearch(e.currentTarget.value)}
      />
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
