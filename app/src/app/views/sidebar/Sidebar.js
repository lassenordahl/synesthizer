import React from "react";
import './Sidebar.css';

import { Link } from "react-router-dom"

import { Card } from "./../../containers";

function Sidebar(props) {
  return (
    <div className={"sidebar" + (props.showSidebar ? " sidebar-expanded" : "")}>
      { props.showSidebar ? 
        <React.Fragment>
          <Link to="/app/songs">
            Songs
          </Link>
          <Link to="/app/albums">
            Albums
          </Link>
          <Link to="/app/artists">
            Artists
          </Link>
        </React.Fragment>
        : null 
      }
    </div>
  );
}

export default Sidebar;
