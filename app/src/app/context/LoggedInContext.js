import React, { useState, useEffect, createContext } from "react";
import { useCookies } from "react-cookie";

// Create Context Object
export const LoggedInContext = createContext();

// Create a provider for components to consume and subscribe to changes
export const LoggedInContextProvider = (props) => {
  const [loggedIn, setLoggedIn] = useState(false);
  const [cookies, setCookie] = useCookies([]);

  useEffect(() => {
    if (cookies.logged_in === "true") {
      setLoggedIn(true);
    }
  }, []);

  return (
    <LoggedInContext.Provider value={[loggedIn, setLoggedIn]}>
      {props.children}
    </LoggedInContext.Provider>
  );
};
