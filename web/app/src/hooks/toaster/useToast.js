import React, { useState, useEffect } from "react";
import "./Toaster.css";

import useInterval from "@use-it/interval";

function useToast() {
  const [toastStack, setToastStack] = useState([]);
  const [delay, setDelay] = useState(5000);

  useInterval(() => {
    console.log('slicing');
    setToastStack(toastStack.slice(1));
  }, delay);

  function showSuccess(text) {
    setToastStack([
      ...toastStack,
      {
        text: text,
        type: "success",
      },
    ]);
    setDelay(5000);
  }

  function showError(text) {
    setToastStack([
      ...toastStack,
      {
        text: text,
        type: "error",
      },
    ]);
    setDelay(5000);
  }

  function getLabelColor(type) {
    if (type === "success") {
      return "toaster-label-green";
    } else if (type === "error") {
      return "toaster-label-red";
    }
    return "";
  }

  function renderToast() {
    return (
      <React.Fragment>
        {toastStack.map(function (toast, index) {
          return (
            <div
              key={index}
              className="toaster fade-in"
              style={{
                animationDuration: "0.5s",
                top: `calc(24px + ${index} * 72px)`,
              }}
            >
              <div className={"toaster-label " + getLabelColor(toast.type)} />
              <div className="toast-text">
                <p>{toast.text}</p>
              </div>
            </div>
          );
        })}
      </React.Fragment>
    );
  }

  return [showSuccess, showError, renderToast];
}

export default useToast;
