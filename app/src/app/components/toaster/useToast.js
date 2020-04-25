import React, { useState, useEffect } from "react";
import './Toaster.css';

function useToast() {

  const [show, setShow] = useState(false);
  const [type, setType] = useState("");
  const [text, setText] = useState("");

  useEffect(() => {
    if (show === true) {
      // After 5 seconds hide the toast
      setTimeout(() => {
        setShow(false);
      }, 5000);
    }
  }, [show]); 

  function showSuccess(text) {
    setType("success");
    setShow(true);
    setText(text);
  }

  function showError(text) {
    setType("error");
    setShow(true);
    setText(text);
  }

  function getLabelColor() {
    if (type === "success") {
      return "toaster-label-green";
    } else if (type === "error") {
      return "toaster-label-red";
    }
    return "";
  }

  function renderToast() {
    return show 
    ?  (
        <div className="toaster fade-in" style={{ animationDuration: "0.5s"}}>
          <div className={"toaster-label " + getLabelColor()}/>
          <div className="toast-text">
            <p>
              {text}
            </p>
          </div>
        </div>
      )
    : null
  }

  return [showSuccess, showError, renderToast]
}

export default useToast;
