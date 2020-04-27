import React, { useState } from "react";
import "./Search.css";

import { beautifyString } from "../../../global/helper";
import { Button } from "../index";

function Search(props) {
  const [searchMode, setSearchMode] = useState(props.searchModes[0]);
  const [search, setSearch] = useState("");

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
      <input
        key="search"
        placeholder="Search"
        value={search}
        onChange={(e) => setSearch(e.currentTarget.value)}
      ></input>
      <div style={{ width: "48px" }}></div>
      <Button type="submit" isPrimary={true} onClick={sendSearch}>
        Search
      </Button>
    </div>
  );
}

export default Search;
