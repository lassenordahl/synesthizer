import React, { useState } from "react";
import "./ExpandableCart.css";

function ExpandableCart(props) {
  const [isExpanded, setIsExpanded] = useState(false);

  return (
    <div className="expandable-cart-wrapper">
      <div
        className={
          "expandable-cart " +
          (isExpanded ? " expandable-cart-expanded " : "") +
          (props.className !== undefined ? props.className : "")
        }
        onClick={() => setIsExpanded(!isExpanded)}
      >
        { isExpanded ? 
          <div className="cart-content">
            <div className="cart-content-header" style={{animationDelay: '0.25s'}}>
              Playlist  
            </div>

          </div>
        : "cart"}
      </div>
    </div>
  );
}

export default ExpandableCart;
