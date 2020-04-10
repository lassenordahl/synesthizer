import React from "react";
import './SkeletonPulse.css';

function SkeletonPulse(props) {
  return (
    <div className="skeleton-pulse" style={props.style}/>
  );
}

export default SkeletonPulse;
