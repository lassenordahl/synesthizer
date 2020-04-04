import React from "react";
import './Sidebar.css';

function Sidebar(props) {
  return (
    <div className={"sidebar" + (props.showSidebar ? " sidebar-expanded" : "")}>
      { props.showSidebar ? 
        <React.Fragment>
          Sidebar
        </React.Fragment>
        : null 
      }
    </div>
  );
}

export default Sidebar;
